    public static byte[] convertHtmlToPdf(
            String weasyprintPath,
            HTMLToPdfRequest request,
            byte[] fileBytes,
            String fileName,
            boolean disableSanitize,
            TempFileManager tempFileManager)
            throws IOException, InterruptedException {

        try (TempFile tempOutputFile = new TempFile(tempFileManager, ".pdf")) {
            try (TempFile tempInputFile =
                    new TempFile(
                            tempFileManager,
                            fileName.toLowerCase().endsWith(".html") ? ".html" : ".zip")) {

                if (fileName.toLowerCase().endsWith(".html")) {
                    String sanitizedHtml =
                            sanitizeHtmlContent(
                                    new String(fileBytes, StandardCharsets.UTF_8), disableSanitize);
                    Files.write(
                            tempInputFile.getPath(),
                            sanitizedHtml.getBytes(StandardCharsets.UTF_8));
                } else if (fileName.toLowerCase().endsWith(".zip")) {
                    Files.write(tempInputFile.getPath(), fileBytes);
                    sanitizeHtmlFilesInZip(
                            tempInputFile.getPath(), disableSanitize, tempFileManager);
                } else {
                    throw ExceptionUtils.createHtmlFileRequiredException();
                }

                List<String> command = new ArrayList<>();
                command.add(weasyprintPath);
                command.add("-e");
                command.add("utf-8");
                command.add("-v");
                command.add("--pdf-forms");
                command.add(tempInputFile.getAbsolutePath());
                command.add(tempOutputFile.getAbsolutePath());

                ProcessExecutorResult returnCode =
                        ProcessExecutor.getInstance(ProcessExecutor.Processes.WEASYPRINT)
                                .runCommandWithOutputHandling(command);

                byte[] pdfBytes = Files.readAllBytes(tempOutputFile.getPath());
                try {
                    return pdfBytes;
                } catch (Exception e) {
                    pdfBytes = Files.readAllBytes(tempOutputFile.getPath());
                    if (pdfBytes.length < 1) {
                        throw e;
                    }
                    return pdfBytes;
                }
            } // tempInputFile auto-closed
        } // tempOutputFile auto-closed
    }
