    private void handleSplitBySize(
            PDDocument sourceDocument, long maxBytes, ZipOutputStream zipOut, String baseFilename)
            throws IOException {
        log.debug("Starting handleSplitBySize with maxBytes={}", maxBytes);

        PDDocument currentDoc =
                pdfDocumentFactory.createNewDocumentBasedOnOldDocument(sourceDocument);
        int fileIndex = 1;
        int totalPages = sourceDocument.getNumberOfPages();
        int pageAdded = 0;

        // Smart size check frequency - check more often with larger documents
        int baseCheckFrequency = 5;

        for (int pageIndex = 0; pageIndex < totalPages; pageIndex++) {
            PDPage page = sourceDocument.getPage(pageIndex);
            log.debug("Processing page {} of {}", pageIndex + 1, totalPages);

            // Add the page to current document
            PDPage newPage = new PDPage(page.getCOSObject());
            currentDoc.addPage(newPage);
            pageAdded++;

            // Dynamic size checking based on document size and page count
            boolean shouldCheckSize =
                    (pageAdded % baseCheckFrequency == 0)
                            || (pageIndex == totalPages - 1)
                            || (pageAdded >= 20); // Always check after 20 pages

            if (shouldCheckSize) {
                log.debug("Performing size check after {} pages", pageAdded);
                ByteArrayOutputStream checkSizeStream = new ByteArrayOutputStream();
                currentDoc.save(checkSizeStream);
                long actualSize = checkSizeStream.size();
                log.debug("Current document size: {} bytes (max: {} bytes)", actualSize, maxBytes);

                if (actualSize > maxBytes) {
                    // We exceeded the limit - remove the last page and save
                    if (currentDoc.getNumberOfPages() > 1) {
                        currentDoc.removePage(currentDoc.getNumberOfPages() - 1);
                        pageIndex--; // Process this page again in the next document
                        log.debug("Size limit exceeded - removed last page");
                    }

                    log.debug(
                            "Saving document with {} pages as part {}",
                            currentDoc.getNumberOfPages(),
                            fileIndex);
                    saveDocumentToZip(currentDoc, zipOut, baseFilename, fileIndex++);
                    currentDoc = new PDDocument();
                    pageAdded = 0;
                } else if (pageIndex < totalPages - 1) {
                    // We're under the limit, calculate if we might fit more pages
                    // Try to predict how many more similar pages might fit
                    if (actualSize < maxBytes * 0.75 && pageAdded > 0) {
                        // Rather than using a ratio, look ahead to test actual upcoming pages
                        int pagesToLookAhead = Math.min(5, totalPages - pageIndex - 1);

                        if (pagesToLookAhead > 0) {
                            log.debug(
                                    "Testing {} upcoming pages for potential addition",
                                    pagesToLookAhead);

                            // Create a temp document with current pages + look-ahead pages
                            PDDocument testDoc = new PDDocument();
                            // First copy existing pages
                            for (int i = 0; i < currentDoc.getNumberOfPages(); i++) {
                                testDoc.addPage(new PDPage(currentDoc.getPage(i).getCOSObject()));
                            }

                            // Try adding look-ahead pages one by one
                            int extraPagesAdded = 0;
                            for (int i = 0; i < pagesToLookAhead; i++) {
                                int testPageIndex = pageIndex + 1 + i;
                                PDPage testPage = sourceDocument.getPage(testPageIndex);
                                testDoc.addPage(new PDPage(testPage.getCOSObject()));

                                // Check if we're still under size
                                ByteArrayOutputStream testStream = new ByteArrayOutputStream();
                                testDoc.save(testStream);
                                long testSize = testStream.size();

                                if (testSize <= maxBytes) {
                                    extraPagesAdded++;
                                    log.debug(
                                            "Test: Can add page {} (size would be {})",
                                            testPageIndex + 1,
                                            testSize);
                                } else {
                                    log.debug(
                                            "Test: Cannot add page {} (size would be {})",
                                            testPageIndex + 1,
                                            testSize);
                                    break;
                                }
                            }

                            testDoc.close();

                            // Add the pages we verified would fit
                            if (extraPagesAdded > 0) {
                                log.debug("Adding {} verified pages ahead", extraPagesAdded);
                                for (int i = 0; i < extraPagesAdded; i++) {
                                    int extraPageIndex = pageIndex + 1 + i;
                                    PDPage extraPage = sourceDocument.getPage(extraPageIndex);
                                    currentDoc.addPage(new PDPage(extraPage.getCOSObject()));
                                }
                                pageIndex += extraPagesAdded;
                                pageAdded += extraPagesAdded;
                            }
                        }
                    }
                }
            }
        }

        // Save final document if it has any pages
        if (currentDoc.getNumberOfPages() > 0) {
            log.debug(
                    "Saving final document with {} pages as part {}",
                    currentDoc.getNumberOfPages(),
                    fileIndex);
            saveDocumentToZip(currentDoc, zipOut, baseFilename, fileIndex++);
        }

        log.debug("Completed handleSplitBySize with {} document parts created", fileIndex - 1);
    }
