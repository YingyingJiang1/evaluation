    private void replaceImageReference(
            PDDocument doc, ImageReference ref, PDImageXObject compressedImage) throws IOException {
        if (ref instanceof NestedImageReference) {
            // Replace nested image within form XObject
            NestedImageReference nestedRef = (NestedImageReference) ref;
            PDPage page = doc.getPage(nestedRef.pageNum);
            PDResources pageResources = page.getResources();

            // Get the form XObject
            PDFormXObject formXObj = (PDFormXObject) pageResources.getXObject(nestedRef.formName);

            // Replace the nested image in the form's resources
            PDResources formResources = formXObj.getResources();
            formResources.put(nestedRef.imageName, compressedImage);

            log.info(
                    "Replaced nested image '{}' in form '{}' on page {} with compressed version",
                    nestedRef.imageName.getName(),
                    nestedRef.formName.getName(),
                    nestedRef.pageNum + 1);
        } else {
            // Replace direct image in page resources
            PDPage page = doc.getPage(ref.pageNum);
            PDResources resources = page.getResources();
            resources.put(ref.name, compressedImage);

            log.info("Replaced direct image on page {} with compressed version", ref.pageNum + 1);
        }
    }
