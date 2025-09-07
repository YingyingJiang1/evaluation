        @Override
        public CommandProcess write(String data) {
            synchronized (ProcessImpl.this) {
                if (processStatus != ExecStatus.RUNNING) {
                    throw new IllegalStateException(
                            "Cannot write to standard output when " + status().name().toLowerCase());
                }
            }
            processOutput.write(data);
            return this;
        }
