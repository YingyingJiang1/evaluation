    private static void addAttachmentToInfo(
            StringBuilder attachmentInfo, String filename, String contentType, String encoding) {
        // Create attachment info with paperclip emoji before filename
        attachmentInfo
                .append("<div class=\"attachment-item\">")
                .append("<span class=\"attachment-icon\">")
                .append(MimeConstants.ATTACHMENT_MARKER)
                .append("</span> ")
                .append("<span class=\"attachment-name\">")
                .append(escapeHtml(filename))
                .append("</span>");

        // Add content type and encoding info
        if (!contentType.isEmpty() || !encoding.isEmpty()) {
            attachmentInfo.append(" <span class=\"attachment-details\">(");
            if (!contentType.isEmpty()) {
                attachmentInfo.append(escapeHtml(contentType));
            }
            if (!encoding.isEmpty()) {
                if (!contentType.isEmpty()) attachmentInfo.append(", ");
                attachmentInfo.append("encoding: ").append(escapeHtml(encoding));
            }
            attachmentInfo.append(")</span>");
        }
        attachmentInfo.append("</div>\n");
    }
