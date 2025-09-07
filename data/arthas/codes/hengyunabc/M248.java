                @Override
                public void operationComplete(final Future<Channel> future) throws Exception {
                    final Channel outboundChannel = future.getNow();
                    if (future.isSuccess()) {
                        tunnelSocketCtx.pipeline().remove(TunnelSocketFrameHandler.this);

                        // outboundChannel is form arthas agent
                        outboundChannel.pipeline().removeLast();

                        outboundChannel.pipeline().addLast(new RelayHandler(tunnelSocketCtx.channel()));
                        tunnelSocketCtx.pipeline().addLast(new RelayHandler(outboundChannel));
                    } else {
                        logger.error("wait for agent connect error. agentId: {}, clientConnectionId: {}", agentId,
                                clientConnectionId);
                        ChannelUtils.closeOnFlush(agentCtx.channel());
                    }
                }
