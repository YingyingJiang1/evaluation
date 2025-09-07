    public void serverStreamExecute(GrpcRequest request, StreamObserver<GrpcResponse> responseObserver) throws Throwable {
        MethodHandle methodHandle = grpcInvokeMap.get(request.getGrpcMethodKey());
        Object req = requestParseFromMap.get(request.getGrpcMethodKey()).invoke(request.readData());
        methodHandle.invoke(req, responseObserver);
    }
