    @Override
    public void removeConsumer(ResultConsumer consumer) {
        consumers.remove(consumer);
        consumer.close();
    }
