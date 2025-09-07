    @Override
    public void execute(GrpcRequest request, Http2DataFrame frame, ChannelHandlerContext context) throws Throwable {
        // 一元调用，等到 endStream 再响应
        if (frame.isEndStream()) {
            GrpcResponse response = dispatcher.unaryExecute(request);
            context.writeAndFlush(new DefaultHttp2HeadersFrame(response.getEndHeader()).stream(frame.stream()));
            context.writeAndFlush(new DefaultHttp2DataFrame(response.getResponseData()).stream(frame.stream()));
            context.writeAndFlush(new DefaultHttp2HeadersFrame(response.getEndStreamHeader(), true).stream(frame.stream()));
        }
    }
