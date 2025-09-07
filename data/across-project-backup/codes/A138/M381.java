    @Override
    public List<ResultModel> pollResults() {
        try {
            lastAccessTime = System.currentTimeMillis();
            long accessTime = lastAccessTime;
            if (lock.tryLock(500, TimeUnit.MILLISECONDS)) {
                polling = true;
                sendingItemCount = 0;
                long firstResultTime = 0;
                // sending delay: time elapsed after firstResultTime
                long sendingDelay = 0;
                // waiting time: time elapsed after access
                long waitingTime = 0;
                List<ResultModel> sendingResults = new ArrayList<ResultModel>(resultBatchSizeLimit);

                while (!closed
                        &&sendingResults.size() < resultBatchSizeLimit
                        && sendingDelay < 100
                        && waitingTime < pollTimeLimit) {
                    ResultModel aResult = resultQueue.poll(100, TimeUnit.MILLISECONDS);
                    if (aResult != null) {
                        sendingResults.add(aResult);
                        //是否为第一次获取到数据
                        if (firstResultTime == 0) {
                            firstResultTime = System.currentTimeMillis();
                        }
                        //判断是否需要立即发送出去
                        if (shouldFlush(sendingResults, aResult)) {
                            break;
                        }
                    } else {
                        if (firstResultTime > 0) {
                            //获取到部分数据后，队列已经取完，计算发送延时时间
                            sendingDelay = System.currentTimeMillis() - firstResultTime;
                        }
                        //计算总共等待时间，长轮询最大等待时间
                        waitingTime = System.currentTimeMillis() - accessTime;
                    }
                }

                //resultQueue.drainTo(sendingResults, resultSizeLimit-sendingResults.size());
                if(logger.isDebugEnabled()) {
                    logger.debug("pollResults: {}, results: {}", sendingResults.size(), JSON.toJSONString(sendingResults));
                }
                return sendingResults;
            }
        } catch (InterruptedException e) {
            //e.printStackTrace();
        } finally {
            if (lock.isHeldByCurrentThread()) {
                lastAccessTime = System.currentTimeMillis();
                polling = false;
                lock.unlock();
            }
        }
        return Collections.emptyList();
    }
