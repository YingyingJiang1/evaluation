    private byte[] decompressGzip(byte[] compressedData) {
        boolean isGzip = (compressedData.length > 2 && (compressedData[0] & 0xff) == 0x1f && (compressedData[1] & 0xff) == 0x8b);
        if (isGzip) {
            try (InputStream byteStream = new ByteArrayInputStream(compressedData);
                 GZIPInputStream gzipStream = new GZIPInputStream(byteStream);
                 ByteArrayOutputStream out = new ByteArrayOutputStream()) {

                byte[] buffer = new byte[1024];
                int len;
                while ((len = gzipStream.read(buffer)) != -1) {
                    out.write(buffer, 0, len);
                }
                return out.toByteArray();
            } catch (IOException e) {
                System.err.println("Failed to decompress GZIP data: " + e.getMessage());
                // Optionally rethrow the exception or return an Optional<byte[]>
                return null; // or throw new RuntimeException(e);
            }
        } else {
            return compressedData;
        }
    }
