    private void closeQuietly(Socket socket) {
        if (socket != null){
            try {
                socket.close();
            } catch (IOException e) {
                // Ignored
            }
        }
    }
