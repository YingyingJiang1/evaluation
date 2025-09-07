    private boolean verifyStandardLicense(String licenseKey, LicenseContext context) {
        try {
            log.info("Checking standard license key");
            String machineFingerprint = generateMachineFingerprint();

            // First, try to validate the license
            JsonNode validationResponse = validateLicense(licenseKey, machineFingerprint, context);
            if (validationResponse != null) {
                boolean isValid = validationResponse.path("meta").path("valid").asBoolean();
                String licenseId = validationResponse.path("data").path("id").asText();
                if (!isValid) {
                    String code = validationResponse.path("meta").path("code").asText();
                    log.info(code);
                    if ("NO_MACHINE".equals(code)
                            || "NO_MACHINES".equals(code)
                            || "FINGERPRINT_SCOPE_MISMATCH".equals(code)) {
                        log.info(
                                "License not activated for this machine. Attempting to"
                                        + " activate...");
                        boolean activated =
                                activateMachine(licenseKey, licenseId, machineFingerprint, context);
                        if (activated) {
                            // Revalidate after activation
                            validationResponse =
                                    validateLicense(licenseKey, machineFingerprint, context);
                            isValid =
                                    validationResponse != null
                                            && validationResponse
                                                    .path("meta")
                                                    .path("valid")
                                                    .asBoolean();
                        }
                    }
                }
                return isValid;
            }

            return false;
        } catch (Exception e) {
            log.error("Error verifying standard license: {}", e.getMessage());
            return false;
        }
    }
