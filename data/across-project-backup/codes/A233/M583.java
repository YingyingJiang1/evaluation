    @PostMapping(value = "/split-pdf-by-chapters", consumes = "multipart/form-data")
    @Operation(
            summary = "Split PDFs by Chapters",
            description = "Splits a PDF into chapters and returns a ZIP file.")
    public ResponseEntity<byte[]> splitPdf(@ModelAttribute SplitPdfByChaptersRequest request)
            throws Exception {
        MultipartFile file = request.getFileInput();
        PDDocument sourceDocument = null;
        Path zipFile = null;

        try {
            boolean includeMetadata = Boolean.TRUE.equals(request.getIncludeMetadata());
            Integer bookmarkLevel =
                    request.getBookmarkLevel(); // levels start from 0 (top most bookmarks)
            if (bookmarkLevel < 0) {
                throw ExceptionUtils.createIllegalArgumentException(
                        "error.invalidArgument", "Invalid argument: {0}", "bookmark level");
            }
            sourceDocument = pdfDocumentFactory.load(file);

            PDDocumentOutline outline = sourceDocument.getDocumentCatalog().getDocumentOutline();

            if (outline == null) {
                log.warn("No outline found for {}", file.getOriginalFilename());
                throw ExceptionUtils.createIllegalArgumentException(
                        "error.pdfBookmarksNotFound", "No PDF bookmarks/outline found in document");
            }
            List<Bookmark> bookmarks = new ArrayList<>();
            try {
                bookmarks =
                        extractOutlineItems(
                                sourceDocument,
                                outline.getFirstChild(),
                                bookmarks,
                                outline.getFirstChild().getNextSibling(),
                                0,
                                bookmarkLevel);
                // to handle last page edge case
                bookmarks.get(bookmarks.size() - 1).setEndPage(sourceDocument.getNumberOfPages());
                Bookmark lastBookmark = bookmarks.get(bookmarks.size() - 1);

            } catch (Exception e) {
                ExceptionUtils.logException("outline extraction", e);
                return ResponseEntity.internalServerError()
                        .body("Unable to extract outline items".getBytes());
            }

            boolean allowDuplicates = Boolean.TRUE.equals(request.getAllowDuplicates());
            if (!allowDuplicates) {
                /*
                duplicates are generated when multiple bookmarks correspond to the same page,
                if the user doesn't want duplicates mergeBookmarksThatCorrespondToSamePage() method will merge the titles of all
                the bookmarks that correspond to the same page, and treat them as a single bookmark
                */
                bookmarks = mergeBookmarksThatCorrespondToSamePage(bookmarks);
            }
            for (Bookmark bookmark : bookmarks) {
                log.info(
                        "{}::::{} to {}",
                        bookmark.getTitle(),
                        bookmark.getStartPage(),
                        bookmark.getEndPage());
            }
            List<ByteArrayOutputStream> splitDocumentsBoas =
                    getSplitDocumentsBoas(sourceDocument, bookmarks, includeMetadata);

            zipFile = createZipFile(bookmarks, splitDocumentsBoas);

            byte[] data = Files.readAllBytes(zipFile);
            Files.deleteIfExists(zipFile);

            String filename =
                    Filenames.toSimpleFileName(file.getOriginalFilename())
                            .replaceFirst("[.][^.]+$", "");
            sourceDocument.close();
            return WebResponseUtils.bytesToWebResponse(
                    data, filename + ".zip", MediaType.APPLICATION_OCTET_STREAM);
        } finally {
            try {
                if (sourceDocument != null) {
                    sourceDocument.close();
                }
                if (zipFile != null) {
                    Files.deleteIfExists(zipFile);
                }
            } catch (Exception e) {
                log.error("Error while cleaning up resources", e);
            }
        }
    }
