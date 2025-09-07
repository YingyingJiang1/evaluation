        @Override
        public CommandProcess resumeHandler(Handler<Void> handler) {
            synchronized (ProcessImpl.this) {
                resumeHandler = handler;
            }
            return this;
        }
