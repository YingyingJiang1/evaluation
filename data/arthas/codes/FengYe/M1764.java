    private void handleGrpcRequest(Http2HeadersFrame headersFrame, ChannelHandlerContext ctx) {
        int id = headersFrame.stream().id();
        String path = headersFrame.headers().get(HEADER_PATH).toString();
        // 去掉前面的斜杠，然后按斜杠分割
        String[] parts = path.substring(1).split("/");
        GrpcRequest grpcRequest = new GrpcRequest(headersFrame.stream().id(), parts[0], parts[1]);
        grpcRequest.setHeaders(headersFrame.headers());
        GrpcDispatcher.checkGrpcType(grpcRequest);
        dataBuffer.put(id, grpcRequest);
        System.out.println("Received headers: " + headersFrame.headers());
    }
