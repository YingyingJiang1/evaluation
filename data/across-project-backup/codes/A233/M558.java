    @PostMapping(consumes = "multipart/form-data", value = "/add-stamp")
    @Operation(
            summary = "Add stamp to a PDF file",
            description =
                    "This endpoint adds a stamp to a given PDF file. Users can specify the stamp"
                            + " type (text or image), rotation, opacity, width spacer, and height"
                            + " spacer. Input:PDF Output:PDF Type:SISO")
    public ResponseEntity<byte[]> addStamp(@ModelAttribute AddStampRequest request)
            throws IOException, Exception {
        MultipartFile pdfFile = request.getFileInput();
        String pdfFileName = pdfFile.getOriginalFilename();
        if (pdfFileName.contains("..") || pdfFileName.startsWith("/")) {
            throw new IllegalArgumentException("Invalid PDF file path");
        }
        
        String stampType = request.getStampType();
        String stampText = request.getStampText();
        MultipartFile stampImage = request.getStampImage();
        String stampImageName = stampImage.getOriginalFilename();
        if (stampImageName.contains("..") || stampImageName.startsWith("/")) {
            throw new IllegalArgumentException("Invalid stamp image file path");
        }
        String alphabet = request.getAlphabet();
        float fontSize = request.getFontSize();
        float rotation = request.getRotation();
        float opacity = request.getOpacity();
        int position = request.getPosition(); // Updated to use 1-9 positioning logic
        float overrideX = request.getOverrideX(); // New field for X override
        float overrideY = request.getOverrideY(); // New field for Y override

        String customColor = request.getCustomColor();
        float marginFactor;

        switch (request.getCustomMargin().toLowerCase()) {
            case "small":
                marginFactor = 0.02f;
                break;
            case "medium":
                marginFactor = 0.035f;
                break;
            case "large":
                marginFactor = 0.05f;
                break;
            case "x-large":
                marginFactor = 0.075f;
                break;
            default:
                marginFactor = 0.035f;
                break;
        }

        // Load the input PDF
        PDDocument document = pdfDocumentFactory.load(pdfFile);

        List<Integer> pageNumbers = request.getPageNumbersList(document, true);

        for (int pageIndex : pageNumbers) {
            int zeroBasedIndex = pageIndex - 1;
            if (zeroBasedIndex >= 0 && zeroBasedIndex < document.getNumberOfPages()) {
                PDPage page = document.getPage(zeroBasedIndex);
                PDRectangle pageSize = page.getMediaBox();
                float margin = marginFactor * (pageSize.getWidth() + pageSize.getHeight()) / 2;

                PDPageContentStream contentStream =
                        new PDPageContentStream(
                                document, page, PDPageContentStream.AppendMode.APPEND, true, true);

                PDExtendedGraphicsState graphicsState = new PDExtendedGraphicsState();
                graphicsState.setNonStrokingAlphaConstant(opacity);
                contentStream.setGraphicsStateParameters(graphicsState);

                if ("text".equalsIgnoreCase(stampType)) {
                    addTextStamp(
                            contentStream,
                            stampText,
                            document,
                            page,
                            rotation,
                            position,
                            fontSize,
                            alphabet,
                            overrideX,
                            overrideY,
                            margin,
                            customColor);
                } else if ("image".equalsIgnoreCase(stampType)) {
                    addImageStamp(
                            contentStream,
                            stampImage,
                            document,
                            page,
                            rotation,
                            position,
                            fontSize,
                            overrideX,
                            overrideY,
                            margin);
                }

                contentStream.close();
            }
        }
        return WebResponseUtils.pdfDocToWebResponse(
                document,
                Filenames.toSimpleFileName(pdfFile.getOriginalFilename())
                                .replaceFirst("[.][^.]+$", "")
                        + "_stamped.pdf");
    }
