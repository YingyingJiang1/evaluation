    public GrpcResponse doUnaryExecute(String service, String method, byte[] arg) throws Throwable {
        MethodHandle methodHandle = grpcInvokeMap.get(generateGrpcMethodKey(service, method));
        MethodType type = grpcInvokeMap.get(generateGrpcMethodKey(service, method)).type();
        Object req = requestParseFromMap.get(generateGrpcMethodKey(service, method)).invoke(arg);
        Object execute = methodHandle.invoke(req);
        GrpcResponse grpcResponse = new GrpcResponse();
        grpcResponse.setClazz(type.returnType());
        grpcResponse.setService(service);
        grpcResponse.setMethod(method);
        grpcResponse.writeResponseData(execute);
        return grpcResponse;
    }
