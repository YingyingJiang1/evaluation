    public String getTargetIpOrDefault() {
        if (this.targetIp == null) {
            return DEFAULT_TARGET_IP;
        } else {
            return this.targetIp;
        }
    }
