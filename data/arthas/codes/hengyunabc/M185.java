        @Override
        public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
            logger.error("Proxy Client error", cause);
            ctx.close();
        }
