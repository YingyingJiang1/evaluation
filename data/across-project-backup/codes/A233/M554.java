    @PostMapping(consumes = "multipart/form-data", value = "/show-javascript")
    @Operation(
            summary = "Grabs all JS from a PDF and returns a single JS file with all code",
            description = "desc. Input:PDF Output:JS Type:SISO")
    public ResponseEntity<byte[]> extractHeader(@ModelAttribute PDFFile file) throws Exception {
        MultipartFile inputFile = file.getFileInput();
        String script = "";

        try (PDDocument document = pdfDocumentFactory.load(inputFile)) {

            if (document.getDocumentCatalog() != null
                    && document.getDocumentCatalog().getNames() != null) {
                PDNameTreeNode<PDActionJavaScript> jsTree =
                        document.getDocumentCatalog().getNames().getJavaScript();

                if (jsTree != null) {
                    Map<String, PDActionJavaScript> jsEntries = jsTree.getNames();

                    for (Map.Entry<String, PDActionJavaScript> entry : jsEntries.entrySet()) {
                        String name = entry.getKey();
                        PDActionJavaScript jsAction = entry.getValue();
                        String jsCodeStr = jsAction.getAction();

                        script +=
                                "// File: "
                                        + Filenames.toSimpleFileName(
                                                inputFile.getOriginalFilename())
                                        + ", Script: "
                                        + name
                                        + "\n"
                                        + jsCodeStr
                                        + "\n";
                    }
                }
            }

            if (script.isEmpty()) {
                script =
                        "PDF '"
                                + Filenames.toSimpleFileName(inputFile.getOriginalFilename())
                                + "' does not contain Javascript";
            }

            return WebResponseUtils.bytesToWebResponse(
                    script.getBytes(StandardCharsets.UTF_8),
                    Filenames.toSimpleFileName(inputFile.getOriginalFilename()) + ".js",
                    MediaType.TEXT_PLAIN);
        }
    }
