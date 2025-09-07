    private boolean processCertificateData(String certData, LicenseContext context) {
        try {
            JSONObject licenseData = new JSONObject(certData);
            JSONObject metaObj = licenseData.optJSONObject("meta");
            if (metaObj != null) {
                String issuedStr = metaObj.optString("issued", null);
                String expiryStr = metaObj.optString("expiry", null);

                if (issuedStr != null && expiryStr != null) {
                    java.time.Instant issued = java.time.Instant.parse(issuedStr);
                    java.time.Instant expiry = java.time.Instant.parse(expiryStr);
                    java.time.Instant now = java.time.Instant.now();

                    if (issued.isAfter(now)) {
                        log.error(
                                "License file issued date is in the future. Please adjust system"
                                        + " time or request a new license");
                        return false;
                    }

                    // Check if the license file has expired
                    if (expiry.isBefore(now)) {
                        log.error("License file has expired on {}", expiryStr);
                        return false;
                    }

                    log.info("License file valid until {}", expiryStr);
                }
            }

            // Get the main license data
            JSONObject dataObj = licenseData.optJSONObject("data");
            if (dataObj == null) {
                log.error("No data object found in certificate");
                return false;
            }

            // Extract license or machine information
            JSONObject attributesObj = dataObj.optJSONObject("attributes");
            if (attributesObj != null) {
                log.info("Found attributes in certificate data");

                // Check for floating license
                context.isFloatingLicense = attributesObj.optBoolean("floating", false);
                context.maxMachines = attributesObj.optInt("maxMachines", 1);

                // Extract metadata
                JSONObject metadataObj = attributesObj.optJSONObject("metadata");
                if (metadataObj != null) {
                    int users = metadataObj.optInt("users", 1);
                    applicationProperties.getPremium().setMaxUsers(users);
                    log.info("License allows for {} users", users);
                    context.isEnterpriseLicense = metadataObj.optBoolean("isEnterprise", false);
                }

                // Check license status if available
                String status = attributesObj.optString("status", null);
                if (status != null
                        && !"ACTIVE".equals(status)
                        && !"EXPIRING".equals(status)) { // Accept "EXPIRING" status as valid
                    log.error("License status is not active: {}", status);
                    return false;
                }
            }

            return true;
        } catch (Exception e) {
            log.error("Error processing certificate data: {}", e.getMessage(), e);
            return false;
        }
    }
