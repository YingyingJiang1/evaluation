    public static void updateArthasConfigMapDefaultValue(Map<String, String> arthasConfigMap) {
        if (!arthasConfigMap.containsKey("disabledCommands")) {
            arthasConfigMap.put("disabledCommands", DEFAULT_DISABLEDCOMMANDS);
        }
    }
