    public void update(long value) {
        if (previous == null) {
            previous = value;
            return;
        }
        rateCounter.update(value - previous);
        previous = value;
    }
