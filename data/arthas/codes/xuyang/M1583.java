        @Override
        public void run() {
            try {
                watchRequestModel.enhance(arthasStreamObserver);
            } catch (Throwable t) {
                logger.error("Error during processing the command:", t);
                arthasStreamObserver.end(-1, "Error during processing the command: " + t.getClass().getName() + ", message:" + t.getMessage()
                        + ", please check $HOME/logs/arthas/arthas.log for more details." );
            }
        }
