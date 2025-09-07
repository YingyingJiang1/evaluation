        @Override
        public void checkClientTrusted(X509Certificate[] chain, String s) {
            if (logger.isDebugEnabled()) {
                logger.debug("Accepting a client certificate: " + chain[0].getSubjectDN());
            }
        }
