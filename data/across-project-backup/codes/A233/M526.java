    private void checkFormForImages(
            int pageNum,
            COSName formName,
            PDFormXObject formXObj,
            Map<String, List<ImageReference>> uniqueImages)
            throws IOException {
        PDResources formResources = formXObj.getResources();
        if (formResources == null || formResources.getXObjectNames() == null) {
            return;
        }

        log.info(
                "Checking form XObject '{}' on page {} for nested images",
                formName.getName(),
                pageNum + 1);

        // Process all XObjects within the form
        for (COSName nestedName : formResources.getXObjectNames()) {
            PDXObject nestedXobj = formResources.getXObject(nestedName);

            if (isImage(nestedXobj)) {
                PDImageXObject nestedImage = (PDImageXObject) nestedXobj;

                log.info(
                        "Found nested image '{}' in form '{}' on page {} - {}x{}",
                        nestedName.getName(),
                        formName.getName(),
                        pageNum + 1,
                        nestedImage.getWidth(),
                        nestedImage.getHeight());

                // Create specialized reference for the nested image
                NestedImageReference nestedRef = new NestedImageReference();
                nestedRef.pageNum = pageNum;
                nestedRef.formName = formName;
                nestedRef.imageName = nestedName;

                String imageHash = generateImageHash(nestedImage);
                uniqueImages.computeIfAbsent(imageHash, k -> new ArrayList<>()).add(nestedRef);
            }
        }
    }
