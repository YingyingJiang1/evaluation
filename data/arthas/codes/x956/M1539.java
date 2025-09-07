        @Override
        public void onError(Throwable t) {
            Status s = Status.fromThrowable(t);
            sendResponse.writeError(s);
            latch.countDown();
        }
