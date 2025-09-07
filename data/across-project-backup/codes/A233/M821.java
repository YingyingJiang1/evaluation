    private void discoverEndpoints() {
        try {
            Map<String, RequestMappingHandlerMapping> mappings =
                    applicationContext.getBeansOfType(RequestMappingHandlerMapping.class);

            for (Map.Entry<String, RequestMappingHandlerMapping> entry : mappings.entrySet()) {
                RequestMappingHandlerMapping mapping = entry.getValue();
                Map<RequestMappingInfo, HandlerMethod> handlerMethods = mapping.getHandlerMethods();

                for (Map.Entry<RequestMappingInfo, HandlerMethod> handlerEntry :
                        handlerMethods.entrySet()) {
                    RequestMappingInfo mappingInfo = handlerEntry.getKey();
                    HandlerMethod handlerMethod = handlerEntry.getValue();

                    boolean isGetHandler = false;
                    try {
                        Set<RequestMethod> methods = mappingInfo.getMethodsCondition().getMethods();
                        isGetHandler = methods.isEmpty() || methods.contains(RequestMethod.GET);
                    } catch (Exception e) {
                        isGetHandler = true;
                    }

                    if (isGetHandler) {
                        Set<String> patterns = extractPatternsUsingDirectPaths(mappingInfo);

                        if (patterns.isEmpty()) {
                            patterns = extractPatternsFromString(mappingInfo);
                        }

                        validGetEndpoints.addAll(patterns);
                    }
                }
            }

            if (validGetEndpoints.isEmpty()) {
                logger.warn("No endpoints discovered. Adding common endpoints as fallback.");
                validGetEndpoints.add("/");
                validGetEndpoints.add("/api/**");
                validGetEndpoints.add("/**");
            }
        } catch (Exception e) {
            logger.error("Error discovering endpoints", e);
        }
    }
