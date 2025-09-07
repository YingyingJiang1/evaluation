    private static void processPartAdvanced(
            Object part, EmailContent content, EmlToPdfRequest request) {
        try {
            if (!isValidJakartaMailPart(part)) {
                log.warn("Invalid Jakarta Mail part type: {}", part.getClass().getName());
                return;
            }

            Class<?> partClass = part.getClass();
            Method isMimeType = partClass.getMethod("isMimeType", String.class);
            Method getContent = partClass.getMethod("getContent");
            Method getDisposition = partClass.getMethod("getDisposition");
            Method getFileName = partClass.getMethod("getFileName");
            Method getContentType = partClass.getMethod("getContentType");
            Method getHeader = partClass.getMethod("getHeader", String.class);

            Object disposition = getDisposition.invoke(part);
            String filename = (String) getFileName.invoke(part);
            String contentType = (String) getContentType.invoke(part);

            if ((Boolean) isMimeType.invoke(part, "text/plain") && disposition == null) {
                content.setTextBody((String) getContent.invoke(part));
            } else if ((Boolean) isMimeType.invoke(part, "text/html") && disposition == null) {
                content.setHtmlBody((String) getContent.invoke(part));
            } else if ("attachment".equalsIgnoreCase((String) disposition)
                    || (filename != null && !filename.trim().isEmpty())) {

                content.setAttachmentCount(content.getAttachmentCount() + 1);

                // Always extract basic attachment metadata for display
                if (filename != null && !filename.trim().isEmpty()) {
                    // Create attachment with metadata only
                    EmailAttachment attachment = new EmailAttachment();
                    // Apply MIME decoding to filename to handle encoded attachment names
                    attachment.setFilename(safeMimeDecode(filename));
                    attachment.setContentType(contentType);

                    // Check if it's an embedded image
                    String[] contentIdHeaders = (String[]) getHeader.invoke(part, "Content-ID");
                    if (contentIdHeaders != null && contentIdHeaders.length > 0) {
                        attachment.setEmbedded(true);
                        // Store the Content-ID, removing angle brackets if present
                        String contentId = contentIdHeaders[0];
                        if (contentId.startsWith("<") && contentId.endsWith(">")) {
                            contentId = contentId.substring(1, contentId.length() - 1);
                        }
                        attachment.setContentId(contentId);
                    }

                    // Extract attachment data if attachments should be included OR if it's an
                    // embedded image (needed for inline display)
                    if ((request != null && request.isIncludeAttachments())
                            || attachment.isEmbedded()) {
                        try {
                            Object attachmentContent = getContent.invoke(part);
                            byte[] attachmentData = null;

                            if (attachmentContent instanceof java.io.InputStream inputStream) {
                                try {
                                    attachmentData = inputStream.readAllBytes();
                                } catch (IOException e) {
                                    log.warn(
                                            "Failed to read InputStream attachment: {}",
                                            e.getMessage());
                                }
                            } else if (attachmentContent instanceof byte[] byteArray) {
                                attachmentData = byteArray;
                            } else if (attachmentContent instanceof String stringContent) {
                                attachmentData = stringContent.getBytes(StandardCharsets.UTF_8);
                            }

                            if (attachmentData != null) {
                                // Check size limit (use default 10MB if request is null)
                                long maxSizeMB =
                                        request != null ? request.getMaxAttachmentSizeMB() : 10L;
                                long maxSizeBytes = maxSizeMB * 1024 * 1024;

                                if (attachmentData.length <= maxSizeBytes) {
                                    attachment.setData(attachmentData);
                                    attachment.setSizeBytes(attachmentData.length);
                                } else {
                                    // For embedded images, always include data regardless of size
                                    // to ensure inline display works
                                    if (attachment.isEmbedded()) {
                                        attachment.setData(attachmentData);
                                        attachment.setSizeBytes(attachmentData.length);
                                    } else {
                                        // Still show attachment info even if too large
                                        attachment.setSizeBytes(attachmentData.length);
                                    }
                                }
                            }
                        } catch (Exception e) {
                            log.warn("Error extracting attachment data: {}", e.getMessage());
                        }
                    }

                    // Add attachment to the list for display (with or without data)
                    content.getAttachments().add(attachment);
                }
            } else if ((Boolean) isMimeType.invoke(part, "multipart/*")) {
                // Handle nested multipart content
                try {
                    Object multipartContent = getContent.invoke(part);
                    Class<?> multipartClass = Class.forName("jakarta.mail.Multipart");
                    if (multipartClass.isInstance(multipartContent)) {
                        processMultipartAdvanced(multipartContent, content, request);
                    }
                } catch (Exception e) {
                    log.warn("Error processing multipart content: {}", e.getMessage());
                }
            }

        } catch (Exception e) {
            log.warn("Error processing multipart part: {}", e.getMessage());
        }
    }
