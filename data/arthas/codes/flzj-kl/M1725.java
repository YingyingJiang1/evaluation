    public static NativeAgentProxyDiscoveryFactory getNativeAgentProxyDiscoveryFactory() {
        if (nativeAgentProxyDiscoveryFactory == null) {
            synchronized (NativeAgentProxyDiscoveryFactory.class) {
                if (nativeAgentProxyDiscoveryFactory == null) {
                    nativeAgentProxyDiscoveryFactory = new NativeAgentProxyDiscoveryFactory();
                }
            }
        }
        return nativeAgentProxyDiscoveryFactory;
    }
