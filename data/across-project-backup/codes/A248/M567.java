    @PostMapping(value = "/scanner-effect", consumes = "multipart/form-data")
    @Operation(
            summary = "Apply scanner effect to PDF",
            description =
                    "Applies various effects to simulate a scanned document, including rotation, noise, and edge softening. Input:PDF Output:PDF Type:SISO")
    public ResponseEntity<byte[]> scannerEffect(@Valid @ModelAttribute ScannerEffectRequest request)
            throws IOException {
        MultipartFile file = request.getFileInput();

        // Apply preset first if needed
        if (!request.isAdvancedEnabled()) {
            switch (request.getQuality()) {
                case high -> request.applyHighQualityPreset();
                case medium -> request.applyMediumQualityPreset();
                case low -> request.applyLowQualityPreset();
            }
        }

        // Extract values after preset application
        int baseRotation = request.getRotationValue() + request.getRotate();
        int rotateVariance = request.getRotateVariance();
        int borderPx = request.getBorder();
        float brightness = request.getBrightness();
        float contrast = request.getContrast();
        float blur = request.getBlur();
        float noise = request.getNoise();
        boolean yellowish = request.isYellowish();
        int resolution = request.getResolution();
        ScannerEffectRequest.Colorspace colorspace = request.getColorspace();

        try (PDDocument document = pdfDocumentFactory.load(file)) {
            PDDocument outputDocument = new PDDocument();
            PDFRenderer pdfRenderer = new PDFRenderer(document);

            for (int i = 0; i < document.getNumberOfPages(); i++) {
                // Get page dimensions to calculate safe resolution
                PDRectangle pageSize = document.getPage(i).getMediaBox();
                float pageWidthPts = pageSize.getWidth();
                float pageHeightPts = pageSize.getHeight();

                // Calculate what the image dimensions would be at the requested resolution
                int projectedWidth = (int) Math.ceil(pageWidthPts * resolution / 72.0);
                int projectedHeight = (int) Math.ceil(pageHeightPts * resolution / 72.0);
                long projectedPixels = (long) projectedWidth * projectedHeight;

                // Calculate safe resolution that stays within limits
                int safeResolution = resolution;
                if (projectedWidth > MAX_IMAGE_WIDTH
                        || projectedHeight > MAX_IMAGE_HEIGHT
                        || projectedPixels > MAX_IMAGE_PIXELS) {
                    double widthScale = (double) MAX_IMAGE_WIDTH / projectedWidth;
                    double heightScale = (double) MAX_IMAGE_HEIGHT / projectedHeight;
                    double pixelScale = Math.sqrt((double) MAX_IMAGE_PIXELS / projectedPixels);
                    double minScale = Math.min(Math.min(widthScale, heightScale), pixelScale);
                    safeResolution = (int) Math.max(72, resolution * minScale);

                    log.warn(
                            "Page {} would be too large at {}dpi ({}x{} pixels). Reducing to {}dpi",
                            i + 1,
                            resolution,
                            projectedWidth,
                            projectedHeight,
                            safeResolution);
                }

                // Render page to image with safe resolution
                BufferedImage image = pdfRenderer.renderImageWithDPI(i, safeResolution);

                log.debug(
                        "Processing page {} with dimensions {}x{} ({} pixels) at {}dpi",
                        i + 1,
                        image.getWidth(),
                        image.getHeight(),
                        (long) image.getWidth() * image.getHeight(),
                        safeResolution);

                // 1. Convert to grayscale or keep color
                BufferedImage processed;
                if (colorspace == ScannerEffectRequest.Colorspace.grayscale) {
                    processed =
                            new BufferedImage(
                                    image.getWidth(),
                                    image.getHeight(),
                                    BufferedImage.TYPE_INT_RGB);
                    Graphics2D gGray = processed.createGraphics();
                    gGray.setColor(Color.BLACK);
                    gGray.fillRect(0, 0, image.getWidth(), image.getHeight());
                    gGray.drawImage(image, 0, 0, null);
                    gGray.dispose();

                    // Convert to grayscale manually
                    for (int y = 0; y < processed.getHeight(); y++) {
                        for (int x = 0; x < processed.getWidth(); x++) {
                            int rgb = processed.getRGB(x, y);
                            int r = (rgb >> 16) & 0xFF;
                            int g = (rgb >> 8) & 0xFF;
                            int b = rgb & 0xFF;
                            int gray = (r + g + b) / 3;
                            int grayRGB = (gray << 16) | (gray << 8) | gray;
                            processed.setRGB(x, y, grayRGB);
                        }
                    }
                } else {
                    processed =
                            new BufferedImage(
                                    image.getWidth(),
                                    image.getHeight(),
                                    BufferedImage.TYPE_INT_RGB);
                    Graphics2D gCol = processed.createGraphics();
                    gCol.drawImage(image, 0, 0, null);
                    gCol.dispose();
                }

                // 2. Add border with randomized grey gradient
                int baseW = processed.getWidth() + 2 * borderPx;
                int baseH = processed.getHeight() + 2 * borderPx;
                boolean vertical = RANDOM.nextBoolean();
                float startGrey = 0.6f + 0.3f * RANDOM.nextFloat();
                float endGrey = 0.6f + 0.3f * RANDOM.nextFloat();
                Color startColor =
                        new Color(
                                Math.round(startGrey * 255),
                                Math.round(startGrey * 255),
                                Math.round(startGrey * 255));
                Color endColor =
                        new Color(
                                Math.round(endGrey * 255),
                                Math.round(endGrey * 255),
                                Math.round(endGrey * 255));
                BufferedImage composed = new BufferedImage(baseW, baseH, processed.getType());
                Graphics2D gBg = composed.createGraphics();
                for (int y = 0; y < baseH; y++) {
                    for (int x = 0; x < baseW; x++) {
                        float frac = vertical ? (float) y / (baseH - 1) : (float) x / (baseW - 1);
                        int r =
                                Math.round(
                                        startColor.getRed()
                                                + (endColor.getRed() - startColor.getRed()) * frac);
                        int g =
                                Math.round(
                                        startColor.getGreen()
                                                + (endColor.getGreen() - startColor.getGreen())
                                                        * frac);
                        int b =
                                Math.round(
                                        startColor.getBlue()
                                                + (endColor.getBlue() - startColor.getBlue())
                                                        * frac);
                        composed.setRGB(x, y, new Color(r, g, b).getRGB());
                    }
                }
                gBg.drawImage(processed, borderPx, borderPx, null);
                gBg.dispose();

                // 3. Rotate the entire composed image
                double pageRotation = baseRotation;
                if (baseRotation != 0 || rotateVariance != 0) {
                    pageRotation += (RANDOM.nextDouble() * 2 - 1) * rotateVariance;
                }

                BufferedImage rotated;
                int w = composed.getWidth();
                int h = composed.getHeight();
                int rotW = w;
                int rotH = h;

                // Skip rotation entirely if no rotation is needed
                if (pageRotation == 0) {
                    rotated = composed;
                } else {
                    double radians = Math.toRadians(pageRotation);
                    double sin = Math.abs(Math.sin(radians));
                    double cos = Math.abs(Math.cos(radians));
                    rotW = (int) Math.floor(w * cos + h * sin);
                    rotH = (int) Math.floor(h * cos + w * sin);
                    BufferedImage rotatedBg = new BufferedImage(rotW, rotH, composed.getType());
                    Graphics2D gBgRot = rotatedBg.createGraphics();
                    for (int y = 0; y < rotH; y++) {
                        for (int x = 0; x < rotW; x++) {
                            float frac = vertical ? (float) y / (rotH - 1) : (float) x / (rotW - 1);
                            int r =
                                    Math.round(
                                            startColor.getRed()
                                                    + (endColor.getRed() - startColor.getRed())
                                                            * frac);
                            int g =
                                    Math.round(
                                            startColor.getGreen()
                                                    + (endColor.getGreen() - startColor.getGreen())
                                                            * frac);
                            int b =
                                    Math.round(
                                            startColor.getBlue()
                                                    + (endColor.getBlue() - startColor.getBlue())
                                                            * frac);
                            rotatedBg.setRGB(x, y, new Color(r, g, b).getRGB());
                        }
                    }
                    gBgRot.dispose();
                    rotated = new BufferedImage(rotW, rotH, composed.getType());
                    Graphics2D g2d = rotated.createGraphics();
                    g2d.drawImage(rotatedBg, 0, 0, null);
                    AffineTransform at = new AffineTransform();
                    at.translate((rotW - w) / 2.0, (rotH - h) / 2.0);
                    at.rotate(radians, w / 2.0, h / 2.0);
                    g2d.setRenderingHint(
                            RenderingHints.KEY_INTERPOLATION,
                            RenderingHints.VALUE_INTERPOLATION_BICUBIC);
                    g2d.setRenderingHint(
                            RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
                    g2d.setRenderingHint(
                            RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                    g2d.drawImage(composed, at, null);
                    g2d.dispose();
                }

                // 4. Scale and center the rotated image to cover the original page size
                PDRectangle origPageSize = document.getPage(i).getMediaBox();
                float origW = origPageSize.getWidth();
                float origH = origPageSize.getHeight();
                float scale = Math.max(origW / rotW, origH / rotH);
                float drawW = rotW * scale;
                float drawH = rotH * scale;
                float offsetX = (origW - drawW) / 2f;
                float offsetY = (origH - drawH) / 2f;

                // 5. Apply adaptive blur and edge softening
                BufferedImage softened =
                        softenEdges(
                                rotated,
                                Math.max(10, Math.round(Math.min(rotW, rotH) * 0.02f)),
                                startColor,
                                endColor,
                                vertical);
                BufferedImage blurred = applyGaussianBlur(softened, blur);

                // 6. Adjust brightness and contrast
                BufferedImage adjusted = adjustBrightnessContrast(blurred, brightness, contrast);

                // 7. Add noise and yellowish effect to the content
                if (yellowish) {
                    applyYellowishEffect(adjusted);
                }
                addGaussianNoise(adjusted, noise);

                // 8. Write to PDF
                PDPage newPage = new PDPage(new PDRectangle(origW, origH));
                outputDocument.addPage(newPage);
                try (PDPageContentStream contentStream =
                        new PDPageContentStream(outputDocument, newPage)) {
                    PDImageXObject pdImage =
                            LosslessFactory.createFromImage(outputDocument, adjusted);
                    contentStream.drawImage(pdImage, offsetX, offsetY, drawW, drawH);
                }
            }

            // Save to byte array
            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            outputDocument.save(outputStream);
            outputDocument.close();

            String outputFilename =
                    Filenames.toSimpleFileName(file.getOriginalFilename())
                                    .replaceFirst("[.][^.]+$", "")
                            + "_scanner_effect.pdf";

            return WebResponseUtils.bytesToWebResponse(
                    outputStream.toByteArray(), outputFilename, MediaType.APPLICATION_PDF);
        }
    }
