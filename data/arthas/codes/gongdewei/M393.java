    @Override
    public void close() {
        for (ResultDistributor distributor : distributors) {
            distributor.close();
        }
    }
