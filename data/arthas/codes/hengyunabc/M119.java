    protected void fireCommandSent(String command, String message) {
        if (getCommandSupport().getListenerCount() > 0) {
            getCommandSupport().fireCommandSent(command, message);
        }
    }
