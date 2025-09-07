    @PostMapping(consumes = "multipart/form-data", value = "/rearrange-pages")
    @Operation(
            summary = "Rearrange pages in a PDF file",
            description =
                    "This endpoint rearranges pages in a given PDF file based on the specified page"
                            + " order or custom mode. Users can provide a page order as a"
                            + " comma-separated list of page numbers or page ranges, or a custom mode."
                            + " Input:PDF Output:PDF")
    public ResponseEntity<byte[]> rearrangePages(@ModelAttribute RearrangePagesRequest request)
            throws IOException {
        MultipartFile pdfFile = request.getFileInput();
        String pageOrder = request.getPageNumbers();
        String sortType = request.getCustomMode();
        try {
            // Load the input PDF
            PDDocument document = pdfDocumentFactory.load(pdfFile);

            // Split the page order string into an array of page numbers or range of numbers
            String[] pageOrderArr = pageOrder != null ? pageOrder.split(",") : new String[0];
            int totalPages = document.getNumberOfPages();
            List<Integer> newPageOrder;
            if (sortType != null
                    && sortType.length() > 0
                    && !"custom".equals(sortType.toLowerCase())) {
                newPageOrder = processSortTypes(sortType, totalPages, pageOrder);
            } else {
                newPageOrder = GeneralUtils.parsePageList(pageOrderArr, totalPages, false);
            }
            log.info("newPageOrder = " + newPageOrder);
            log.info("totalPages = " + totalPages);
            // Create a new list to hold the pages in the new order
            List<PDPage> newPages = new ArrayList<>();
            for (int i = 0; i < newPageOrder.size(); i++) {
                newPages.add(document.getPage(newPageOrder.get(i)));
            }

            // Remove all the pages from the original document
            for (int i = document.getNumberOfPages() - 1; i >= 0; i--) {
                document.removePage(i);
            }

            // Add the pages in the new order
            for (PDPage page : newPages) {
                document.addPage(page);
            }

            return WebResponseUtils.pdfDocToWebResponse(
                    document,
                    Filenames.toSimpleFileName(pdfFile.getOriginalFilename())
                                    .replaceFirst("[.][^.]+$", "")
                            + "_rearranged.pdf");
        } catch (IOException e) {
            ExceptionUtils.logException("document rearrangement", e);
            throw e;
        }
    }
