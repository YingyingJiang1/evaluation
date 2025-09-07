    public File convertToPdf(MultipartFile inputFile) throws IOException, InterruptedException {
        // Check for valid file extension
        String originalFilename = Filenames.toSimpleFileName(inputFile.getOriginalFilename());
        if (originalFilename == null
                || !isValidFileExtension(FilenameUtils.getExtension(originalFilename))) {
            throw new IllegalArgumentException("Invalid file extension");
        }

        // Save the uploaded file to a temporary location
        Path tempInputFile =
                Files.createTempFile("input_", "." + FilenameUtils.getExtension(originalFilename));
        inputFile.transferTo(tempInputFile);

        // Prepare the output file path
        Path tempOutputFile = Files.createTempFile("output_", ".pdf");

        try {
            // Run the LibreOffice command
            List<String> command =
                    new ArrayList<>(
                            Arrays.asList(
                                    runtimePathConfig.getUnoConvertPath(),
                                    "--port",
                                    "2003",
                                    "--convert-to",
                                    "pdf",
                                    tempInputFile.toString(),
                                    tempOutputFile.toString()));
            ProcessExecutorResult returnCode =
                    ProcessExecutor.getInstance(ProcessExecutor.Processes.LIBRE_OFFICE)
                            .runCommandWithOutputHandling(command);

            // Read the converted PDF file
            return tempOutputFile.toFile();
        } finally {
            // Clean up the temporary files
            if (tempInputFile != null) Files.deleteIfExists(tempInputFile);
        }
    }
