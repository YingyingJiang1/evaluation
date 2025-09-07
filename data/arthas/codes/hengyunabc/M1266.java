    public static boolean isLocalConnection(ChannelHandlerContext ctx) {
        SocketAddress remoteAddress = ctx.channel().remoteAddress();
        if (remoteAddress instanceof InetSocketAddress) {
            String hostAddress = ((InetSocketAddress) remoteAddress).getAddress().getHostAddress();
            if ("127.0.0.1".equals(hostAddress)) {
                return true;
            }
        }
        return false;
    }
