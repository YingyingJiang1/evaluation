    protected void fireReplyReceived(int replyCode, String reply) {
        if (getCommandSupport().getListenerCount() > 0) {
            getCommandSupport().fireReplyReceived(replyCode, reply);
        }
    }
