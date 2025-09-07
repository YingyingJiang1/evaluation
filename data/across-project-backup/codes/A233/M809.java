    public boolean isGroupEnabled(String group) {
        // Rule 1: If group is explicitly disabled, it stays disabled
        if (disabledGroups.contains(group)) {
            log.debug("isGroupEnabled('{}') -> false (explicitly disabled)", group);
            return false;
        }

        Set<String> endpoints = endpointGroups.get(group);
        if (endpoints == null || endpoints.isEmpty()) {
            log.debug("isGroupEnabled('{}') -> false (no endpoints)", group);
            return false;
        }

        // Rule 2: For functional groups, check if all endpoints are enabled
        // Rule 3: For tool groups, they're enabled unless explicitly disabled (handled above)
        if (isToolGroup(group)) {
            log.debug("isGroupEnabled('{}') -> true (tool group not disabled)", group);
            return true;
        }

        // For functional groups, check each endpoint individually
        for (String endpoint : endpoints) {
            if (!isEndpointEnabledDirectly(endpoint)) {
                log.debug(
                        "isGroupEnabled('{}') -> false (endpoint '{}' disabled)", group, endpoint);
                return false;
            }
        }

        log.debug("isGroupEnabled('{}') -> true (all endpoints enabled)", group);
        return true;
    }
