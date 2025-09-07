    @PostMapping(consumes = "multipart/form-data", value = "/update-metadata")
    @Operation(
            summary = "Update metadata of a PDF file",
            description =
                    "This endpoint allows you to update the metadata of a given PDF file. You can"
                            + " add, modify, or delete standard and custom metadata fields. Input:PDF"
                            + " Output:PDF Type:SISO")
    public ResponseEntity<byte[]> metadata(@ModelAttribute MetadataRequest request)
            throws IOException {

        // Extract PDF file from the request object
        MultipartFile pdfFile = request.getFileInput();

        // Extract metadata information
        boolean deleteAll = Boolean.TRUE.equals(request.getDeleteAll());
        String author = request.getAuthor();
        String creationDate = request.getCreationDate();
        String creator = request.getCreator();
        String keywords = request.getKeywords();
        String modificationDate = request.getModificationDate();
        String producer = request.getProducer();
        String subject = request.getSubject();
        String title = request.getTitle();
        String trapped = request.getTrapped();

        // Extract additional custom parameters
        Map<String, String> allRequestParams = request.getAllRequestParams();
        if (allRequestParams == null) {
            allRequestParams = new java.util.HashMap<String, String>();
        }
        // Load the PDF file into a PDDocument
        PDDocument document = pdfDocumentFactory.load(pdfFile, true);

        // Get the document information from the PDF
        PDDocumentInformation info = document.getDocumentInformation();

        // Check if each metadata value is "undefined" and set it to null if it is
        author = checkUndefined(author);
        creationDate = checkUndefined(creationDate);
        creator = checkUndefined(creator);
        keywords = checkUndefined(keywords);
        modificationDate = checkUndefined(modificationDate);
        producer = checkUndefined(producer);
        subject = checkUndefined(subject);
        title = checkUndefined(title);
        trapped = checkUndefined(trapped);

        // If the "deleteAll" flag is set, remove all metadata from the document
        // information
        if (deleteAll) {
            for (String key : info.getMetadataKeys()) {
                info.setCustomMetadataValue(key, null);
            }
            // Remove metadata from the PDF history
            document.getDocumentCatalog().getCOSObject().removeItem(COSName.getPDFName("Metadata"));
            document.getDocumentCatalog()
                    .getCOSObject()
                    .removeItem(COSName.getPDFName("PieceInfo"));
            author = null;
            creationDate = null;
            creator = null;
            keywords = null;
            modificationDate = null;
            producer = null;
            subject = null;
            title = null;
            trapped = null;
        } else {
            // Iterate through the request parameters and set the metadata values
            for (Entry<String, String> entry : allRequestParams.entrySet()) {
                String key = entry.getKey();
                // Check if the key is a standard metadata key
                if (!"Author".equalsIgnoreCase(key)
                        && !"CreationDate".equalsIgnoreCase(key)
                        && !"Creator".equalsIgnoreCase(key)
                        && !"Keywords".equalsIgnoreCase(key)
                        && !"modificationDate".equalsIgnoreCase(key)
                        && !"Producer".equalsIgnoreCase(key)
                        && !"Subject".equalsIgnoreCase(key)
                        && !"Title".equalsIgnoreCase(key)
                        && !"Trapped".equalsIgnoreCase(key)
                        && !key.contains("customKey")
                        && !key.contains("customValue")) {
                    info.setCustomMetadataValue(key, entry.getValue());
                } else if (key.contains("customKey")) {
                    int number = Integer.parseInt(key.replaceAll("\\D", ""));
                    String customKey = entry.getValue();
                    String customValue = allRequestParams.get("customValue" + number);
                    info.setCustomMetadataValue(customKey, customValue);
                }
            }
        }
        if (creationDate != null && creationDate.length() > 0) {
            Calendar creationDateCal = Calendar.getInstance();
            try {
                creationDateCal.setTime(
                        new SimpleDateFormat("yyyy/MM/dd HH:mm:ss").parse(creationDate));
            } catch (ParseException e) {
                log.error("exception", e);
            }
            info.setCreationDate(creationDateCal);
        } else {
            info.setCreationDate(null);
        }
        if (modificationDate != null && modificationDate.length() > 0) {
            Calendar modificationDateCal = Calendar.getInstance();
            try {
                modificationDateCal.setTime(
                        new SimpleDateFormat("yyyy/MM/dd HH:mm:ss").parse(modificationDate));
            } catch (ParseException e) {
                log.error("exception", e);
            }
            info.setModificationDate(modificationDateCal);
        } else {
            info.setModificationDate(null);
        }
        info.setCreator(creator);
        info.setKeywords(keywords);
        info.setAuthor(author);
        info.setProducer(producer);
        info.setSubject(subject);
        info.setTitle(title);
        info.setTrapped(trapped);

        document.setDocumentInformation(info);
        return WebResponseUtils.pdfDocToWebResponse(
                document,
                Filenames.toSimpleFileName(pdfFile.getOriginalFilename())
                                .replaceFirst("[.][^.]+$", "")
                        + "_metadata.pdf");
    }
