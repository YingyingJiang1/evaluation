        @Override
        public void onHeaders(Metadata h) {
            sendResponse.writeHeaders(h);
            headersSent = true;
        }
