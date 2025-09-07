    public boolean isEndpointEnabled(String endpoint) {
        String original = endpoint;
        if (endpoint.startsWith("/")) {
            endpoint = endpoint.substring(1);
        }

        // Rule 1: Explicit flag wins - if disabled via disableEndpoint(), stay disabled
        Boolean explicitStatus = endpointStatuses.get(endpoint);
        if (Boolean.FALSE.equals(explicitStatus)) {
            log.debug("isEndpointEnabled('{}') -> false (explicitly disabled)", original);
            return false;
        }

        // Rule 2: Functional-group override - check if endpoint belongs to any disabled functional
        // group
        for (String group : endpointGroups.keySet()) {
            if (disabledGroups.contains(group) && endpointGroups.get(group).contains(endpoint)) {
                // Skip tool groups (qpdf, OCRmyPDF, Ghostscript, LibreOffice, etc.)
                if (!isToolGroup(group)) {
                    log.debug(
                            "isEndpointEnabled('{}') -> false (functional group '{}' disabled)",
                            original,
                            group);
                    return false;
                }
            }
        }

        // Rule 3: Tool-group fallback - check if at least one alternative tool group is enabled
        Set<String> alternatives = endpointAlternatives.get(endpoint);
        if (alternatives != null && !alternatives.isEmpty()) {
            boolean hasEnabledToolGroup =
                    alternatives.stream()
                            .anyMatch(toolGroup -> !disabledGroups.contains(toolGroup));
            log.debug(
                    "isEndpointEnabled('{}') -> {} (tool groups check)",
                    original,
                    hasEnabledToolGroup);
            return hasEnabledToolGroup;
        }

        // Rule 4: Single-dependency check - if no alternatives defined, check if endpoint belongs
        // to any disabled tool groups
        for (String group : endpointGroups.keySet()) {
            if (isToolGroup(group)
                    && disabledGroups.contains(group)
                    && endpointGroups.get(group).contains(endpoint)) {
                log.debug(
                        "isEndpointEnabled('{}') -> false (single tool group '{}' disabled, no alternatives)",
                        original,
                        group);
                return false;
            }
        }

        // Default: enabled if not explicitly disabled
        boolean enabled = !Boolean.FALSE.equals(explicitStatus);
        log.debug("isEndpointEnabled('{}') -> {} (default)", original, enabled);
        return enabled;
    }
