    public int getWsPortOrDefault() {
        if (this.wsPort == null) {
            return DEFAULT_WS_PORT;
        } else {
            return this.httpPort;
        }
    }
