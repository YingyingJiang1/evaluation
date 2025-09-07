    private Method getRpcMethod(Object stub, String rpcMethodName) {
        for (Method m : stub.getClass().getMethods()) {
            if (m.getName().equals(rpcMethodName)) {
                return m;
            }
        }
        throw new IllegalArgumentException("Couldn't find rpcmethod: " + rpcMethodName);
    }
