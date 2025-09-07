    public Path compressImagesInPDF(
            Path pdfFile, double scaleFactor, float jpegQuality, boolean convertToGrayscale)
            throws Exception {
        Path newCompressedPDF = Files.createTempFile("compressedPDF", ".pdf");
        long originalFileSize = Files.size(pdfFile);
        log.info(
                "Starting image compression with scale factor: {}, JPEG quality: {}, grayscale: {} on file size: {}",
                scaleFactor,
                jpegQuality,
                convertToGrayscale,
                GeneralUtils.formatBytes(originalFileSize));

        try (PDDocument doc = pdfDocumentFactory.load(pdfFile)) {
            // Find all unique images in the document
            Map<String, List<ImageReference>> uniqueImages = findImages(doc);

            // Get statistics
            CompressionStats stats = new CompressionStats();
            stats.uniqueImagesCount = uniqueImages.size();
            calculateImageStats(uniqueImages, stats);

            // Create compressed versions of unique images
            Map<String, PDImageXObject> compressedVersions =
                    createCompressedImages(
                            doc, uniqueImages, scaleFactor, jpegQuality, convertToGrayscale, stats);

            // Replace all instances with compressed versions
            replaceImages(doc, uniqueImages, compressedVersions, stats);

            // Log compression statistics
            logCompressionStats(stats, originalFileSize);

            // Free memory before saving
            compressedVersions.clear();
            uniqueImages.clear();

            log.info("Saving compressed PDF to {}", newCompressedPDF.toString());
            doc.save(newCompressedPDF.toString());

            // Log overall file size reduction
            long compressedFileSize = Files.size(newCompressedPDF);
            double overallReduction = 100.0 - ((compressedFileSize * 100.0) / originalFileSize);
            log.info(
                    "Overall PDF compression: {} → {} (reduced by {}%)",
                    GeneralUtils.formatBytes(originalFileSize),
                    GeneralUtils.formatBytes(compressedFileSize),
                    String.format("%.1f", overallReduction));
            return newCompressedPDF;
        }
    }
