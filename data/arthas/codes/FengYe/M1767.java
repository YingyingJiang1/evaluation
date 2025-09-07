    private void processError(ChannelHandlerContext ctx, Throwable e, Http2FrameStream stream) {
        GrpcResponse response = new GrpcResponse();
        ArthasGrpc.ErrorRes.Builder builder = ArthasGrpc.ErrorRes.newBuilder();
        ArthasGrpc.ErrorRes errorRes = builder.setErrorMsg(Optional.ofNullable(e.getMessage()).orElse("")).build();
        response.setClazz(ArthasGrpc.ErrorRes.class);
        response.writeResponseData(errorRes);
        ctx.writeAndFlush(new DefaultHttp2HeadersFrame(response.getEndHeader()).stream(stream));
        ctx.writeAndFlush(new DefaultHttp2DataFrame(response.getResponseData()).stream(stream));
        ctx.writeAndFlush(new DefaultHttp2HeadersFrame(response.getEndStreamHeader(), true).stream(stream));
    }
