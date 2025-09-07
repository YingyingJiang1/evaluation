    public String findAvailableProxyAddress() {
        // Find in address register
        NativeAgentProxyDiscoveryFactory proxyDiscoveryFactory = NativeAgentProxyDiscoveryFactory.getNativeAgentProxyDiscoveryFactory();
        NativeAgentProxyDiscovery proxyDiscovery = proxyDiscoveryFactory.getNativeAgentProxyDiscovery(NativeAgentManagementWebBootstrap.registrationType);
        List<String> proxyList = proxyDiscovery.listNativeAgentProxy(NativeAgentManagementWebBootstrap.registrationAddress);
        if (proxyList == null || proxyList.size() == 0) {
            return null;
        }
        // Return a random index of proxy address, like 127.0.0.1:2233
        Random random = new Random();
        int randomIndex = random.nextInt(proxyList.size());
        return proxyList.get(randomIndex);
    }
