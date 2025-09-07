    public void handleWebSocketFrame(ChannelHandlerContext ctx, WebSocketFrame frame) {
        if (frame instanceof CloseWebSocketFrame) {
            closeOutboundChannel(ctx.channel());
            ctx.close();
            return;
        }

        Channel outboundChannel = channelMappings.get(ctx.channel());
        if (outboundChannel == null || !outboundChannel.isActive()) {
            connectToDestinationServer(ctx, frame);
        } else {
            forwardWebSocketFrame(frame, outboundChannel);
        }
    }
