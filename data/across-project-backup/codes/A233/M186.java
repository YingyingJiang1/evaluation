    public Map<String, Object> captureApplicationProperties() {
        Map<String, Object> properties = new HashMap<>();

        // Capture Legal properties
        addIfNotEmpty(
                properties,
                "legal_termsAndConditions",
                applicationProperties.getLegal().getTermsAndConditions());
        addIfNotEmpty(
                properties,
                "legal_privacyPolicy",
                applicationProperties.getLegal().getPrivacyPolicy());
        addIfNotEmpty(
                properties,
                "legal_accessibilityStatement",
                applicationProperties.getLegal().getAccessibilityStatement());
        addIfNotEmpty(
                properties,
                "legal_cookiePolicy",
                applicationProperties.getLegal().getCookiePolicy());
        addIfNotEmpty(
                properties, "legal_impressum", applicationProperties.getLegal().getImpressum());

        // Capture Security properties
        addIfNotEmpty(
                properties,
                "security_enableLogin",
                applicationProperties.getSecurity().getEnableLogin());
        addIfNotEmpty(
                properties,
                "security_csrfDisabled",
                applicationProperties.getSecurity().getCsrfDisabled());
        addIfNotEmpty(
                properties,
                "security_loginAttemptCount",
                applicationProperties.getSecurity().getLoginAttemptCount());
        addIfNotEmpty(
                properties,
                "security_loginResetTimeMinutes",
                applicationProperties.getSecurity().getLoginResetTimeMinutes());
        addIfNotEmpty(
                properties,
                "security_loginMethod",
                applicationProperties.getSecurity().getLoginMethod());

        // Capture OAuth2 properties (excluding sensitive information)
        addIfNotEmpty(
                properties,
                "security_oauth2_enabled",
                applicationProperties.getSecurity().getOauth2().getEnabled());
        if (applicationProperties.getSecurity().getOauth2().getEnabled()) {
            addIfNotEmpty(
                    properties,
                    "security_oauth2_autoCreateUser",
                    applicationProperties.getSecurity().getOauth2().getAutoCreateUser());
            addIfNotEmpty(
                    properties,
                    "security_oauth2_blockRegistration",
                    applicationProperties.getSecurity().getOauth2().getBlockRegistration());
            addIfNotEmpty(
                    properties,
                    "security_oauth2_useAsUsername",
                    applicationProperties.getSecurity().getOauth2().getUseAsUsername());
            addIfNotEmpty(
                    properties,
                    "security_oauth2_provider",
                    applicationProperties.getSecurity().getOauth2().getProvider());
        }
        // Capture System properties
        addIfNotEmpty(
                properties,
                "system_defaultLocale",
                applicationProperties.getSystem().getDefaultLocale());
        addIfNotEmpty(
                properties,
                "system_googlevisibility",
                applicationProperties.getSystem().getGooglevisibility());
        addIfNotEmpty(
                properties, "system_showUpdate", applicationProperties.getSystem().isShowUpdate());
        addIfNotEmpty(
                properties,
                "system_showUpdateOnlyAdmin",
                applicationProperties.getSystem().getShowUpdateOnlyAdmin());
        addIfNotEmpty(
                properties,
                "system_customHTMLFiles",
                applicationProperties.getSystem().isCustomHTMLFiles());
        addIfNotEmpty(
                properties,
                "system_tessdataDir",
                applicationProperties.getSystem().getTessdataDir());
        addIfNotEmpty(
                properties,
                "system_enableAlphaFunctionality",
                applicationProperties.getSystem().getEnableAlphaFunctionality());
        addIfNotEmpty(
                properties,
                "system_enableAnalytics",
                applicationProperties.getSystem().isAnalyticsEnabled());

        // Capture UI properties
        addIfNotEmpty(properties, "ui_appName", applicationProperties.getUi().getAppName());
        addIfNotEmpty(
                properties,
                "ui_homeDescription",
                applicationProperties.getUi().getHomeDescription());
        addIfNotEmpty(
                properties, "ui_appNameNavbar", applicationProperties.getUi().getAppNameNavbar());

        // Capture Metrics properties
        addIfNotEmpty(
                properties, "metrics_enabled", applicationProperties.getMetrics().getEnabled());

        // Capture EnterpriseEdition properties
        addIfNotEmpty(
                properties,
                "enterpriseEdition_enabled",
                applicationProperties.getPremium().isEnabled());
        if (applicationProperties.getPremium().isEnabled()) {
            addIfNotEmpty(
                    properties,
                    "enterpriseEdition_customMetadata_autoUpdateMetadata",
                    applicationProperties
                            .getPremium()
                            .getProFeatures()
                            .getCustomMetadata()
                            .isAutoUpdateMetadata());
            addIfNotEmpty(
                    properties,
                    "enterpriseEdition_customMetadata_author",
                    applicationProperties
                            .getPremium()
                            .getProFeatures()
                            .getCustomMetadata()
                            .getAuthor());
            addIfNotEmpty(
                    properties,
                    "enterpriseEdition_customMetadata_creator",
                    applicationProperties
                            .getPremium()
                            .getProFeatures()
                            .getCustomMetadata()
                            .getCreator());
            addIfNotEmpty(
                    properties,
                    "enterpriseEdition_customMetadata_producer",
                    applicationProperties
                            .getPremium()
                            .getProFeatures()
                            .getCustomMetadata()
                            .getProducer());
        }
        // Capture AutoPipeline properties
        addIfNotEmpty(
                properties,
                "autoPipeline_outputFolder",
                applicationProperties.getAutoPipeline().getOutputFolder());

        return properties;
    }
