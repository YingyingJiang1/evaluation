    @Override
    public InetAddress convert(String source, Class<InetAddress> targetType) {
        try {
            return InetAddress.getByName(source);
        } catch (UnknownHostException e) {
            throw new IllegalArgumentException("Invalid InetAddress value '" + source + "'", e);
        }
    }
