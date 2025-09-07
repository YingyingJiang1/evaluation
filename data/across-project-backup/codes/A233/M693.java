    private List<EndpointCount> getEndpointCounts(String method) {
        Map<String, Double> counts = new HashMap<>();
        meterRegistry
                .find("http.requests")
                .tag("method", method)
                .counters()
                .forEach(
                        counter -> {
                            String uri = counter.getId().getTag("uri");

                            // Skip if uri is null
                            if (uri == null) {
                                return;
                            }

                            // For POST requests, only include if they start with /api/v1
                            if ("POST".equals(method) && !uri.contains("api/v1")) {
                                return;
                            }

                            if (uri.contains(".txt")) {
                                return;
                            }

                            // For GET requests, validate if we have a list of valid endpoints
                            final boolean validateGetEndpoints =
                                    endpointInspector.getValidGetEndpoints().size() != 0;
                            if ("GET".equals(method)
                                    && validateGetEndpoints
                                    && !endpointInspector.isValidGetEndpoint(uri)) {
                                log.debug("Skipping invalid GET endpoint: {}", uri);
                                return;
                            }

                            counts.merge(uri, counter.count(), Double::sum);
                        });

        return counts.entrySet().stream()
                .map(entry -> new EndpointCount(entry.getKey(), entry.getValue()))
                .sorted(Comparator.comparing(EndpointCount::getCount).reversed())
                .toList();
    }
