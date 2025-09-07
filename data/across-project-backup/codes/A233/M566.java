    private void decompressStream(COSStream stream) {
        try {
            log.debug("Processing stream: {}", stream);

            // Only remove filter information if it exists
            if (stream.containsKey(COSName.FILTER)
                    || stream.containsKey(COSName.DECODE_PARMS)
                    || stream.containsKey(COSName.D)) {

                // Read the decompressed content first
                byte[] decompressedBytes;
                try (COSInputStream is = stream.createInputStream()) {
                    decompressedBytes = IOUtils.toByteArray(is);
                }

                // Now remove filter information
                stream.removeItem(COSName.FILTER);
                stream.removeItem(COSName.DECODE_PARMS);
                stream.removeItem(COSName.D);

                // Write the raw content back
                try (OutputStream out = stream.createRawOutputStream()) {
                    out.write(decompressedBytes);
                }

                // Set the Length to reflect the new stream size
                stream.setInt(COSName.LENGTH, decompressedBytes.length);
            }
        } catch (IOException e) {
            ExceptionUtils.logException("stream decompression", e);
            // Continue processing other streams even if this one fails
        }
    }
