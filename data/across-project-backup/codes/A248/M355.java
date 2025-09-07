    private static String processInlineImages(String htmlContent, EmailContent emailContent) {
        if (htmlContent == null || emailContent == null) return htmlContent;

        // Create a map of Content-ID to attachment data
        Map<String, EmailAttachment> contentIdMap = new HashMap<>();
        for (EmailAttachment attachment : emailContent.getAttachments()) {
            if (attachment.isEmbedded()
                    && attachment.getContentId() != null
                    && attachment.getData() != null) {
                contentIdMap.put(attachment.getContentId(), attachment);
            }
        }

        if (contentIdMap.isEmpty()) return htmlContent;

        // Pattern to match cid: references in img src attributes
        Pattern cidPattern =
                Pattern.compile(
                        "(?i)<img[^>]*\\ssrc\\s*=\\s*['\"]cid:([^'\"]+)['\"][^>]*>",
                        Pattern.CASE_INSENSITIVE);
        Matcher matcher = cidPattern.matcher(htmlContent);

        StringBuffer result = new StringBuffer();
        while (matcher.find()) {
            String contentId = matcher.group(1);
            EmailAttachment attachment = contentIdMap.get(contentId);

            if (attachment != null && attachment.getData() != null) {
                // Convert to data URI
                String mimeType = attachment.getContentType();
                if (mimeType == null || mimeType.isEmpty()) {
                    // Try to determine MIME type from filename
                    String filename = attachment.getFilename();
                    if (filename != null) {
                        if (filename.toLowerCase().endsWith(".png")) {
                            mimeType = "image/png";
                        } else if (filename.toLowerCase().endsWith(".jpg")
                                || filename.toLowerCase().endsWith(".jpeg")) {
                            mimeType = "image/jpeg";
                        } else if (filename.toLowerCase().endsWith(".gif")) {
                            mimeType = "image/gif";
                        } else if (filename.toLowerCase().endsWith(".bmp")) {
                            mimeType = "image/bmp";
                        } else {
                            mimeType = "image/png"; // fallback
                        }
                    } else {
                        mimeType = "image/png"; // fallback
                    }
                }

                String base64Data = Base64.getEncoder().encodeToString(attachment.getData());
                String dataUri = "data:" + mimeType + ";base64," + base64Data;

                // Replace the cid: reference with the data URI
                String replacement =
                        matcher.group(0).replaceFirst("cid:" + Pattern.quote(contentId), dataUri);
                matcher.appendReplacement(result, Matcher.quoteReplacement(replacement));
            } else {
                // Keep original if attachment not found
                matcher.appendReplacement(result, Matcher.quoteReplacement(matcher.group(0)));
            }
        }
        matcher.appendTail(result);

        return result.toString();
    }
