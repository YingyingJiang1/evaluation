        @Override
        public CommandProcess interruptHandler(Handler<Void> handler) {
            synchronized (ProcessImpl.this) {
                interruptHandler = handler;
            }
            return this;
        }
