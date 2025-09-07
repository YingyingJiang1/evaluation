    public void writeResponseData(Object response) {
        byte[] encode = null;
        try {
            if (ArthasGrpc.ErrorRes.class.equals(clazz)) {
                encode = ((ArthasGrpc.ErrorRes) response).toByteArray();
            } else {
                encode = (byte[]) GrpcDispatcher.responseToByteArrayMap.get(GrpcDispatcher.generateGrpcMethodKey(service, method)).invoke(response);
            }
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
        this.byteData = ByteUtil.newByteBuf();
        this.byteData.writeBoolean(false);
        this.byteData.writeInt(encode.length);
        this.byteData.writeBytes(encode);
    }
