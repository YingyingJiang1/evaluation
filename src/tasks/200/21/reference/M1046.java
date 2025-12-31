    public static void closeSock(SocketChannel sock) {
        if (!sock.isOpen()) {
            return;
        }

        try {
            /*
             * The following sequence of code is stupid! You would think that
             * only sock.close() is needed, but alas, it doesn't work that way.
             * If you just do sock.close() there are cases where the socket
             * doesn't actually close...
             */
            sock.socket().shutdownOutput();
        } catch (IOException e) {
            // This is a relatively common exception that we can't avoid
            LOG.debug("ignoring exception during output shutdown", e);
        }
        try {
            sock.socket().shutdownInput();
        } catch (IOException e) {
            // This is a relatively common exception that we can't avoid
            LOG.debug("ignoring exception during input shutdown", e);
        }
        try {
            sock.socket().close();
        } catch (IOException e) {
            LOG.debug("ignoring exception during socket close", e);
        }
        try {
            sock.close();
        } catch (IOException e) {
            LOG.debug("ignoring exception during socketchannel close", e);
        }
    }
