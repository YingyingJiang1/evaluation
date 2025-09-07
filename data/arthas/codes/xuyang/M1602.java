        public void registerAdviceListener(String className, String methodName, String methodDesc,
                AdviceListener listener) {
            synchronized (this) {
                className = className.replace('/', '.');
                String key = key(className, methodName, methodDesc);

                List<AdviceListener> listeners = map.get(key);
                if (listeners == null) {
                    listeners = new ArrayList<AdviceListener>();
                    map.put(key, listeners);
                }
                if (!listeners.contains(listener)) {
                    listeners.add(listener);
                }
            }
        }
