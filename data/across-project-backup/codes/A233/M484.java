    @PostMapping(consumes = "multipart/form-data", value = "/filter-page-size")
    @Operation(
            summary = "Checks if a PDF is of a certain size",
            description = "Input:PDF Output:Boolean Type:SISO")
    public ResponseEntity<byte[]> pageSize(@ModelAttribute PageSizeRequest request)
            throws IOException, InterruptedException {
        MultipartFile inputFile = request.getFileInput();
        String standardPageSize = request.getStandardPageSize();
        String comparator = request.getComparator();

        // Load the PDF
        PDDocument document = pdfDocumentFactory.load(inputFile);

        PDPage firstPage = document.getPage(0);
        PDRectangle actualPageSize = firstPage.getMediaBox();

        // Calculate the area of the actual page size
        float actualArea = actualPageSize.getWidth() * actualPageSize.getHeight();

        // Get the standard size and calculate its area
        PDRectangle standardSize = PdfUtils.textToPageSize(standardPageSize);
        float standardArea = standardSize.getWidth() * standardSize.getHeight();

        boolean valid = false;
        // Perform the comparison
        switch (comparator) {
            case "Greater":
                valid = actualArea > standardArea;
                break;
            case "Equal":
                valid = actualArea == standardArea;
                break;
            case "Less":
                valid = actualArea < standardArea;
                break;
            default:
                throw ExceptionUtils.createInvalidArgumentException("comparator", comparator);
        }

        if (valid) return WebResponseUtils.multiPartFileToWebResponse(inputFile);
        return null;
    }
