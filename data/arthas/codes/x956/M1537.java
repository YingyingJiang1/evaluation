    private io.grpc.stub.AbstractStub getRpcStub(Channel ch, Class cls, String stubName) {
        try {
            Method m = cls.getDeclaredMethod(stubName, io.grpc.Channel.class);
            return (io.grpc.stub.AbstractStub) m.invoke(null, ch);
        } catch (Exception e) {
            logger.warn("Error when fetching " + stubName + " for: " + cls.getName());
            throw new IllegalArgumentException(e);
        }
    }
