    synchronized void writeTrailer(Status status, Metadata trailer) {
        if (isTrailerSent) {
            return;
        }
        StringBuffer sb = new StringBuffer();
        if (trailer != null) {
            Map<String, String> ht = MetadataUtil.getHttpHeadersFromMetadata(trailer);
            for (String key : ht.keySet()) {
                sb.append(String.format("%s:%s\r\n", key, ht.get(key)));
            }
        }
        sb.append(String.format("grpc-status:%d\r\n", status.getCode().value()));
        if (status.getDescription() != null && !status.getDescription().isEmpty()) {
            sb.append(String.format("grpc-message:%s\r\n", status.getDescription()));
        }

        writeResponse(sb.toString().getBytes(), MessageFramer.Type.TRAILER);

        isTrailerSent = true;

        writeEndChunk();
    }
