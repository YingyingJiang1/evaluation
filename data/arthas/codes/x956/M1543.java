        @Override
        public void onClose(Status s, Metadata t) {
            // TODO 这个函数会在 onCompleted 之前回调，这里有点奇怪
            if (!headersSent) {
                // seems, sometimes onHeaders() is not called before this method is called!
                // so far, they are the error cases. let onError() method in ClientListener
                // handle this call. Could ignore this.
                // TODO is this correct? what if onError() never gets called?
            } else {
                sendResponse.writeTrailer(s, t);
                latch.countDown();
            }
            super.onClose(s, t);
        }
