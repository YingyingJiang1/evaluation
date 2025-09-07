        private void close() {
            if (statisticsHandler != null && flushHandlerChain != null) {
                String data = statisticsHandler.result();

                for (Function<String, String> function : flushHandlerChain) {
                    data = function.apply(data);
                    if (function instanceof StatisticsFunction) {
                        data = ((StatisticsFunction) function).result();
                    }
                }
            }

            if (stdoutHandlerChain != null) {
                for (Function<String, String> function : stdoutHandlerChain) {
                    if (function instanceof CloseFunction) {
                        ((CloseFunction) function).close();
                    }
                }
            }
        }
