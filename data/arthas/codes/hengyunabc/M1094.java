    private void tryUpdateWelcomeMessage() {
        TunnelClient tunnelClient = ArthasBootstrap.getInstance().getTunnelClient();
        if (tunnelClient != null) {
            String id = tunnelClient.getId();
            if (id != null) {
                Map<String, String> welcomeInfos = new HashMap<String, String>();
                welcomeInfos.put("id", id);
                this.welcomeMessage = ArthasBanner.welcome(welcomeInfos);
            }
        }
    }
