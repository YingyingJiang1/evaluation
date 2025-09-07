    private static byte[] attachFilesToPdf(
            byte[] pdfBytes,
            List<EmailAttachment> attachments,
            CustomPDFDocumentFactory pdfDocumentFactory)
            throws IOException {
        try (PDDocument document = pdfDocumentFactory.load(pdfBytes);
                ByteArrayOutputStream outputStream = new ByteArrayOutputStream()) {

            if (attachments == null || attachments.isEmpty()) {
                document.save(outputStream);
                return outputStream.toByteArray();
            }

            List<String> embeddedFiles = new ArrayList<>();

            // Set up the embedded files name tree once
            if (document.getDocumentCatalog().getNames() == null) {
                document.getDocumentCatalog()
                        .setNames(new PDDocumentNameDictionary(document.getDocumentCatalog()));
            }

            PDDocumentNameDictionary names = document.getDocumentCatalog().getNames();
            if (names.getEmbeddedFiles() == null) {
                names.setEmbeddedFiles(new PDEmbeddedFilesNameTreeNode());
            }

            PDEmbeddedFilesNameTreeNode efTree = names.getEmbeddedFiles();
            Map<String, PDComplexFileSpecification> efMap = efTree.getNames();
            if (efMap == null) {
                efMap = new HashMap<>();
            }

            // Embed each attachment directly into the PDF
            for (EmailAttachment attachment : attachments) {
                if (attachment.getData() == null || attachment.getData().length == 0) {
                    continue;
                }

                try {
                    // Generate unique filename
                    String filename = attachment.getFilename();
                    if (filename == null || filename.trim().isEmpty()) {
                        filename = "attachment_" + System.currentTimeMillis();
                        if (attachment.getContentType() != null
                                && attachment.getContentType().contains("/")) {
                            String[] parts = attachment.getContentType().split("/");
                            if (parts.length > 1) {
                                filename += "." + parts[1];
                            }
                        }
                    }

                    // Ensure unique filename
                    String uniqueFilename = getUniqueFilename(filename, embeddedFiles, efMap);

                    // Create embedded file
                    PDEmbeddedFile embeddedFile =
                            new PDEmbeddedFile(
                                    document, new ByteArrayInputStream(attachment.getData()));
                    embeddedFile.setSize(attachment.getData().length);
                    embeddedFile.setCreationDate(new GregorianCalendar());

                    // Create file specification
                    PDComplexFileSpecification fileSpec = new PDComplexFileSpecification();
                    fileSpec.setFile(uniqueFilename);
                    fileSpec.setEmbeddedFile(embeddedFile);
                    if (attachment.getContentType() != null) {
                        embeddedFile.setSubtype(attachment.getContentType());
                        fileSpec.setFileDescription("Email attachment: " + uniqueFilename);
                    }

                    // Add to the map (but don't set it yet)
                    efMap.put(uniqueFilename, fileSpec);
                    embeddedFiles.add(uniqueFilename);

                    // Store the filename for annotation creation
                    attachment.setEmbeddedFilename(uniqueFilename);

                } catch (Exception e) {
                    // Log error but continue with other attachments
                    log.warn("Failed to embed attachment: {}", attachment.getFilename(), e);
                }
            }

            // Set the complete map once at the end
            if (!efMap.isEmpty()) {
                efTree.setNames(efMap);

                // Set catalog viewer preferences to automatically show attachments pane
                setCatalogViewerPreferences(document, PageMode.USE_ATTACHMENTS);
            }

            // Add attachment annotations to the first page for each embedded file
            if (!embeddedFiles.isEmpty()) {
                addAttachmentAnnotationsToDocument(document, attachments);
            }

            document.save(outputStream);
            return outputStream.toByteArray();
        }
    }
