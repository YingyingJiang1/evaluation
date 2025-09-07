        public void registerTraceAdviceListener(String className, String owner, String methodName, String methodDesc,
                AdviceListener listener) {

            className = className.replace('/', '.');
            String key = keyForTrace(className, owner, methodName, methodDesc);

            List<AdviceListener> listeners = map.get(key);
            if (listeners == null) {
                listeners = new ArrayList<AdviceListener>();
                map.put(key, listeners);
            }
            if (!listeners.contains(listener)) {
                listeners.add(listener);
            }
        }
