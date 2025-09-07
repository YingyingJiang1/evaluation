    private boolean matchesWildcardOrPathVariable(String uri) {
        for (String pattern : validGetEndpoints) {
            if (pattern.contains("*") || pattern.contains("{")) {
                int wildcardIndex = pattern.indexOf('*');
                int variableIndex = pattern.indexOf('{');

                int cutoffIndex;
                if (wildcardIndex < 0) {
                    cutoffIndex = variableIndex;
                } else if (variableIndex < 0) {
                    cutoffIndex = wildcardIndex;
                } else {
                    cutoffIndex = Math.min(wildcardIndex, variableIndex);
                }

                String staticPrefix = pattern.substring(0, cutoffIndex);

                if (uri.startsWith(staticPrefix)) {
                    return true;
                }
            }
        }
        return false;
    }
