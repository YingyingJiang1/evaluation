    public static boolean isValidAddress(InetAddress address) {
        return address != null && !address.isLoopbackAddress() // filter 127.x.x.x
                && !address.isAnyLocalAddress() // filter 0.0.0.0
                && !address.isLinkLocalAddress() // filter 169.254.0.0/16
                && !address.getHostAddress().contains(":");// filter IPv6
    }
