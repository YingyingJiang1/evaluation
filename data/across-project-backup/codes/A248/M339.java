    public static byte[] convertEmlToPdf(
            String weasyprintPath,
            EmlToPdfRequest request,
            byte[] emlBytes,
            String fileName,
            boolean disableSanitize,
            stirling.software.common.service.CustomPDFDocumentFactory pdfDocumentFactory,
            TempFileManager tempFileManager)
            throws IOException, InterruptedException {

        validateEmlInput(emlBytes);

        try {
            // Generate HTML representation
            EmailContent emailContent = null;
            String htmlContent;

            if (isJakartaMailAvailable()) {
                emailContent = extractEmailContentAdvanced(emlBytes, request);
                htmlContent = generateEnhancedEmailHtml(emailContent, request);
            } else {
                htmlContent = convertEmlToHtmlBasic(emlBytes, request);
            }

            // Convert HTML to PDF
            byte[] pdfBytes =
                    convertHtmlToPdf(
                            weasyprintPath, request, htmlContent, disableSanitize, tempFileManager);

            // Attach files if available and requested
            if (shouldAttachFiles(emailContent, request)) {
                pdfBytes =
                        attachFilesToPdf(
                                pdfBytes, emailContent.getAttachments(), pdfDocumentFactory);
            }

            return pdfBytes;

        } catch (IOException | InterruptedException e) {
            log.error("Failed to convert EML to PDF for file: {}", fileName, e);
            throw e;
        } catch (Exception e) {
            log.error("Unexpected error during EML to PDF conversion for file: {}", fileName, e);
            throw new IOException("Conversion failed: " + e.getMessage(), e);
        }
    }
