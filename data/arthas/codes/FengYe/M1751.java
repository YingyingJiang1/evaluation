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
