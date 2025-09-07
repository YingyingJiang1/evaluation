    private void processWithOcrMyPdf(
            List<String> selectedLanguages,
            Boolean sidecar,
            Boolean deskew,
            Boolean clean,
            Boolean cleanFinal,
            String ocrType,
            String ocrRenderType,
            Boolean removeImagesAfter,
            Path tempInputFile,
            Path tempOutputFile,
            Path sidecarTextPath)
            throws IOException, InterruptedException {

        // Build OCRmyPDF command
        String languageOption = String.join("+", selectedLanguages);

        List<String> command =
                new ArrayList<>(
                        Arrays.asList(
                                "ocrmypdf",
                                "--verbose",
                                "2",
                                "--output-type",
                                "pdf",
                                "--pdf-renderer",
                                ocrRenderType));

        if (sidecar != null && sidecar && sidecarTextPath != null) {
            command.add("--sidecar");
            command.add(sidecarTextPath.toString());
        }

        if (deskew != null && deskew) {
            command.add("--deskew");
        }
        if (clean != null && clean) {
            command.add("--clean");
        }
        if (cleanFinal != null && cleanFinal) {
            command.add("--clean-final");
        }
        if (ocrType != null && !"".equals(ocrType)) {
            if ("skip-text".equals(ocrType)) {
                command.add("--skip-text");
            } else if ("force-ocr".equals(ocrType)) {
                command.add("--force-ocr");
            }
        }

        command.addAll(
                Arrays.asList(
                        "--language",
                        languageOption,
                        tempInputFile.toString(),
                        tempOutputFile.toString()));

        // Run CLI command
        ProcessExecutorResult result =
                ProcessExecutor.getInstance(ProcessExecutor.Processes.OCR_MY_PDF)
                        .runCommandWithOutputHandling(command);

        if (result.getRc() != 0
                && result.getMessages().contains("multiprocessing/synchronize.py")
                && result.getMessages().contains("OSError: [Errno 38] Function not implemented")) {
            command.add("--jobs");
            command.add("1");
            result =
                    ProcessExecutor.getInstance(ProcessExecutor.Processes.OCR_MY_PDF)
                            .runCommandWithOutputHandling(command);
        }

        if (result.getRc() != 0) {
            throw new IOException("OCRmyPDF failed with return code: " + result.getRc());
        }

        // Remove images from the OCR processed PDF if the flag is set to true
        if (removeImagesAfter != null && removeImagesAfter) {
            try (TempFile tempPdfWithoutImages = new TempFile(tempFileManager, "_no_images.pdf")) {
                List<String> gsCommand =
                        Arrays.asList(
                                "gs",
                                "-sDEVICE=pdfwrite",
                                "-dFILTERIMAGE",
                                "-o",
                                tempPdfWithoutImages.getPath().toString(),
                                tempOutputFile.toString());

                ProcessExecutor.getInstance(ProcessExecutor.Processes.GHOSTSCRIPT)
                        .runCommandWithOutputHandling(gsCommand);

                // Replace output file with version without images
                Files.copy(
                        tempPdfWithoutImages.getPath(),
                        tempOutputFile,
                        java.nio.file.StandardCopyOption.REPLACE_EXISTING);
            }
        }
    }
