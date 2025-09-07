    public static Principal localPrincipal(ChannelHandlerContext ctx) {
        if (configure.isLocalConnectionNonAuth() && isLocalConnection(ctx)) {
            return new LocalConnectionPrincipal();
        }
        return null;
    }
