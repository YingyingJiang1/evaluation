        @Override
        public void appendResult(ResultModel result) {
            if (processStatus != ExecStatus.RUNNING) {
                throw new IllegalStateException(
                        "Cannot write to standard output when " + status().name().toLowerCase());
            }
            ProcessImpl.this.appendResult(result);
        }
