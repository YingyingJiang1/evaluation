    public void evictConsumers(Session session) {
        SharingResultDistributor distributor = session.getResultDistributor();
        if (distributor != null) {
            List<ResultConsumer> consumers = distributor.getConsumers();
            //remove inactive consumer from session directly
            long now = System.currentTimeMillis();
            for (ResultConsumer consumer : consumers) {
                long inactiveTime = now - consumer.getLastAccessTime();
                if (inactiveTime > consumerTimeoutMillis) {
                    //inactive duration must be large than pollTimeLimit
                    logger.info("Removing inactive consumer from session, sessionId: {}, consumerId: {}, inactive duration: {}",
                            session.getSessionId(), consumer.getConsumerId(), inactiveTime);
                    consumer.appendResult(new MessageModel("consumer is inactive for a while, please refresh the page."));
                    distributor.removeConsumer(consumer);
                }
            }
        }
    }
