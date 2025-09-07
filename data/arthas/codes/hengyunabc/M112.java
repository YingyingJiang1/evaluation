    private void closeQuietly(Closeable close){
        if (close != null){
            try {
                close.close();
            } catch (IOException e) {
                // Ignored
            }
        }
    }
