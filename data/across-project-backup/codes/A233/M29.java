    private boolean processJWTLicensePayload(String payload, LicenseContext context) {
        try {
            log.info("Processing license payload: {}", payload);

            JSONObject licenseData = new JSONObject(payload);

            JSONObject licenseObj = licenseData.optJSONObject("license");
            if (licenseObj == null) {
                String id = licenseData.optString("id", null);
                if (id != null) {
                    log.info("Found license ID: {}", id);
                    licenseObj = licenseData; // Use the root object as the license object
                } else {
                    log.error("License data not found in payload");
                    return false;
                }
            }

            String licenseId = licenseObj.optString("id", "unknown");
            log.info("Processing license with ID: {}", licenseId);

            // Check for floating license in license object
            context.isFloatingLicense = licenseObj.optBoolean("floating", false);
            context.maxMachines = licenseObj.optInt("maxMachines", 1);
            if (context.isFloatingLicense) {
                log.info("Detected floating license with max machines: {}", context.maxMachines);
            }

            // Check expiry date
            String expiryStr = licenseObj.optString("expiry", null);
            if (expiryStr != null && !"null".equals(expiryStr)) {
                java.time.Instant expiry = java.time.Instant.parse(expiryStr);
                java.time.Instant now = java.time.Instant.now();

                if (now.isAfter(expiry)) {
                    log.error("License has expired on {}", expiryStr);
                    return false;
                }

                log.info("License valid until {}", expiryStr);
            } else {
                log.info("License has no expiration date");
            }

            // Extract account, product, policy info
            JSONObject accountObj = licenseData.optJSONObject("account");
            if (accountObj != null) {
                String accountId = accountObj.optString("id", "unknown");
                log.info("License belongs to account: {}", accountId);

                // Verify this matches your expected account ID
                if (!ACCOUNT_ID.equals(accountId)) {
                    log.warn("License account ID does not match expected account ID");
                    // You might want to fail verification here depending on your requirements
                }
            }

            // Extract policy information if available
            JSONObject policyObj = licenseData.optJSONObject("policy");
            if (policyObj != null) {
                String policyId = policyObj.optString("id", "unknown");
                log.info("License uses policy: {}", policyId);

                // Check for floating license in policy
                boolean policyFloating = policyObj.optBoolean("floating", false);
                int policyMaxMachines = policyObj.optInt("maxMachines", 1);

                // Policy settings take precedence
                if (policyFloating) {
                    context.isFloatingLicense = true;
                    context.maxMachines = policyMaxMachines;
                    log.info(
                            "Policy defines floating license with max machines: {}",
                            context.maxMachines);
                }

                // Extract max users and isEnterprise from policy or metadata
                int users = policyObj.optInt("users", 1);
                context.isEnterpriseLicense = policyObj.optBoolean("isEnterprise", false);

                if (users > 0) {
                    applicationProperties.getPremium().setMaxUsers(users);
                    log.info("License allows for {} users", users);
                } else {
                    // Try to get users from metadata if present
                    Object metadataObj = policyObj.opt("metadata");
                    if (metadataObj instanceof JSONObject metadata) {
                        users = metadata.optInt("users", 1);
                        applicationProperties.getPremium().setMaxUsers(users);
                        log.info("License allows for {} users (from metadata)", users);

                        // Check for isEnterprise flag in metadata
                        context.isEnterpriseLicense = metadata.optBoolean("isEnterprise", false);
                    } else {
                        // Default value
                        applicationProperties.getPremium().setMaxUsers(1);
                        log.info("Using default of 1 user for license");
                    }
                }
            }

            return true;
        } catch (Exception e) {
            log.error("Error processing license payload: {}", e.getMessage(), e);
            return false;
        }
    }
