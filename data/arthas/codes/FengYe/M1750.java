    @Override
    @GrpcMethod(value = "clientStreamSum", grpcType = GrpcInvokeTypeEnum.CLIENT_STREAM)
    public StreamObserver<GrpcRequest<ArthasUnittest.ArthasUnittestRequest>> clientStreamSum(StreamObserver<GrpcResponse<ArthasUnittest.ArthasUnittestResponse>> observer) {
        return new StreamObserver<GrpcRequest<ArthasUnittest.ArthasUnittestRequest>>() {
            AtomicInteger sum = new AtomicInteger(0);

            @Override
            public void onNext(GrpcRequest<ArthasUnittest.ArthasUnittestRequest> req) {
                try {
                    byte[] bytes = req.readData();
                    while (bytes != null && bytes.length != 0) {
                        ArthasUnittest.ArthasUnittestRequest request = ArthasUnittest.ArthasUnittestRequest.parseFrom(bytes);
                        sum.addAndGet(request.getNum());
                        bytes = req.readData();
                    }
                } catch (InvalidProtocolBufferException e) {
                    throw new RuntimeException(e);
                }
            }

            @Override
            public void onCompleted() {
                ArthasUnittest.ArthasUnittestResponse response = ArthasUnittest.ArthasUnittestResponse.newBuilder()
                        .setNum(sum.get())
                        .build();
                GrpcResponse<ArthasUnittest.ArthasUnittestResponse> grpcResponse = new GrpcResponse<>();
                grpcResponse.setService("arthas.grpc.unittest.ArthasUnittestService");
                grpcResponse.setMethod("clientStreamSum");
                grpcResponse.writeResponseData(response);
                observer.onNext(grpcResponse);
                observer.onCompleted();
            }
        };
    }
