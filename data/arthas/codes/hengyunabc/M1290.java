        public List<AdviceListener> queryTraceAdviceListeners(String className, String owner, String methodName,
                String methodDesc) {
            className = className.replace('/', '.');
            String key = keyForTrace(className, owner, methodName, methodDesc);

            List<AdviceListener> listeners = map.get(key);

            return listeners;
        }
