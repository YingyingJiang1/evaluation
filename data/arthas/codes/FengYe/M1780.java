            @Override
            public void onNext(GrpcResponse res) {
                // 控制流只能响应一次header
                if (!sendHeader.get()) {
                    sendHeader.compareAndSet(false, true);
                    context.writeAndFlush(new DefaultHttp2HeadersFrame(res.getEndHeader()).stream(frame.stream()));
                }
                context.writeAndFlush(new DefaultHttp2DataFrame(res.getResponseData()).stream(frame.stream()));
            }
