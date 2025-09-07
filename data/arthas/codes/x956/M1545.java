    synchronized void returnUnimplementedStatusCode(String className) {
        writeHeaders(null);
        writeTrailer(
                Status.UNIMPLEMENTED.withDescription("Can not find service impl, check dep, service: " + className),
                null);
    }
