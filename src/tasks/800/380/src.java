    @POST
    @Produces( { MediaType.APPLICATION_JSON, "application/javascript",
            MediaType.APPLICATION_XML })
    public Response createSession(@QueryParam("op") String op,
            @DefaultValue("5") @QueryParam("expire") String expire,
            @Context UriInfo ui) {
        if (!op.equals("create")) {
            throw new WebApplicationException(Response.status(
                    Response.Status.BAD_REQUEST).entity(
                    new ZError(ui.getRequestUri().toString(), "")).build());
        }

        int expireInSeconds;
        try {
            expireInSeconds = Integer.parseInt(expire);
        } catch (NumberFormatException e) {
            throw new WebApplicationException(Response.status(
                    Response.Status.BAD_REQUEST).build());
        }

        String uuid = UUID.randomUUID().toString();
        while (ZooKeeperService.isConnected(contextPath, uuid)) {
            uuid = UUID.randomUUID().toString();
        }

        // establish the connection to the ZooKeeper cluster
        try {
            ZooKeeperService.getClient(contextPath, uuid, expireInSeconds);
        } catch (IOException e) {
            LOG.error("Failed while trying to create a new session", e);

            throw new WebApplicationException(Response.status(
                    Response.Status.INTERNAL_SERVER_ERROR).build());
        }

        URI uri = ui.getAbsolutePathBuilder().path(uuid).build();
        return Response.created(uri).entity(
                new JSONWithPadding(new ZSession(uuid, uri.toString())))
                .build();
    }
