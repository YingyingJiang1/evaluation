        @Override
        public LatencyLatestInfo build(Object data) {
            List<Object> commandData = (List<Object>) data;

            String command = STRING.build(commandData.get(0));
            long timestamp = LONG.build(commandData.get(1));
            long lastEventLatency = LONG.build(commandData.get(2));
            long maxEventLatency = LONG.build(commandData.get(3));

            return new LatencyLatestInfo(command, timestamp, lastEventLatency, maxEventLatency);
        }
