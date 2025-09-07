    private static void sendNotFound(ChannelHandlerContext ctx){
    	FullHttpResponse response = new DefaultFullHttpResponse(HttpVersion.HTTP_1_1, HttpResponseStatus.NOT_FOUND);
    	response.headers().set(HttpHeaderNames.CONTENT_LENGTH, 0);
    	ctx.writeAndFlush(response);
    }
