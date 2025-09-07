    private boolean deregisterMachine(String licenseKey, String machineId) {
        try {
            HttpRequest request =
                    HttpRequest.newBuilder()
                            .uri(URI.create(BASE_URL + "/" + ACCOUNT_ID + "/machines/" + machineId))
                            .header("Content-Type", "application/vnd.api+json")
                            .header("Accept", "application/vnd.api+json")
                            .header("Authorization", "License " + licenseKey)
                            .DELETE()
                            .build();

            HttpResponse<String> response =
                    httpClient.send(request, HttpResponse.BodyHandlers.ofString());

            if (response.statusCode() == 204) {
                log.info("Machine {} successfully deregistered", machineId);
                return true;
            } else {
                log.error(
                        "Error deregistering machine. Status code: {}, error: {}",
                        response.statusCode(),
                        response.body());
                return false;
            }
        } catch (Exception e) {
            log.error("Exception during machine deregistration: {}", e.getMessage(), e);
            return false;
        }
    }
