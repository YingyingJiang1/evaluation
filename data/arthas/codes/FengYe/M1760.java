    public static void checkGrpcType(GrpcRequest request) {
        request.setGrpcType(
                Optional.ofNullable(grpcInvokeTypeMap.get(generateGrpcMethodKey(request.getService(), request.getMethod())))
                        .orElse(GrpcInvokeTypeEnum.UNARY)
        );
        request.setStreamFirstData(true);
    }
