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
