    private Pair<String, String> getClassAndMethod(String pathInfo) throws IllegalArgumentException {
        // pathInfo starts with "/". ignore that first char.
        String[] rpcClassAndMethodTokens = pathInfo.substring(1).split("/");
        if (rpcClassAndMethodTokens.length != 2) {
            throw new IllegalArgumentException("incorrect pathinfo: " + pathInfo);
        }

        String rpcClassName = rpcClassAndMethodTokens[0];
        String rpcMethodNameRecvd = rpcClassAndMethodTokens[1];
        String rpcMethodName = rpcMethodNameRecvd.substring(0, 1).toLowerCase() + rpcMethodNameRecvd.substring(1);
        return new Pair<>(rpcClassName, rpcMethodName);
    }
