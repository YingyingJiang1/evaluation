        @Override
        public CommandProcess stdinHandler(Handler<String> handler) {
            stdinHandler = handler;
            if (processForeground && stdinHandler != null) {
                tty.stdinHandler(stdinHandler);
            }
            return this;
        }
