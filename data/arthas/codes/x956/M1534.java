    public void handle(ChannelHandlerContext ctx, FullHttpRequest req) {
        // 处理 CORS OPTIONS 请求
        if (req.method().equals(HttpMethod.OPTIONS)) {
            FullHttpResponse response = new DefaultFullHttpResponse(HttpVersion.HTTP_1_1, HttpResponseStatus.OK);
            CorsUtils.updateCorsHeader(response.headers());
            ctx.writeAndFlush(response);
            return;
        }

        String contentTypeStr = req.headers().get(HttpHeaderNames.CONTENT_TYPE);

        MessageUtils.ContentType contentType = MessageUtils.validateContentType(contentTypeStr);
        SendGrpcWebResponse sendResponse = new SendGrpcWebResponse(ctx, req);

        try {
            // From the request, get the rpc-method name and class name and then get their
            // corresponding
            // concrete objects.
            QueryStringDecoder queryStringDecoder = new QueryStringDecoder(req.uri());
            String pathInfo = queryStringDecoder.path();

            Pair<String, String> classAndMethodNames = getClassAndMethod(pathInfo);
            String className = classAndMethodNames.getFirst();
            String methodName = classAndMethodNames.getSecond();
            Class cls = getClassObject(className);
            if (cls == null) {
                logger.error("cannot find service impl in the request, className: " + className);
                // incorrect classname specified in the request.
                sendResponse.returnUnimplementedStatusCode(className);
                return;
            }

            // Create a ClientInterceptor object
            CountDownLatch latch = new CountDownLatch(1);
            GrpcWebClientInterceptor interceptor = new GrpcWebClientInterceptor(latch, sendResponse);
            Channel channel = grpcServiceConnectionManager.getChannelWithClientInterceptor(interceptor);

            // get the stub for the rpc call and the method to be called within the stub
            io.grpc.stub.AbstractStub asyncStub = getRpcStub(channel, cls, "newStub");
            Metadata headers = MetadataUtil.getHtpHeaders(req.headers());
            if (!headers.keys().isEmpty()) {
                asyncStub = MetadataUtils.attachHeaders(asyncStub, headers);
            }
            Method asyncStubCall = getRpcMethod(asyncStub, methodName);
            // Get the input object bytes
            ByteBuf content = req.content();
            InputStream in = new ByteBufInputStream(content);
            MessageDeframer deframer = new MessageDeframer();
            Object inObj = null;
            if (deframer.processInput(in, contentType)) {
                inObj = MessageUtils.getInputProtobufObj(asyncStubCall, deframer.getMessageBytes());
            }
            ManagedChannel managedChannel = grpcServiceConnectionManager.getChannel();
            // Invoke the rpc call
            asyncStubCall.invoke(asyncStub, inObj, new GrpcCallResponseReceiver(sendResponse, latch,managedChannel));
            if (!latch.await( 1000, TimeUnit.MILLISECONDS)) {
                logger.warn("grpc call took too long!");
            }
        } catch (Exception e) {
            logger.error("try to invoke grpc serivce error, uri: {}", req.uri(), e);
            sendResponse.writeError(Status.UNAVAILABLE.withCause(e));
        }
    }
