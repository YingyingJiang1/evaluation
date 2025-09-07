    public void logDisabledEndpointsSummary() {
        // Get all unique endpoints across all groups
        Set<String> allEndpoints =
                endpointGroups.values().stream()
                        .flatMap(Set::stream)
                        .collect(java.util.stream.Collectors.toSet());

        // Check which endpoints are actually disabled (functionally unavailable)
        List<String> functionallyDisabledEndpoints =
                allEndpoints.stream()
                        .filter(endpoint -> !isEndpointEnabled(endpoint))
                        .sorted()
                        .toList();

        // Separate tool groups from functional groups
        List<String> disabledToolGroups =
                disabledGroups.stream().filter(this::isToolGroup).sorted().toList();

        List<String> disabledFunctionalGroups =
                disabledGroups.stream().filter(group -> !isToolGroup(group)).sorted().toList();

        if (!disabledToolGroups.isEmpty()) {
            log.info(
                    "Disabled tool groups: {} (endpoints may have alternative implementations)",
                    String.join(", ", disabledToolGroups));
        }

        if (!disabledFunctionalGroups.isEmpty()) {
            log.info("Disabled functional groups: {}", String.join(", ", disabledFunctionalGroups));
        }

        if (!functionallyDisabledEndpoints.isEmpty()) {
            log.info(
                    "Total disabled endpoints: {}. Disabled endpoints: {}",
                    functionallyDisabledEndpoints.size(),
                    String.join(", ", functionallyDisabledEndpoints));
        } else if (!disabledToolGroups.isEmpty()) {
            log.info(
                    "No endpoints disabled despite missing tools - fallback implementations available");
        }
    }
