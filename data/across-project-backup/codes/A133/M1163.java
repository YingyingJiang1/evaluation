    public static DefaultFullHttpResponse directView(File dir, String path, FullHttpRequest request, ChannelHandlerContext ctx) throws IOException {
        if (path.startsWith("/")) {
            path = path.substring(1);
        }

        // path maybe: arthas-output/20201225-203454.svg 
        // 需要取 dir的parent来去掉前缀
        File file = new File(dir.getParent(), path);
        HttpVersion version = request.protocolVersion();
        if (isSubFile(dir, file)) {
            DefaultFullHttpResponse fullResp = new DefaultFullHttpResponse(version, HttpResponseStatus.OK);

            if (file.isDirectory()) {
                if (!path.endsWith("/")) {
                    fullResp.setStatus(HttpResponseStatus.FOUND).headers().set(HttpHeaderNames.LOCATION, "/" + path + "/");
                }
                String renderResult = renderDir(file, !isSameFile(dir, file));
                fullResp.content().writeBytes(renderResult.getBytes("utf-8"));
                fullResp.headers().set(HttpHeaderNames.CONTENT_TYPE, "text/html; charset=utf-8");
                ctx.write(fullResp);
                ChannelFuture future = ctx.writeAndFlush(LastHttpContent.EMPTY_LAST_CONTENT);
                future.addListener(ChannelFutureListener.CLOSE);
                return fullResp;
            } else {
                logger.info("get file now. file:" + file.getPath());
                if (file.isHidden() || !file.exists() || file.isDirectory() || !file.isFile()) {
                    return null;
                }

                long fileLength = file.length();
                if (fileLength < MIN_NETTY_DIRECT_SEND_SIZE){
                    FileInputStream fileInputStream = new FileInputStream(file);
                    try {
                        byte[] content = IOUtils.getBytes(fileInputStream);
                        fullResp.content().writeBytes(content);
                        HttpUtil.setContentLength(fullResp, fullResp.content().readableBytes());
                    } finally {
                        IOUtils.close(fileInputStream);
                    }
                    ChannelFuture channelFuture = ctx.writeAndFlush(fullResp);
                    channelFuture.addListener((ChannelFutureListener) future -> {
                        if (future.isSuccess()) {
                            ChannelFuture lastContentFuture = ctx.writeAndFlush(LastHttpContent.EMPTY_LAST_CONTENT);
                            lastContentFuture.addListener(ChannelFutureListener.CLOSE);
                        } else {
                            future.channel().close();
                        }
                    });
                    return fullResp;
                }
                logger.info("file {} size bigger than {}, send by future.",file.getName(), MIN_NETTY_DIRECT_SEND_SIZE);
                HttpResponse response = new DefaultHttpResponse(HTTP_1_1, OK);
                HttpUtil.setContentLength(response, fileLength);
                setContentTypeHeader(response, file);
                setDateAndCacheHeaders(response, file);
                if (HttpUtil.isKeepAlive(request)) {
                    response.headers().set(HttpHeaderNames.CONNECTION, HttpHeaderValues.KEEP_ALIVE);
                }

                // Write the initial line and the header.
                ctx.write(response);
                // Write the content.
                ChannelFuture sendFileFuture;
                ChannelFuture lastContentFuture;
                RandomAccessFile raf = new RandomAccessFile(file, "r"); // will closed by netty
                if (ctx.pipeline().get(SslHandler.class) == null) {
                    sendFileFuture =
                            ctx.write(new DefaultFileRegion(raf.getChannel(), 0, fileLength), ctx.newProgressivePromise());
                    // Write the end marker.
                    lastContentFuture = ctx.writeAndFlush(LastHttpContent.EMPTY_LAST_CONTENT);
                } else {
                    sendFileFuture =
                            ctx.writeAndFlush(new HttpChunkedInput(new ChunkedFile(raf, 0, fileLength, 8192)),
                                    ctx.newProgressivePromise());
                    // HttpChunkedInput will write the end marker (LastHttpContent) for us.
                    lastContentFuture = sendFileFuture;
                }

                sendFileFuture.addListener(new ChannelProgressiveFutureListener() {
                    @Override
                    public void operationProgressed(ChannelProgressiveFuture future, long progress, long total) {
                        if (total < 0) { // total unknown
                            logger.info(future.channel() + " Transfer progress: " + progress);
                        } else {
                            logger.info(future.channel() + " Transfer progress: " + progress + " / " + total);
                        }
                    }

                    @Override
                    public void operationComplete(ChannelProgressiveFuture future) {
                        logger.info(future.channel() + " Transfer complete.");
                    }
                });

                // Decide whether to close the connection or not.
                if (!HttpUtil.isKeepAlive(request)) {
                    // Close the connection when the whole content is written out.
                    lastContentFuture.addListener(ChannelFutureListener.CLOSE);
                }
                return fullResp;
            }
        }

        return null;
    }
