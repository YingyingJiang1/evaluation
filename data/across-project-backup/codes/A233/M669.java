    private void addTableOfContents(PDDocument mergedDocument, MultipartFile[] files) {
        // Create the document outline
        PDDocumentOutline outline = new PDDocumentOutline();
        mergedDocument.getDocumentCatalog().setDocumentOutline(outline);

        int pageIndex = 0; // Current page index in the merged document

        // Iterate through the original files
        for (MultipartFile file : files) {
            // Get the filename without extension to use as bookmark title
            String filename = file.getOriginalFilename();
            String title = filename;
            if (title != null && title.contains(".")) {
                title = title.substring(0, title.lastIndexOf('.'));
            }

            // Create an outline item for this file
            PDOutlineItem item = new PDOutlineItem();
            item.setTitle(title);

            // Set the destination to the first page of this file in the merged document
            if (pageIndex < mergedDocument.getNumberOfPages()) {
                PDPage page = mergedDocument.getPage(pageIndex);
                item.setDestination(page);
            }

            // Add the item to the outline
            outline.addLast(item);

            // Increment page index for the next file
            try (PDDocument doc = pdfDocumentFactory.load(file)) {
                pageIndex += doc.getNumberOfPages();
            } catch (IOException e) {
                ExceptionUtils.logException("document loading for TOC generation", e);
                pageIndex++; // Increment by at least one if we can't determine page count
            }
        }
    }
