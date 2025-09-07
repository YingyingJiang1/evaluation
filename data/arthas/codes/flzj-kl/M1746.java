    public int getPortOrDefault() {
        if (this.port == null) {
            return DEFAULT_NATIVE_AGENT_MANAGEMENT_WEB_PORT;
        } else {
            return this.port;
        }
    }
