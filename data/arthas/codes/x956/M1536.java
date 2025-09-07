    private Class<?> getClassObject(String className) {
        Class rpcClass = null;
        try {
            rpcClass = Class.forName(className + "Grpc");
        } catch (ClassNotFoundException e) {
            logger.info("no such class " + className);
        }
        return rpcClass;
    }
