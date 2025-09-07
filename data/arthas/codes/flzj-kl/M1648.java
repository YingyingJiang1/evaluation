    private void loadNativeAgentDiscovery2Map(Map<String, String> registrationConfigMap) {
        for (Map.Entry<String, String> entry : registrationConfigMap.entrySet()) {
            String name = entry.getKey();
            String classPath = entry.getValue();

            try {
                Class<?> clazz = Class.forName(classPath);
                Constructor<?> constructor = clazz.getConstructor();
                NativeAgentDiscovery instance = (NativeAgentDiscovery) constructor.newInstance();
                nativeAgentDiscoveryMap.put(name, instance);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }
    }
