    private static String extractAttachmentInfo(String emlContent) {
        StringBuilder attachmentInfo = new StringBuilder();
        try {
            String[] lines = emlContent.split("\r?\n");
            boolean inHeaders = true;
            String currentContentType = "";
            String currentDisposition = "";
            String currentFilename = "";
            String currentEncoding = "";
            boolean inMultipart = false;
            String boundary = "";

            // First pass: find boundary for multipart messages
            for (String line : lines) {
                String lowerLine = line.toLowerCase().trim();
                if (lowerLine.startsWith("content-type:") && lowerLine.contains("multipart")) {
                    if (lowerLine.contains("boundary=")) {
                        int boundaryStart = lowerLine.indexOf("boundary=") + 9;
                        String boundaryPart = line.substring(boundaryStart).trim();
                        if (boundaryPart.startsWith("\"")) {
                            boundary = boundaryPart.substring(1, boundaryPart.indexOf("\"", 1));
                        } else {
                            int spaceIndex = boundaryPart.indexOf(" ");
                            boundary =
                                    spaceIndex > 0
                                            ? boundaryPart.substring(0, spaceIndex)
                                            : boundaryPart;
                        }
                        inMultipart = true;
                        break;
                    }
                }
                if (line.trim().isEmpty()) break;
            }

            // Second pass: extract attachment information
            for (String line : lines) {
                String lowerLine = line.toLowerCase().trim();

                // Check for boundary markers in multipart messages
                if (inMultipart && line.trim().startsWith("--" + boundary)) {
                    // Reset for new part
                    currentContentType = "";
                    currentDisposition = "";
                    currentFilename = "";
                    currentEncoding = "";
                    inHeaders = true;
                    continue;
                }

                if (inHeaders && line.trim().isEmpty()) {
                    inHeaders = false;

                    // Process accumulated attachment info
                    if (isAttachment(currentDisposition, currentFilename, currentContentType)) {
                        addAttachmentToInfo(
                                attachmentInfo,
                                currentFilename,
                                currentContentType,
                                currentEncoding);

                        // Reset for next attachment
                        currentContentType = "";
                        currentDisposition = "";
                        currentFilename = "";
                        currentEncoding = "";
                    }
                    continue;
                }

                if (!inHeaders) continue; // Skip body content

                // Parse headers
                if (lowerLine.startsWith("content-type:")) {
                    currentContentType = line.substring(13).trim();
                } else if (lowerLine.startsWith("content-disposition:")) {
                    currentDisposition = line.substring(20).trim();
                    // Extract filename if present
                    currentFilename = extractFilenameFromDisposition(currentDisposition);
                } else if (lowerLine.startsWith("content-transfer-encoding:")) {
                    currentEncoding = line.substring(26).trim();
                } else if (line.startsWith(" ") || line.startsWith("\t")) {
                    // Continuation of previous header
                    if (currentDisposition.contains("filename=")) {
                        currentDisposition += " " + line.trim();
                        currentFilename = extractFilenameFromDisposition(currentDisposition);
                    } else if (!currentContentType.isEmpty()) {
                        currentContentType += " " + line.trim();
                    }
                }
            }

            if (isAttachment(currentDisposition, currentFilename, currentContentType)) {
                addAttachmentToInfo(
                        attachmentInfo, currentFilename, currentContentType, currentEncoding);
            }

        } catch (RuntimeException e) {
            log.warn("Error extracting attachment info: {}", e.getMessage());
        }
        return attachmentInfo.toString();
    }
