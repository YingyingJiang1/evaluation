    void notifyInputListener() {
        TelnetInputListener listener;
        synchronized (this) {
            listener = this.inputListener;
        }
        if (listener != null) {
            listener.telnetInputAvailable();
        }
    }
