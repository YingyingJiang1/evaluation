    public static byte[] imageToPdf(
            MultipartFile[] files,
            String fitOption,
            boolean autoRotate,
            String colorType,
            CustomPDFDocumentFactory pdfDocumentFactory)
            throws IOException {
        try (PDDocument doc = pdfDocumentFactory.createNewDocument()) {
            for (MultipartFile file : files) {
                String contentType = file.getContentType();
                String originalFilename = Filenames.toSimpleFileName(file.getOriginalFilename());
                if (originalFilename != null
                        && (originalFilename.toLowerCase().endsWith(".tiff")
                                || originalFilename.toLowerCase().endsWith(".tif"))) {
                    ImageReader reader = ImageIO.getImageReadersByFormatName("tiff").next();
                    reader.setInput(ImageIO.createImageInputStream(file.getInputStream()));
                    int numPages = reader.getNumImages(true);
                    for (int i = 0; i < numPages; i++) {
                        BufferedImage pageImage = reader.read(i);
                        BufferedImage convertedImage =
                                ImageProcessingUtils.convertColorType(pageImage, colorType);
                        PDImageXObject pdImage =
                                LosslessFactory.createFromImage(doc, convertedImage);
                        addImageToDocument(doc, pdImage, fitOption, autoRotate);
                    }
                } else {
                    BufferedImage image = ImageProcessingUtils.loadImageWithExifOrientation(file);
                    BufferedImage convertedImage =
                            ImageProcessingUtils.convertColorType(image, colorType);
                    // Use JPEGFactory if it's JPEG since JPEG is lossy
                    PDImageXObject pdImage =
                            (contentType != null && "image/jpeg".equals(contentType))
                                    ? JPEGFactory.createFromImage(doc, convertedImage)
                                    : LosslessFactory.createFromImage(doc, convertedImage);
                    addImageToDocument(doc, pdImage, fitOption, autoRotate);
                }
            }
            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
            doc.save(byteArrayOutputStream);
            log.debug("PDF successfully saved to byte array");
            return byteArrayOutputStream.toByteArray();
        }
    }
