    @PostMapping(consumes = "multipart/form-data", value = "/filter-page-rotation")
    @Operation(
            summary = "Checks if a PDF is of a certain rotation",
            description = "Input:PDF Output:Boolean Type:SISO")
    public ResponseEntity<byte[]> pageRotation(@ModelAttribute PageRotationRequest request)
            throws IOException, InterruptedException {
        MultipartFile inputFile = request.getFileInput();
        int rotation = request.getRotation();
        String comparator = request.getComparator();

        // Load the PDF
        PDDocument document = pdfDocumentFactory.load(inputFile);

        // Get the rotation of the first page
        PDPage firstPage = document.getPage(0);
        int actualRotation = firstPage.getRotation();
        boolean valid = false;
        // Perform the comparison
        switch (comparator) {
            case "Greater":
                valid = actualRotation > rotation;
                break;
            case "Equal":
                valid = actualRotation == rotation;
                break;
            case "Less":
                valid = actualRotation < rotation;
                break;
            default:
                throw ExceptionUtils.createInvalidArgumentException("comparator", comparator);
        }

        if (valid) return WebResponseUtils.multiPartFileToWebResponse(inputFile);
        return null;
    }
