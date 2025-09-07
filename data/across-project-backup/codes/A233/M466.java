    @PostMapping(value = "/edit-table-of-contents", consumes = "multipart/form-data")
    @Operation(
            summary = "Edit Table of Contents",
            description = "Add or edit bookmarks/table of contents in a PDF document.")
    public ResponseEntity<byte[]> editTableOfContents(
            @ModelAttribute EditTableOfContentsRequest request) throws Exception {
        MultipartFile file = request.getFileInput();
        PDDocument document = null;

        try {
            document = pdfDocumentFactory.load(file);

            // Parse the bookmark data from JSON
            List<BookmarkItem> bookmarks =
                    objectMapper.readValue(
                            request.getBookmarkData(), new TypeReference<List<BookmarkItem>>() {});

            // Create a new document outline
            PDDocumentOutline outline = new PDDocumentOutline();
            document.getDocumentCatalog().setDocumentOutline(outline);

            // Add bookmarks to the outline
            addBookmarksToOutline(document, outline, bookmarks);

            // Save the document to a byte array
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            document.save(baos);

            String filename = file.getOriginalFilename().replaceFirst("[.][^.]+$", "");
            return WebResponseUtils.bytesToWebResponse(
                    baos.toByteArray(), filename + "_with_toc.pdf", MediaType.APPLICATION_PDF);

        } finally {
            if (document != null) {
                document.close();
            }
        }
    }
