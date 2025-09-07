    private String determineContentType(String fileName) {
        if (fileName == null) {
            return MediaType.APPLICATION_OCTET_STREAM_VALUE;
        }

        String lowerName = fileName.toLowerCase();
        if (lowerName.endsWith(".pdf")) {
            return MediaType.APPLICATION_PDF_VALUE;
        } else if (lowerName.endsWith(".txt")) {
            return MediaType.TEXT_PLAIN_VALUE;
        } else if (lowerName.endsWith(".json")) {
            return MediaType.APPLICATION_JSON_VALUE;
        } else if (lowerName.endsWith(".xml")) {
            return MediaType.APPLICATION_XML_VALUE;
        } else if (lowerName.endsWith(".jpg") || lowerName.endsWith(".jpeg")) {
            return MediaType.IMAGE_JPEG_VALUE;
        } else if (lowerName.endsWith(".png")) {
            return MediaType.IMAGE_PNG_VALUE;
        } else {
            return MediaType.APPLICATION_OCTET_STREAM_VALUE;
        }
    }
