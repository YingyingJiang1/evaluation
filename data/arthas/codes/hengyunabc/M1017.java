    @Override
    public CLI cli() {
        if (shouldOverrideCli(clazz)) {
            try {
                return clazz.newInstance().cli();
            } catch (Exception ignore) {
                // Use cli instead
            }
        }
        return cli;
    }
