    public void pause(long mills) {
        try {
            Thread.sleep(mills);
        } catch (InterruptedException e) {
            // ignore
        }
    }
