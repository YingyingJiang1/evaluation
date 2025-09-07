    public static String getInetAddress() {
        try {
            Enumeration<NetworkInterface> interfaces = NetworkInterface.getNetworkInterfaces();
            InetAddress address = null;
            while (interfaces.hasMoreElements()) {
                NetworkInterface ni = interfaces.nextElement();
                Enumeration<InetAddress> addresses = ni.getInetAddresses();
                while (addresses.hasMoreElements()) {
                    address = addresses.nextElement();
                    if (isValidAddress(address)) {
                        return address.getHostAddress();
                    }
                }
            }
            logger.warn("Can not get the server IP address");
            return null;
        } catch (Throwable t) {
            logger.error("Can not get the server IP address", t);
            return null;
        }
    }
