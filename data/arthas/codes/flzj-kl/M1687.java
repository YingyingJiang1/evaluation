    public static NativeAgentRegistryFactory getNativeAgentClientRegisterFactory() {
        if (nativeAgentRegistryFactory == null) {
            synchronized (NativeAgentRegistryFactory.class) {
                if (nativeAgentRegistryFactory == null) {
                    nativeAgentRegistryFactory = new NativeAgentRegistryFactory();
                }
            }
        }
        return nativeAgentRegistryFactory;
    }
