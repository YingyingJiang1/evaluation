    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
        logger.error("RelayHandler error", cause);
        try {
            if (relayChannel.isActive()) {
                relayChannel.close();
            }
        } finally {
            ctx.close();
        }
    }
