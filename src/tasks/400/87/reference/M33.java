    private JsonNode fetchMachinesForLicense(String licenseKey, String licenseId) throws Exception {
        HttpRequest request =
                HttpRequest.newBuilder()
                        .uri(
                                URI.create(
                                        BASE_URL
                                                + "/"
                                                + ACCOUNT_ID
                                                + "/licenses/"
                                                + licenseId
                                                + "/machines"))
                        .header("Content-Type", "application/vnd.api+json")
                        .header("Accept", "application/vnd.api+json")
                        .header("Authorization", "License " + licenseKey)
                        .GET()
                        .build();

        HttpResponse<String> response =
                httpClient.send(request, HttpResponse.BodyHandlers.ofString());
        log.info("fetchMachinesForLicense Response body: {}", response.body());

        if (response.statusCode() == 200) {
            return objectMapper.readTree(response.body());
        } else {
            log.error(
                    "Error fetching machines for license. Status code: {}, error: {}",
                    response.statusCode(),
                    response.body());
            return null;
        }
    }
