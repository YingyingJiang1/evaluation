        @Override
        public CommandProcess endHandler(Handler<Void> handler) {
            synchronized (ProcessImpl.this) {
                endHandler = handler;
            }
            return this;
        }
