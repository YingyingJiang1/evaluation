            @Override
            public void onNext(GrpcRequest<ArthasUnittest.ArthasUnittestRequest> req) {
                try {
                    byte[] bytes = req.readData();
                    while (bytes != null && bytes.length != 0) {
                        GrpcResponse<ArthasUnittest.ArthasUnittestResponse> grpcResponse = new GrpcResponse<>();
                        grpcResponse.setService("arthas.grpc.unittest.ArthasUnittestService");
                        grpcResponse.setMethod("biStream");
                        grpcResponse.writeResponseData(ArthasUnittest.ArthasUnittestResponse.parseFrom(bytes));
                        observer.onNext(grpcResponse);
                        bytes = req.readData();
                    }
                } catch (InvalidProtocolBufferException e) {
                    throw new RuntimeException(e);
                }
            }
