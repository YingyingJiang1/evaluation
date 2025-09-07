    private void writeEndChunk() {
        if (isEndChunkSent) {
            return;
        }
        LastHttpContent end = new DefaultLastHttpContent();
        ctx.writeAndFlush(end);
        isEndChunkSent = true;
    }
