        @Override
        public CommandProcess backgroundHandler(Handler<Void> handler) {
            synchronized (ProcessImpl.this) {
                backgroundHandler = handler;
            }
            return this;
        }
