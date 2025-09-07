    private void processEnvironmentConfigs() {
        if (applicationProperties != null && applicationProperties.getEndpoints() != null) {
            List<String> endpointsToRemove = applicationProperties.getEndpoints().getToRemove();
            List<String> groupsToRemove = applicationProperties.getEndpoints().getGroupsToRemove();

            if (endpointsToRemove != null) {
                for (String endpoint : endpointsToRemove) {
                    disableEndpoint(endpoint.trim());
                }
            }

            if (groupsToRemove != null) {
                for (String group : groupsToRemove) {
                    disableGroup(group.trim());
                }
            }
        }
        if (!runningProOrHigher) {
            disableGroup("enterprise");
        }

        if (!applicationProperties.getSystem().getEnableUrlToPDF()) {
            disableEndpoint("url-to-pdf");
        }
    }
