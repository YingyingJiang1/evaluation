    public int getPortOrDefault() {
        if (this.port == null) {
            return DEFAULT_NATIVE_AGENT_PROXY_PORT;
        } else {
            return this.port;
        }
    }
