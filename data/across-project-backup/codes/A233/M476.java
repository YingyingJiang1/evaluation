    private void handleSplitByPageCount(
            PDDocument sourceDocument, int pageCount, ZipOutputStream zipOut, String baseFilename)
            throws IOException {
        log.debug("Starting handleSplitByPageCount with pageCount={}", pageCount);
        int currentPageCount = 0;
        log.debug("Creating initial output document");
        PDDocument currentDoc = null;
        try {
            currentDoc = pdfDocumentFactory.createNewDocumentBasedOnOldDocument(sourceDocument);
            log.debug("Successfully created initial output document");
        } catch (Exception e) {
            ExceptionUtils.logException("initial output document creation", e);
            throw ExceptionUtils.createFileProcessingException("split", e);
        }

        int fileIndex = 1;
        int pageIndex = 0;
        int totalPages = sourceDocument.getNumberOfPages();
        log.debug("Processing {} pages", totalPages);

        try {
            for (PDPage page : sourceDocument.getPages()) {
                pageIndex++;
                log.debug("Processing page {} of {}", pageIndex, totalPages);

                try {
                    log.debug("Adding page {} to current document", pageIndex);
                    currentDoc.addPage(page);
                    log.debug("Successfully added page {} to current document", pageIndex);
                } catch (Exception e) {
                    log.error("Error adding page {} to current document", pageIndex, e);
                    throw ExceptionUtils.createFileProcessingException("split", e);
                }

                currentPageCount++;
                log.debug("Current page count: {}/{}", currentPageCount, pageCount);

                if (currentPageCount == pageCount) {
                    log.debug(
                            "Reached target page count ({}), saving current document as part {}",
                            pageCount,
                            fileIndex);
                    try {
                        saveDocumentToZip(currentDoc, zipOut, baseFilename, fileIndex++);
                        log.debug("Successfully saved document part {}", fileIndex - 1);
                    } catch (Exception e) {
                        log.error("Error saving document part {}", fileIndex - 1, e);
                        throw e;
                    }

                    try {
                        log.debug("Creating new document for next part");
                        currentDoc = new PDDocument();
                        log.debug("Successfully created new document");
                    } catch (Exception e) {
                        log.error("Error creating new document for next part", e);
                        throw ExceptionUtils.createFileProcessingException("split", e);
                    }

                    currentPageCount = 0;
                    log.debug("Reset current page count to 0");
                }
            }
        } catch (Exception e) {
            log.error("Error iterating through pages", e);
            throw ExceptionUtils.createFileProcessingException("split", e);
        }

        // Add the last document if it contains any pages
        try {
            if (currentDoc.getPages().getCount() != 0) {
                log.debug(
                        "Saving final document with {} pages as part {}",
                        currentDoc.getPages().getCount(),
                        fileIndex);
                try {
                    saveDocumentToZip(currentDoc, zipOut, baseFilename, fileIndex++);
                    log.debug("Successfully saved final document part {}", fileIndex - 1);
                } catch (Exception e) {
                    log.error("Error saving final document part {}", fileIndex - 1, e);
                    throw e;
                }
            } else {
                log.debug("Final document has no pages, skipping");
            }
        } catch (Exception e) {
            log.error("Error checking or saving final document", e);
            throw ExceptionUtils.createFileProcessingException("split", e);
        } finally {
            try {
                log.debug("Closing final document");
                currentDoc.close();
                log.debug("Successfully closed final document");
            } catch (Exception e) {
                log.error("Error closing final document", e);
            }
        }

        log.debug("Completed handleSplitByPageCount with {} document parts created", fileIndex - 1);
    }
