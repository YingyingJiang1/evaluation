    public void startDemo(final boolean shouldLog) {
        startMetricsMonitor(shouldLog);
        while (true) {
            final HystrixRequestContext context = HystrixRequestContext.initializeContext();
            Observable<CreditCardAuthorizationResult> o = observeSimulatedUserRequestForOrderConfirmationAndCreditCardPayment();

            final CountDownLatch latch = new CountDownLatch(1);
            o.subscribe(new Subscriber<CreditCardAuthorizationResult>() {
                @Override
                public void onCompleted() {
                    latch.countDown();
                    context.shutdown();
                }

                @Override
                public void onError(Throwable e) {
                    e.printStackTrace();
                    latch.countDown();
                    context.shutdown();
                }

                @Override
                public void onNext(CreditCardAuthorizationResult creditCardAuthorizationResult) {
                    if (shouldLog) {
                        System.out.println("Request => " + HystrixRequestLog.getCurrentRequest().getExecutedCommandsAsString());
                    }
                }
            });

            try {
                latch.await(5000, TimeUnit.MILLISECONDS);
            } catch (InterruptedException ex) {
                System.out.println("INTERRUPTED!");
            }
        }
    }
