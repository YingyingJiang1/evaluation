    private void processWithTesseract(
            List<String> selectedLanguages, String ocrType, Path tempInputFile, Path tempOutputFile)
            throws IOException, InterruptedException {

        // Create temp directory for Tesseract processing
        try (TempDirectory tempDir = new TempDirectory(tempFileManager)) {
            File tempOutputDir = new File(tempDir.getPath().toFile(), "output");
            File tempImagesDir = new File(tempDir.getPath().toFile(), "images");
            File finalOutputFile = new File(tempDir.getPath().toFile(), "final_output.pdf");

            // Create directories
            tempOutputDir.mkdirs();
            tempImagesDir.mkdirs();

            PDFMergerUtility merger = new PDFMergerUtility();
            merger.setDestinationFileName(finalOutputFile.toString());

            try (PDDocument document = pdfDocumentFactory.load(tempInputFile.toFile())) {
                PDFRenderer pdfRenderer = new PDFRenderer(document);
                int pageCount = document.getNumberOfPages();

                for (int pageNum = 0; pageNum < pageCount; pageNum++) {
                    PDPage page = document.getPage(pageNum);
                    boolean hasText = false;

                    // Check for existing text
                    try (PDDocument tempDoc = new PDDocument()) {
                        tempDoc.addPage(page);
                        PDFTextStripper stripper = new PDFTextStripper();
                        hasText = !stripper.getText(tempDoc).trim().isEmpty();
                    }

                    boolean shouldOcr =
                            switch (ocrType) {
                                case "skip-text" -> !hasText;
                                case "force-ocr" -> true;
                                default -> true;
                            };

                    File pageOutputPath =
                            new File(tempOutputDir, String.format("page_%d.pdf", pageNum));

                    if (shouldOcr) {
                        // Convert page to image
                        BufferedImage image = pdfRenderer.renderImageWithDPI(pageNum, 300);
                        File imagePath =
                                new File(tempImagesDir, String.format("page_%d.png", pageNum));
                        ImageIO.write(image, "png", imagePath);

                        // Build OCR command
                        List<String> command = new ArrayList<>();
                        command.add("tesseract");
                        command.add(imagePath.toString());
                        command.add(
                                new File(tempOutputDir, String.format("page_%d", pageNum))
                                        .toString());
                        command.add("-l");
                        command.add(String.join("+", selectedLanguages));
                        command.add("pdf"); // Always output PDF

                        ProcessExecutorResult result =
                                ProcessExecutor.getInstance(ProcessExecutor.Processes.TESSERACT)
                                        .runCommandWithOutputHandling(command);

                        if (result.getRc() != 0) {
                            throw ExceptionUtils.createRuntimeException(
                                    "error.commandFailed",
                                    "{0} command failed with exit code: {1}",
                                    null,
                                    "Tesseract",
                                    result.getRc());
                        }

                        // Add OCR'd PDF to merger
                        merger.addSource(pageOutputPath);
                    } else {
                        // Save original page without OCR
                        try (PDDocument pageDoc = new PDDocument()) {
                            pageDoc.addPage(page);
                            pageDoc.save(pageOutputPath);
                            merger.addSource(pageOutputPath);
                        }
                    }
                }
            }

            // Merge all pages into final PDF
            merger.mergeDocuments(null);

            // Copy final output to the expected location
            Files.copy(
                    finalOutputFile.toPath(),
                    tempOutputFile,
                    java.nio.file.StandardCopyOption.REPLACE_EXISTING);
        }
    }
