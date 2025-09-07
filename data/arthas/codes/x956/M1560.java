    public void close() {
        if (bossGroup != null) {
            bossGroup.shutdownGracefully();
        }
        if(workerGroup != null){
            workerGroup.shutdownGracefully();
        }
        logger.info("success to close grpc web proxy server!");
    }
