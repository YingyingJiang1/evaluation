    @Override
    public ResultConsumer getConsumer(String consumerId) {
        for (int i = 0; i < consumers.size(); i++) {
            ResultConsumer consumer = consumers.get(i);
            if (consumer.getConsumerId().equals(consumerId)) {
                return consumer;
            }
        }
        return null;
    }
