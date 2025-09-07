        @Override
        public void onCompleted() {
            sendResponse.writeTrailer(Status.OK, null);
            latch.countDown();
        }
