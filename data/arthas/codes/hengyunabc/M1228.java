    @Override
    public String[] getPropertyNames() {
        synchronized (this.source) {
            return super.getPropertyNames();
        }
    }
