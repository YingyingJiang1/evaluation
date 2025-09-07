    private void forwardWebSocketFrame(WebSocketFrame frame, Channel outboundChannel) {
        if (outboundChannel != null && outboundChannel.isActive()) {
            outboundChannel.writeAndFlush(frame.retain()).addListener(future -> {
                if (!future.isSuccess()) {
                    logger.error("Failed to forward WebSocket frame", future.cause());
                }
            });
        } else {
            logger.warn("Outbound channel is not active. Cannot forward frame.");
        }
    }
