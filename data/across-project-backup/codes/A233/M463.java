    @PostMapping(value = "/extract-bookmarks", consumes = "multipart/form-data")
    @Operation(
            summary = "Extract PDF Bookmarks",
            description = "Extracts bookmarks/table of contents from a PDF document as JSON.")
    @ResponseBody
    public List<Map<String, Object>> extractBookmarks(@RequestParam("file") MultipartFile file)
            throws Exception {
        PDDocument document = null;
        try {
            document = pdfDocumentFactory.load(file);
            PDDocumentOutline outline = document.getDocumentCatalog().getDocumentOutline();

            if (outline == null) {
                log.info("No outline/bookmarks found in PDF");
                return new ArrayList<>();
            }

            return extractBookmarkItems(document, outline);
        } finally {
            if (document != null) {
                document.close();
            }
        }
    }
