    @PostMapping(value = "/overlay-pdfs", consumes = "multipart/form-data")
    @Operation(
            summary = "Overlay PDF files in various modes",
            description =
                    "Overlay PDF files onto a base PDF with different modes: Sequential,"
                            + " Interleaved, or Fixed Repeat. Input:PDF Output:PDF Type:MIMO")
    public ResponseEntity<byte[]> overlayPdfs(@ModelAttribute OverlayPdfsRequest request)
            throws IOException {
        MultipartFile baseFile = request.getFileInput();
        int overlayPos = request.getOverlayPosition();

        MultipartFile[] overlayFiles = request.getOverlayFiles();
        File[] overlayPdfFiles = new File[overlayFiles.length];
        List<File> tempFiles = new ArrayList<>(); // List to keep track of temporary files

        try {
            for (int i = 0; i < overlayFiles.length; i++) {
                overlayPdfFiles[i] = GeneralUtils.multipartToFile(overlayFiles[i]);
            }

            String mode = request.getOverlayMode(); // "SequentialOverlay", "InterleavedOverlay",
            // "FixedRepeatOverlay"
            int[] counts = request.getCounts(); // Used for FixedRepeatOverlay mode

            try (PDDocument basePdf = pdfDocumentFactory.load(baseFile);
                    Overlay overlay = new Overlay()) {
                Map<Integer, String> overlayGuide =
                        prepareOverlayGuide(
                                basePdf.getNumberOfPages(),
                                overlayPdfFiles,
                                mode,
                                counts,
                                tempFiles);

                overlay.setInputPDF(basePdf);
                if (overlayPos == 0) {
                    overlay.setOverlayPosition(Overlay.Position.FOREGROUND);
                } else {
                    overlay.setOverlayPosition(Overlay.Position.BACKGROUND);
                }

                ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
                overlay.overlay(overlayGuide).save(outputStream);
                byte[] data = outputStream.toByteArray();
                String outputFilename =
                        Filenames.toSimpleFileName(baseFile.getOriginalFilename())
                                        .replaceFirst("[.][^.]+$", "")
                                + "_overlayed.pdf"; // Remove file extension and append .pdf

                return WebResponseUtils.bytesToWebResponse(
                        data, outputFilename, MediaType.APPLICATION_PDF);
            }
        } finally {
            for (File overlayPdfFile : overlayPdfFiles) {
                if (overlayPdfFile != null) {
                    Files.deleteIfExists(overlayPdfFile.toPath());
                }
            }
            for (File tempFile : tempFiles) { // Delete temporary files
                if (tempFile != null) {
                    Files.deleteIfExists(tempFile.toPath());
                }
            }
        }
    }
