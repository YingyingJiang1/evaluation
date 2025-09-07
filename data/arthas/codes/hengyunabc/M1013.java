        @Override
        public void complete(final Completion completion) {
            if (completeHandler != null) {
                try {
                    completeHandler.handle(completion);
                } catch (Throwable t) {
                    completion.complete(Collections.<String>emptyList());
                }
            } else {
                super.complete(completion);
            }
        }
