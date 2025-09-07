    private Map<String, List<ImageReference>> findImages(PDDocument doc) throws IOException {
        Map<String, List<ImageReference>> uniqueImages = new HashMap<>();

        // Scan through all pages in the document
        for (int pageNum = 0; pageNum < doc.getNumberOfPages(); pageNum++) {
            PDPage page = doc.getPage(pageNum);
            PDResources res = page.getResources();
            if (res == null || res.getXObjectNames() == null) continue;

            // Process all XObjects on the page
            for (COSName name : res.getXObjectNames()) {
                PDXObject xobj = res.getXObject(name);

                // Direct image
                if (isImage(xobj)) {
                    addDirectImage(pageNum, name, (PDImageXObject) xobj, uniqueImages);
                    log.info(
                            "Found direct image '{}' on page {} - {}x{}",
                            name.getName(),
                            pageNum + 1,
                            ((PDImageXObject) xobj).getWidth(),
                            ((PDImageXObject) xobj).getHeight());
                }
                // Form XObject that may contain nested images
                else if (isForm(xobj)) {
                    checkFormForImages(pageNum, name, (PDFormXObject) xobj, uniqueImages);
                }
            }
        }

        return uniqueImages;
    }
