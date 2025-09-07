    public static NativeAgentProxyRegistryFactory getNativeAgentProxyRegistryFactory() {
        if (nativeAgentProxyRegistryFactory == null) {
            synchronized (NativeAgentProxyRegistryFactory.class) {
                if (nativeAgentProxyRegistryFactory == null) {
                    nativeAgentProxyRegistryFactory = new NativeAgentProxyRegistryFactory();
                }
            }
        }
        return nativeAgentProxyRegistryFactory;
    }
