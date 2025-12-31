    private boolean verifyJWTLicense(String licenseKey, LicenseContext context) {
        try {
            log.info("Verifying ED25519_SIGN format license key");

            // Remove the "key/" prefix
            String licenseData = licenseKey.substring(JWT_PREFIX.length());

            // Split into payload and signature
            String[] parts = licenseData.split("\\.", 2);
            if (parts.length != 2) {
                log.error(
                        "Invalid ED25519_SIGN license format. Expected format:"
                                + " key/payload.signature");
                return false;
            }

            String encodedPayload = parts[0];
            String encodedSignature = parts[1];

            // Verify signature
            boolean isSignatureValid = verifyJWTSignature(encodedPayload, encodedSignature);
            if (!isSignatureValid) {
                log.error("ED25519_SIGN license signature is invalid");
                return false;
            }

            log.info("ED25519_SIGN license signature is valid");

            // Decode and process payload - first convert from URL-safe base64 if needed
            String base64Payload = encodedPayload.replace('-', '+').replace('_', '/');
            byte[] payloadBytes = Base64.getDecoder().decode(base64Payload);
            String payload = new String(payloadBytes);

            // Process the license payload
            boolean isValid = processJWTLicensePayload(payload, context);

            return isValid;
        } catch (Exception e) {
            log.error("Error verifying ED25519_SIGN license: {}", e.getMessage());
            return false;
        }
    }
