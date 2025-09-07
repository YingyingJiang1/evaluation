    private boolean matchesPathSegments(String uri) {
        for (String pattern : validGetEndpoints) {
            if (!pattern.contains("*") && !pattern.contains("{")) {
                String[] patternSegments = pattern.split("/");
                String[] uriSegments = uri.split("/");

                if (uriSegments.length < patternSegments.length) {
                    continue;
                }

                boolean match = true;
                for (int i = 0; i < patternSegments.length; i++) {
                    if (!patternSegments[i].equals(uriSegments[i])) {
                        match = false;
                        break;
                    }
                }

                if (match) {
                    return true;
                }
            }
        }
        return false;
    }
