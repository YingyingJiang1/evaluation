    @PostMapping(value = "/document-properties", consumes = "multipart/form-data")
    @Operation(
            summary = "Get PDF document properties",
            description = "Returns title, author, subject, etc. Input:PDF Output:JSON Type:SISO")
    public Map<String, String> getDocumentProperties(@ModelAttribute PDFFile file)
            throws IOException {
        // Load the document in read-only mode to prevent modifications and ensure the integrity of
        // the original file.
        try (PDDocument document = pdfDocumentFactory.load(file.getFileInput(), true)) {
            PDDocumentInformation info = document.getDocumentInformation();
            Map<String, String> properties = new HashMap<>();
            properties.put("title", info.getTitle());
            properties.put("author", info.getAuthor());
            properties.put("subject", info.getSubject());
            properties.put("keywords", info.getKeywords());
            properties.put("creator", info.getCreator());
            properties.put("producer", info.getProducer());
            properties.put("creationDate", info.getCreationDate().toString());
            properties.put("modificationDate", info.getModificationDate().toString());
            return properties;
        }
    }
