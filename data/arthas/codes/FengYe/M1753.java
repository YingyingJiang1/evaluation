    @Override
    @GrpcMethod(value = "serverStream", grpcType = GrpcInvokeTypeEnum.SERVER_STREAM)
    public void serverStream(ArthasUnittest.ArthasUnittestRequest request, StreamObserver<GrpcResponse<ArthasUnittest.ArthasUnittestResponse>> observer) {

        for (int i = 0; i < 5; i++) {
            ArthasUnittest.ArthasUnittestResponse response = ArthasUnittest.ArthasUnittestResponse.newBuilder()
                    .setMessage("Server response " + i + " to " + request.getMessage())
                    .build();
            GrpcResponse<ArthasUnittest.ArthasUnittestResponse> grpcResponse = new GrpcResponse<>();
            grpcResponse.setService("arthas.grpc.unittest.ArthasUnittestService");
            grpcResponse.setMethod("serverStream");
            grpcResponse.writeResponseData(response);
            observer.onNext(grpcResponse);
        }
        observer.onCompleted();
    }
