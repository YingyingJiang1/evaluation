        @Override
        public CommandProcess foregroundHandler(Handler<Void> handler) {
            synchronized (ProcessImpl.this) {
                foregroundHandler = handler;
            }
            return this;
        }
