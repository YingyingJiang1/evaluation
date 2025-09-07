    private Map<String, PDImageXObject> createCompressedImages(
            PDDocument doc,
            Map<String, List<ImageReference>> uniqueImages,
            double scaleFactor,
            float jpegQuality,
            boolean convertToGrayscale,
            CompressionStats stats)
            throws IOException {

        Map<String, PDImageXObject> compressedVersions = new HashMap<>();

        // Process each unique image exactly once
        for (Entry<String, List<ImageReference>> entry : uniqueImages.entrySet()) {
            String imageHash = entry.getKey();
            List<ImageReference> references = entry.getValue();

            if (references.isEmpty()) continue;

            // Get the first instance of this image
            PDImageXObject originalImage = getOriginalImage(doc, references.get(0));

            // Track original size
            int originalSize = (int) originalImage.getCOSObject().getLength();
            stats.totalOriginalBytes += originalSize;

            // Process this unique image
            PDImageXObject compressedImage =
                    compressImage(
                            doc,
                            originalImage,
                            originalSize,
                            scaleFactor,
                            jpegQuality,
                            convertToGrayscale);

            if (compressedImage != null) {
                // Store the compressed version in our map
                compressedVersions.put(imageHash, compressedImage);
                stats.compressedImages++;

                // Update compression stats
                int compressedSize = (int) compressedImage.getCOSObject().getLength();
                stats.totalCompressedBytes += compressedSize * references.size();

                double reductionPercentage = 100.0 - ((compressedSize * 100.0) / originalSize);
                log.info(
                        "Image hash {}: Compressed from {} to {} (reduced by {}%)",
                        imageHash,
                        GeneralUtils.formatBytes(originalSize),
                        GeneralUtils.formatBytes(compressedSize),
                        String.format("%.1f", reductionPercentage));
            } else {
                log.info("Image hash {}: Not suitable for compression, skipping", imageHash);
                stats.totalCompressedBytes += originalSize * references.size();
                stats.skippedImages++;
            }
        }

        return compressedVersions;
    }
