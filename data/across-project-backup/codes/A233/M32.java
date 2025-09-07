    private boolean activateMachine(
            String licenseKey, String licenseId, String machineFingerprint, LicenseContext context)
            throws Exception {
        // For floating licenses, we first need to check if we need to deregister any machines
        if (context.isFloatingLicense) {
            log.info(
                    "Processing floating license activation. Max machines allowed: {}",
                    context.maxMachines);

            // Get the current machines for this license
            JsonNode machinesResponse = fetchMachinesForLicense(licenseKey, licenseId);
            if (machinesResponse != null) {
                JsonNode machines = machinesResponse.path("data");
                int currentMachines = machines.size();

                log.info(
                        "Current machine count: {}, Max allowed: {}",
                        currentMachines,
                        context.maxMachines);

                // Check if the current fingerprint is already activated
                boolean isCurrentMachineActivated = false;
                String currentMachineId = null;

                for (JsonNode machine : machines) {
                    if (machineFingerprint.equals(
                            machine.path("attributes").path("fingerprint").asText())) {
                        isCurrentMachineActivated = true;
                        currentMachineId = machine.path("id").asText();
                        log.info(
                                "Current machine is already activated with ID: {}",
                                currentMachineId);
                        break;
                    }
                }

                // If the current machine is already activated, there's no need to do anything
                if (isCurrentMachineActivated) {
                    log.info("Machine already activated. No action needed.");
                    return true;
                }

                // If we've reached the max machines limit, we need to deregister the oldest machine
                if (currentMachines >= context.maxMachines) {
                    log.info(
                            "Max machines reached. Deregistering oldest machine to make room for the new machine.");

                    // Find the oldest machine based on creation timestamp
                    if (machines.size() > 0) {
                        // Find the machine with the oldest creation date
                        String oldestMachineId = null;
                        java.time.Instant oldestTime = null;

                        for (JsonNode machine : machines) {
                            String createdStr =
                                    machine.path("attributes").path("created").asText(null);
                            if (createdStr != null && !createdStr.isEmpty()) {
                                try {
                                    java.time.Instant createdTime =
                                            java.time.Instant.parse(createdStr);
                                    if (oldestTime == null || createdTime.isBefore(oldestTime)) {
                                        oldestTime = createdTime;
                                        oldestMachineId = machine.path("id").asText();
                                    }
                                } catch (Exception e) {
                                    log.warn(
                                            "Could not parse creation time for machine: {}",
                                            e.getMessage());
                                }
                            }
                        }

                        // If we couldn't determine the oldest by timestamp, use the first one
                        if (oldestMachineId == null) {
                            log.warn(
                                    "Could not determine oldest machine by timestamp, using first machine in list");
                            oldestMachineId = machines.path(0).path("id").asText();
                        }

                        log.info("Deregistering machine with ID: {}", oldestMachineId);

                        boolean deregistered = deregisterMachine(licenseKey, oldestMachineId);
                        if (!deregistered) {
                            log.error(
                                    "Failed to deregister machine. Cannot proceed with activation.");
                            return false;
                        }
                        log.info(
                                "Machine deregistered successfully. Proceeding with activation of new machine.");
                    } else {
                        log.error(
                                "License has reached machine limit but no machines were found to deregister. This is unexpected.");
                        // We'll still try to activate, but it might fail
                    }
                }
            }
        }

        // Proceed with machine activation
        String hostname;
        try {
            hostname = java.net.InetAddress.getLocalHost().getHostName();
        } catch (Exception e) {
            hostname = "Unknown";
        }

        JSONObject body =
                new JSONObject()
                        .put(
                                "data",
                                new JSONObject()
                                        .put("type", "machines")
                                        .put(
                                                "attributes",
                                                new JSONObject()
                                                        .put("fingerprint", machineFingerprint)
                                                        .put(
                                                                "platform",
                                                                System.getProperty("os.name"))
                                                        .put("name", hostname))
                                        .put(
                                                "relationships",
                                                new JSONObject()
                                                        .put(
                                                                "license",
                                                                new JSONObject()
                                                                        .put(
                                                                                "data",
                                                                                new JSONObject()
                                                                                        .put(
                                                                                                "type",
                                                                                                "licenses")
                                                                                        .put(
                                                                                                "id",
                                                                                                licenseId)))));

        HttpRequest request =
                HttpRequest.newBuilder()
                        .uri(URI.create(BASE_URL + "/" + ACCOUNT_ID + "/machines"))
                        .header("Content-Type", "application/vnd.api+json")
                        .header("Accept", "application/vnd.api+json")
                        .header("Authorization", "License " + licenseKey)
                        .POST(HttpRequest.BodyPublishers.ofString(body.toString()))
                        .build();

        HttpResponse<String> response =
                httpClient.send(request, HttpResponse.BodyHandlers.ofString());
        log.info("activateMachine Response body: " + response.body());
        if (response.statusCode() == 201) {
            log.info("Machine activated successfully");
            return true;
        } else {
            log.error(
                    "Error activating machine. Status code: {}, error: {}",
                    response.statusCode(),
                    response.body());

            return false;
        }
    }
