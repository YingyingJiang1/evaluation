        @Override
        public CommandProcess suspendHandler(Handler<Void> handler) {
            synchronized (ProcessImpl.this) {
                suspendHandler = handler;
            }
            return this;
        }
