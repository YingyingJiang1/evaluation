    private void handleSplitByDocCount(
            PDDocument sourceDocument,
            int documentCount,
            ZipOutputStream zipOut,
            String baseFilename)
            throws IOException {
        log.debug("Starting handleSplitByDocCount with documentCount={}", documentCount);
        int totalPageCount = sourceDocument.getNumberOfPages();
        log.debug("Total pages in source document: {}", totalPageCount);

        int pagesPerDocument = totalPageCount / documentCount;
        int extraPages = totalPageCount % documentCount;
        log.debug("Pages per document: {}, Extra pages: {}", pagesPerDocument, extraPages);

        int currentPageIndex = 0;
        int fileIndex = 1;

        for (int i = 0; i < documentCount; i++) {
            log.debug("Creating document {} of {}", i + 1, documentCount);
            PDDocument currentDoc = null;
            try {
                currentDoc = pdfDocumentFactory.createNewDocumentBasedOnOldDocument(sourceDocument);
                log.debug("Successfully created document {} of {}", i + 1, documentCount);
            } catch (Exception e) {
                log.error("Error creating document {} of {}", i + 1, documentCount, e);
                throw ExceptionUtils.createFileProcessingException("split", e);
            }

            int pagesToAdd = pagesPerDocument + (i < extraPages ? 1 : 0);
            log.debug("Adding {} pages to document {}", pagesToAdd, i + 1);

            for (int j = 0; j < pagesToAdd; j++) {
                try {
                    log.debug(
                            "Adding page {} (index {}) to document {}",
                            j + 1,
                            currentPageIndex,
                            i + 1);
                    currentDoc.addPage(sourceDocument.getPage(currentPageIndex));
                    log.debug("Successfully added page {} to document {}", j + 1, i + 1);
                    currentPageIndex++;
                } catch (Exception e) {
                    log.error("Error adding page {} to document {}", j + 1, i + 1, e);
                    throw ExceptionUtils.createFileProcessingException("split", e);
                }
            }

            try {
                log.debug("Saving document {} with {} pages", i + 1, pagesToAdd);
                saveDocumentToZip(currentDoc, zipOut, baseFilename, fileIndex++);
                log.debug("Successfully saved document {}", i + 1);
            } catch (Exception e) {
                log.error("Error saving document {}", i + 1, e);
                throw e;
            }
        }

        log.debug("Completed handleSplitByDocCount with {} documents created", documentCount);
    }
