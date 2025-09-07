    private static String findAppNameFromAgentId(String id) {
        int index = id.indexOf('_');
        if (index < 0 || index >= id.length()) {
            return null;
        }

        return id.substring(0, index);
    }
