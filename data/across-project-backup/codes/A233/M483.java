    @PostMapping(consumes = "multipart/form-data", value = "/filter-page-count")
    @Operation(
            summary = "Checks if a PDF is greater, less or equal to a setPageCount",
            description = "Input:PDF Output:Boolean Type:SISO")
    public ResponseEntity<byte[]> pageCount(@ModelAttribute PDFComparisonAndCount request)
            throws IOException, InterruptedException {
        MultipartFile inputFile = request.getFileInput();
        int pageCount = request.getPageCount();
        String comparator = request.getComparator();
        // Load the PDF
        PDDocument document = pdfDocumentFactory.load(inputFile);
        int actualPageCount = document.getNumberOfPages();

        boolean valid = false;
        // Perform the comparison
        switch (comparator) {
            case "Greater":
                valid = actualPageCount > pageCount;
                break;
            case "Equal":
                valid = actualPageCount == pageCount;
                break;
            case "Less":
                valid = actualPageCount < pageCount;
                break;
            default:
                throw ExceptionUtils.createInvalidArgumentException("comparator", comparator);
        }

        if (valid) return WebResponseUtils.multiPartFileToWebResponse(inputFile);
        return null;
    }
