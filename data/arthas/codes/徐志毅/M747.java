        @Override
        public void handle(Void event) {
            timer.cancel();
            super.handle(event);
        }
