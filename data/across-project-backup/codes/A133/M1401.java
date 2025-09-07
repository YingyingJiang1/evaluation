    public static String getLocalIP() {
        InetAddress ip = null;
        try {
            if (isWindowsOS()) {
                ip = InetAddress.getLocalHost();
            } else {
                // scan all NetWorkInterfaces if it's loopback address
                if (!InetAddress.getLocalHost().isLoopbackAddress()) {
                    ip = InetAddress.getLocalHost();
                } else {
                    boolean bFindIP = false;
                    Enumeration<NetworkInterface> netInterfaces = NetworkInterface.getNetworkInterfaces();
                    while (netInterfaces.hasMoreElements()) {
                        if (bFindIP) {
                            break;
                        }
                        NetworkInterface ni = netInterfaces.nextElement();
                        // ----------特定情况，可以考虑用ni.getName判断
                        // iterator all IPs
                        Enumeration<InetAddress> ips = ni.getInetAddresses();
                        while (ips.hasMoreElements()) {
                            ip = ips.nextElement();
                            // IP starts with 127. is loopback address
                            if (ip.isSiteLocalAddress() && !ip.isLoopbackAddress()
                                    && !ip.getHostAddress().contains(":")) {
                                bFindIP = true;
                                break;
                            }
                        }

                    }
                }
            }
        } catch (Exception e) {
        }

        return ip == null ? null : ip.getHostAddress();
    }
