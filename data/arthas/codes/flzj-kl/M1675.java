    private void closeOutboundChannel(Channel inboundChannel) {
        Channel outboundChannel = channelMappings.remove(inboundChannel);
        if (outboundChannel != null) {
            logger.info("Closing outbound channel");
            outboundChannel.close();
        }
    }
