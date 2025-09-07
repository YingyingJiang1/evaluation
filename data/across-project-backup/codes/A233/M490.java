    @PostMapping(consumes = "multipart/form-data", value = "/url/pdf")
    @Operation(
            summary = "Convert a URL to a PDF",
            description =
                    "This endpoint fetches content from a URL and converts it to a PDF format."
                            + " Input:N/A Output:PDF Type:SISO")
    public ResponseEntity<byte[]> urlToPdf(@ModelAttribute UrlToPdfRequest request)
            throws IOException, InterruptedException {
        String URL = request.getUrlInput();

        if (!applicationProperties.getSystem().getEnableUrlToPDF()) {
            throw ExceptionUtils.createIllegalArgumentException(
                    "error.endpointDisabled", "This endpoint has been disabled by the admin");
        }
        // Validate the URL format
        if (!URL.matches("^https?://.*") || !GeneralUtils.isValidURL(URL)) {
            throw ExceptionUtils.createInvalidArgumentException(
                    "URL", "provided format is invalid");
        }

        // validate the URL is reachable
        if (!GeneralUtils.isURLReachable(URL)) {
            throw ExceptionUtils.createIllegalArgumentException(
                    "error.urlNotReachable", "URL is not reachable, please provide a valid URL");
        }

        Path tempOutputFile = null;
        PDDocument doc = null;
        try {
            // Prepare the output file path
            tempOutputFile = Files.createTempFile("output_", ".pdf");

            // Prepare the WeasyPrint command
            List<String> command = new ArrayList<>();
            command.add(runtimePathConfig.getWeasyPrintPath());
            command.add(URL);
            command.add("--pdf-forms");
            command.add(tempOutputFile.toString());

            ProcessExecutorResult returnCode =
                    ProcessExecutor.getInstance(ProcessExecutor.Processes.WEASYPRINT)
                            .runCommandWithOutputHandling(command);

            // Load the PDF using pdfDocumentFactory
            doc = pdfDocumentFactory.load(tempOutputFile.toFile());

            // Convert URL to a safe filename
            String outputFilename = convertURLToFileName(URL);

            return WebResponseUtils.pdfDocToWebResponse(doc, outputFilename);
        } finally {

            if (tempOutputFile != null) {
                try {
                    Files.deleteIfExists(tempOutputFile);
                } catch (IOException e) {
                    log.error("Error deleting temporary output file", e);
                }
            }
        }
    }
