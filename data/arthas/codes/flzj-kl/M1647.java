    public static NativeAgentDiscoveryFactory getNativeAgentDiscoveryFactory() {
        if (nativeAgentDiscoveryFactory == null) {
            synchronized (NativeAgentDiscoveryFactory.class) {
                if (nativeAgentDiscoveryFactory == null) {
                    nativeAgentDiscoveryFactory = new NativeAgentDiscoveryFactory();
                }
            }
        }
        return nativeAgentDiscoveryFactory;
    }
