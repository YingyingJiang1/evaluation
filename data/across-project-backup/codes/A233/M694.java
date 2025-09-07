    private double getUniqueUserCount(String method, Optional<String> endpoint) {
        Set<String> uniqueUsers = new HashSet<>();
        meterRegistry.find("http.requests").tag("method", method).counters().stream()
                .filter(
                        counter -> {
                            String uri = counter.getId().getTag("uri");

                            // Skip if uri is null
                            if (uri == null) {
                                return false;
                            }

                            // For POST requests, only include if they start with /api/v1
                            if ("POST".equals(method) && !uri.contains("api/v1")) {
                                return false;
                            }

                            if (uri.contains(".txt")) {
                                return false;
                            }

                            // For GET requests, validate if we have a list of valid endpoints
                            final boolean validateGetEndpoints =
                                    endpointInspector.getValidGetEndpoints().size() != 0;
                            if ("GET".equals(method)
                                    && validateGetEndpoints
                                    && !endpointInspector.isValidGetEndpoint(uri)) {
                                log.debug("Skipping invalid GET endpoint: {}", uri);
                                return false;
                            }
                            return !endpoint.isPresent() || endpoint.get().equals(uri);
                        })
                .forEach(
                        counter -> {
                            String session = counter.getId().getTag("session");
                            if (session != null) {
                                uniqueUsers.add(session);
                            }
                        });
        return uniqueUsers.size();
    }
