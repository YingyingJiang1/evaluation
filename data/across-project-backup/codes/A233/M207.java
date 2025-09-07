    private ResponseEntity<?> handleResultForSyncJob(Object result) throws IOException {
        if (result instanceof byte[]) {
            // Return byte array as PDF
            return ResponseEntity.ok()
                    .contentType(MediaType.APPLICATION_PDF)
                    .header(
                            HttpHeaders.CONTENT_DISPOSITION,
                            "form-data; name=\"attachment\"; filename=\"result.pdf\"")
                    .body(result);
        } else if (result instanceof MultipartFile) {
            // Return MultipartFile content
            MultipartFile file = (MultipartFile) result;
            return ResponseEntity.ok()
                    .contentType(MediaType.parseMediaType(file.getContentType()))
                    .header(
                            HttpHeaders.CONTENT_DISPOSITION,
                            "form-data; name=\"attachment\"; filename=\""
                                    + file.getOriginalFilename()
                                    + "\"")
                    .body(file.getBytes());
        } else {
            // Default case: return as JSON
            return ResponseEntity.ok(result);
        }
    }
