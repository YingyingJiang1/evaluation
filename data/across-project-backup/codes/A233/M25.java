    private boolean verifyEd25519Signature(String encryptedData, String encodedSignature) {
        try {
            log.info("Signature to verify: {}", encodedSignature);

            byte[] signatureBytes = Base64.getDecoder().decode(encodedSignature);

            // Create the signing data format - prefix with "license/"
            String signingData = String.format("license/%s", encryptedData);
            byte[] signingDataBytes = signingData.getBytes();

            log.info("Signing data length: {} bytes", signingDataBytes.length);

            byte[] publicKeyBytes = Hex.decode(PUBLIC_KEY);

            Ed25519PublicKeyParameters verifierParams =
                    new Ed25519PublicKeyParameters(publicKeyBytes, 0);
            Ed25519Signer verifier = new Ed25519Signer();

            verifier.init(false, verifierParams);
            verifier.update(signingDataBytes, 0, signingDataBytes.length);

            // Verify the signature
            boolean result = verifier.verifySignature(signatureBytes);
            if (!result) {
                log.error("Signature verification failed with standard public key");
            }

            return result;
        } catch (Exception e) {
            log.error("Error verifying Ed25519 signature: {}", e.getMessage(), e);
            return false;
        }
    }
