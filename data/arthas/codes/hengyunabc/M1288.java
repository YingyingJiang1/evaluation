        public List<AdviceListener> queryAdviceListeners(String className, String methodName, String methodDesc) {
            className = className.replace('/', '.');
            String key = key(className, methodName, methodDesc);

            List<AdviceListener> listeners = map.get(key);

            return listeners;
        }
