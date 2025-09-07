    public void loadGrpcService(String grpcServicePackageName) {
        List<Class<?>> classes = ReflectUtil.findClasses(Optional.ofNullable(grpcServicePackageName).orElse(DEFAULT_GRPC_SERVICE_PACKAGE_NAME));
        for (Class<?> clazz : classes) {
            if (clazz.isAnnotationPresent(GrpcService.class)) {
                try {
                    // 处理 service
                    GrpcService grpcService = clazz.getAnnotation(GrpcService.class);
                    Object instance = clazz.getDeclaredConstructor().newInstance();
                    // 处理 method
                    MethodHandles.Lookup lookup = MethodHandles.lookup();
                    Method[] declaredMethods = clazz.getDeclaredMethods();
                    for (Method method : declaredMethods) {
                        if (method.isAnnotationPresent(GrpcMethod.class)) {
                            GrpcMethod grpcMethod = method.getAnnotation(GrpcMethod.class);
                            MethodHandle grpcInvoke = lookup.unreflect(method);
                            String grpcMethodKey = generateGrpcMethodKey(grpcService.value(), grpcMethod.value());
                            grpcInvokeTypeMap.put(grpcMethodKey, grpcMethod.grpcType());
                            grpcInvokeMap.put(grpcMethodKey, grpcInvoke.bindTo(instance));


                            Class<?> requestClass = null;
                            Class<?> responseClass = null;
                            if (GrpcInvokeTypeEnum.UNARY.equals(grpcMethod.grpcType())) {
                                requestClass = grpcInvoke.type().parameterType(1);
                                responseClass = grpcInvoke.type().returnType();
                            } else if (GrpcInvokeTypeEnum.CLIENT_STREAM.equals(grpcMethod.grpcType()) || GrpcInvokeTypeEnum.BI_STREAM.equals(grpcMethod.grpcType())) {
                                responseClass = getInnerGenericClass(method.getGenericParameterTypes()[0]);
                                requestClass = getInnerGenericClass(method.getGenericReturnType());
                            } else if (GrpcInvokeTypeEnum.SERVER_STREAM.equals(grpcMethod.grpcType())) {
                                requestClass = getInnerGenericClass(method.getGenericParameterTypes()[0]);
                                responseClass = getInnerGenericClass(method.getGenericParameterTypes()[1]);
                            }
                            MethodHandle requestParseFrom = lookup.findStatic(requestClass, "parseFrom", MethodType.methodType(requestClass, byte[].class));
                            MethodHandle responseParseFrom = lookup.findStatic(responseClass, "parseFrom", MethodType.methodType(responseClass, byte[].class));
                            MethodHandle requestToByteArray = lookup.findVirtual(requestClass, "toByteArray", MethodType.methodType(byte[].class));
                            MethodHandle responseToByteArray = lookup.findVirtual(responseClass, "toByteArray", MethodType.methodType(byte[].class));
                            requestParseFromMap.put(grpcMethodKey, requestParseFrom);
                            responseParseFromMap.put(grpcMethodKey, responseParseFrom);
                            requestToByteArrayMap.put(grpcMethodKey, requestToByteArray);
                            responseToByteArrayMap.put(grpcMethodKey, responseToByteArray);


//                            switch (grpcMethod.grpcType()) {
//                                case UNARY:
//                                    unaryInvokeMap.put(grpcMethodKey, grpcInvoke.bindTo(instance));
//                                    return;
//                                case CLIENT_STREAM:
//                                    Object invoke = grpcInvoke.bindTo(instance).invoke();
//                                    if (!(invoke instanceof StreamObserver)) {
//                                        throw new RuntimeException(grpcMethodKey + " return class is not StreamObserver!");
//                                    }
//                                    clientStreamInvokeMap.put(grpcMethodKey, (StreamObserver) invoke);
//                                    return;
//                                case SERVER_STREAM:
//                                    return;
//                                case BI_STREAM:
//                                    return;
//                            }
                        }
                    }
                } catch (Throwable e) {
                    logger.error("GrpcDispatcher loadGrpcService error.", e);
                }
            }
        }
    }
