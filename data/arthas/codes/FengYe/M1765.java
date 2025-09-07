    private void handleGrpcData(Http2DataFrame dataFrame, ChannelHandlerContext ctx) throws IOException {
        int streamId = dataFrame.stream().id();
        GrpcRequest grpcRequest = dataBuffer.get(streamId);
        ByteBuf content = dataFrame.content();
        grpcRequest.writeData(content);

        executorGroup.execute(() -> {
            try {
                grpcExecutorFactory.getExecutor(grpcRequest.getGrpcType()).execute(grpcRequest, dataFrame, ctx);
            } catch (Throwable e) {
                logger.error("handleGrpcData error", e);
                processError(ctx, e, dataFrame.stream());
            }
        });
    }
