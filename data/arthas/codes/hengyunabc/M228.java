    @RequestMapping(value = "/proxy/{agentId}/**")
    @ResponseBody
    public ResponseEntity<?> execute(@PathVariable(name = "agentId", required = true) String agentId,
            HttpServletRequest request) throws InterruptedException, ExecutionException, TimeoutException {

        String fullPath = (String) request.getAttribute(HandlerMapping.PATH_WITHIN_HANDLER_MAPPING_ATTRIBUTE);
        String targetUrl = fullPath.substring("/proxy/".length() + agentId.length());

        logger.info("http proxy, agentId: {}, targetUrl: {}", agentId, targetUrl);

        Optional<AgentInfo> findAgent = tunnelServer.findAgent(agentId);

        if (findAgent.isPresent()) {
            String requestId = RandomStringUtils.random(20, true, true).toUpperCase();

            ChannelHandlerContext agentCtx = findAgent.get().getChannelHandlerContext();

            Promise<SimpleHttpResponse> httpResponsePromise = GlobalEventExecutor.INSTANCE.newPromise();

            tunnelServer.addProxyRequestPromise(requestId, httpResponsePromise);

            URI uri = UriComponentsBuilder.newInstance().scheme(URIConstans.RESPONSE).path("/")
                    .queryParam(URIConstans.METHOD, MethodConstants.HTTP_PROXY).queryParam(URIConstans.ID, agentId)
                    .queryParam(URIConstans.TARGET_URL, targetUrl).queryParam(URIConstans.PROXY_REQUEST_ID, requestId)
                    .build().toUri();

            agentCtx.channel().writeAndFlush(new TextWebSocketFrame(uri.toString()));
            logger.info("waitting for arthas agent http proxy, agentId: {}, targetUrl: {}", agentId, targetUrl);

            SimpleHttpResponse simpleHttpResponse = httpResponsePromise.get(15, TimeUnit.SECONDS);

            BodyBuilder bodyBuilder = ResponseEntity.status(simpleHttpResponse.getStatus());
            for (Entry<String, String> entry : simpleHttpResponse.getHeaders().entrySet()) {
                bodyBuilder.header(entry.getKey(), entry.getValue());
            }
            ResponseEntity<byte[]> responseEntity = bodyBuilder.body(simpleHttpResponse.getContent());
            return responseEntity;
        } else {
            logger.error("can not find agent by agentId: {}", agentId);
        }

        return ResponseEntity.notFound().build();
    }
