    @Override
    public void close() {
        if (context != null) {
            context.close();
        }
    }
