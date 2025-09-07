    @Override
    public void initChannel(SocketChannel ch) throws Exception {
        ChannelPipeline pipeline = ch.pipeline();
        //将请求和应答消息编码或解码为HTTP消息
        pipeline.addLast(new HttpServerCodec());
        //将HTTP消息的多个部分组合成一条完整的HTTP消息
        pipeline.addLast(new HttpObjectAggregator(64 * 1024));
        pipeline.addLast(new ChunkedWriteHandler());
        pipeline.addLast(new NettyHttpStaticFileHandler(this.STATIC_LOCATION));
    }
