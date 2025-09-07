    public ResponseEntity<byte[]> processPdfToMarkdown(MultipartFile inputFile)
            throws IOException, InterruptedException {
        if (!"application/pdf".equals(inputFile.getContentType())) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }

        MutableDataSet options =
                new MutableDataSet()
                        .set(
                                FlexmarkHtmlConverter.MAX_BLANK_LINES,
                                2) // Control max consecutive blank lines
                        .set(
                                FlexmarkHtmlConverter.MAX_TRAILING_BLANK_LINES,
                                1) // Control trailing blank lines
                        .set(
                                FlexmarkHtmlConverter.SETEXT_HEADINGS,
                                true) // Use Setext headings for h1 and h2
                        .set(
                                FlexmarkHtmlConverter.OUTPUT_UNKNOWN_TAGS,
                                false) // Don't output HTML for unknown tags
                        .set(
                                FlexmarkHtmlConverter.TYPOGRAPHIC_QUOTES,
                                true) // Convert quotation marks
                        .set(
                                FlexmarkHtmlConverter.BR_AS_PARA_BREAKS,
                                true) // Convert <br> to paragraph breaks
                        .set(FlexmarkHtmlConverter.CODE_INDENT, "    "); // Indent for code blocks

        FlexmarkHtmlConverter htmlToMarkdownConverter =
                FlexmarkHtmlConverter.builder(options).build();

        String originalPdfFileName = Filenames.toSimpleFileName(inputFile.getOriginalFilename());
        String pdfBaseName = originalPdfFileName;
        if (originalPdfFileName.contains(".")) {
            pdfBaseName = originalPdfFileName.substring(0, originalPdfFileName.lastIndexOf('.'));
        }

        Path tempInputFile = null;
        Path tempOutputDir = null;
        byte[] fileBytes;
        String fileName = "temp.file";

        try {
            tempInputFile = Files.createTempFile("input_", ".pdf");
            inputFile.transferTo(tempInputFile);
            tempOutputDir = Files.createTempDirectory("output_");

            List<String> command =
                    new ArrayList<>(
                            Arrays.asList(
                                    "pdftohtml",
                                    "-s",
                                    "-noframes",
                                    "-c",
                                    tempInputFile.toString(),
                                    pdfBaseName));

            ProcessExecutorResult returnCode =
                    ProcessExecutor.getInstance(ProcessExecutor.Processes.PDFTOHTML)
                            .runCommandWithOutputHandling(command, tempOutputDir.toFile());
            // Process HTML files to Markdown
            File[] outputFiles = Objects.requireNonNull(tempOutputDir.toFile().listFiles());
            List<File> markdownFiles = new ArrayList<>();

            // Convert HTML files to Markdown
            for (File outputFile : outputFiles) {
                if (outputFile.getName().endsWith(".html")) {
                    String html = Files.readString(outputFile.toPath());
                    String markdown = htmlToMarkdownConverter.convert(html);

                    String mdFileName = outputFile.getName().replace(".html", ".md");
                    File mdFile = new File(tempOutputDir.toFile(), mdFileName);
                    Files.writeString(mdFile.toPath(), markdown);
                    markdownFiles.add(mdFile);
                }
            }

            // If there's only one markdown file, return it directly
            if (markdownFiles.size() == 1) {
                fileName = pdfBaseName + ".md";
                fileBytes = Files.readAllBytes(markdownFiles.get(0).toPath());
            } else {
                // Multiple files - create a zip
                fileName = pdfBaseName + "ToMarkdown.zip";
                ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();

                try (ZipOutputStream zipOutputStream = new ZipOutputStream(byteArrayOutputStream)) {
                    // Add markdown files
                    for (File mdFile : markdownFiles) {
                        ZipEntry mdEntry = new ZipEntry(mdFile.getName());
                        zipOutputStream.putNextEntry(mdEntry);
                        Files.copy(mdFile.toPath(), zipOutputStream);
                        zipOutputStream.closeEntry();
                    }

                    // Add images and other assets
                    for (File file : outputFiles) {
                        if (!file.getName().endsWith(".html") && !file.getName().endsWith(".md")) {
                            ZipEntry assetEntry = new ZipEntry(file.getName());
                            zipOutputStream.putNextEntry(assetEntry);
                            Files.copy(file.toPath(), zipOutputStream);
                            zipOutputStream.closeEntry();
                        }
                    }
                }

                fileBytes = byteArrayOutputStream.toByteArray();
            }

        } finally {
            if (tempInputFile != null) Files.deleteIfExists(tempInputFile);
            if (tempOutputDir != null) FileUtils.deleteDirectory(tempOutputDir.toFile());
        }
        return WebResponseUtils.bytesToWebResponse(
                fileBytes, fileName, MediaType.APPLICATION_OCTET_STREAM);
    }
