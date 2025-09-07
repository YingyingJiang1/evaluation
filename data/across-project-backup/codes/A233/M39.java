    private void checkLicense() {
        if (!applicationProperties.getPremium().isEnabled()) {
            premiumEnabledResult = License.NORMAL;
        } else {
            String licenseKey = getLicenseKeyContent(applicationProperties.getPremium().getKey());
            if (licenseKey != null) {
                premiumEnabledResult = licenseService.verifyLicense(licenseKey);
                if (License.ENTERPRISE == premiumEnabledResult) {
                    log.info("License key is Enterprise.");
                } else if (License.PRO == premiumEnabledResult) {
                    log.info("License key is Pro.");
                } else {
                    log.info("License key is invalid, defaulting to non pro license.");
                }
            } else {
                log.error("Failed to obtain license key content.");
                premiumEnabledResult = License.NORMAL;
            }
        }
    }
