    @PostMapping(consumes = "multipart/form-data", value = "/filter-file-size")
    @Operation(
            summary = "Checks if a PDF is a set file size",
            description = "Input:PDF Output:Boolean Type:SISO")
    public ResponseEntity<byte[]> fileSize(@ModelAttribute FileSizeRequest request)
            throws IOException, InterruptedException {
        MultipartFile inputFile = request.getFileInput();
        long fileSize = request.getFileSize();
        String comparator = request.getComparator();

        // Get the file size
        long actualFileSize = inputFile.getSize();

        boolean valid = false;
        // Perform the comparison
        switch (comparator) {
            case "Greater":
                valid = actualFileSize > fileSize;
                break;
            case "Equal":
                valid = actualFileSize == fileSize;
                break;
            case "Less":
                valid = actualFileSize < fileSize;
                break;
            default:
                throw ExceptionUtils.createInvalidArgumentException("comparator", comparator);
        }

        if (valid) return WebResponseUtils.multiPartFileToWebResponse(inputFile);
        return null;
    }
