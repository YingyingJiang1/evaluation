        @Override
        public LatencyHistoryInfo build(Object data) {
            List<Object> commandData = (List<Object>) data;

            long timestamp = LONG.build(commandData.get(0));
            long latency = LONG.build(commandData.get(1));

            return new LatencyHistoryInfo(timestamp, latency);
        }
