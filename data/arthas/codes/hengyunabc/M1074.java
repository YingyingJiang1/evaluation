        @Override
        public CommandProcess resizehandler(Handler<Void> handler) {
            resizeHandler = handler;
            tty.resizehandler(resizeHandler);
            return this;
        }
