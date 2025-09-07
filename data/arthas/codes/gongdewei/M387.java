    @Override
    public void addConsumer(ResultConsumer consumer) {
        int consumerNo = consumerNumGenerator.incrementAndGet();
        String consumerId = UUID.randomUUID().toString().replaceAll("-", "") + "_" + consumerNo;
        consumer.setConsumerId(consumerId);

        //将队列中的消息复制给新的消费者
        sharingResultConsumer.copyTo(consumer);

        consumers.add(consumer);
    }
