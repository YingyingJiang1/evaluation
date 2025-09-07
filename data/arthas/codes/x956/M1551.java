    public static ContentType validateContentType(String contentType) throws IllegalArgumentException {
        if (contentType == null || !GRPC_GCP_CONTENT_TYPES.containsKey(contentType)) {
            throw new IllegalArgumentException("This content type is not used for grpc-web: " + contentType);
        }
        return getContentType(contentType);
    }
