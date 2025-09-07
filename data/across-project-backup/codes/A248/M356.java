    private static void appendEnhancedStyles(StringBuilder html) {
        int fontSize = StyleConstants.DEFAULT_FONT_SIZE;
        String textColor = StyleConstants.DEFAULT_TEXT_COLOR;
        String backgroundColor = StyleConstants.DEFAULT_BACKGROUND_COLOR;
        String borderColor = StyleConstants.DEFAULT_BORDER_COLOR;

        html.append("body {\n");
        html.append("  font-family: ").append(StyleConstants.DEFAULT_FONT_FAMILY).append(";\n");
        html.append("  font-size: ").append(fontSize).append("px;\n");
        html.append("  line-height: ").append(StyleConstants.DEFAULT_LINE_HEIGHT).append(";\n");
        html.append("  color: ").append(textColor).append(";\n");
        html.append("  margin: 0;\n");
        html.append("  padding: 16px;\n");
        html.append("  background-color: ").append(backgroundColor).append(";\n");
        html.append("}\n\n");

        html.append(".email-container {\n");
        html.append("  width: 100%;\n");
        html.append("  max-width: 100%;\n");
        html.append("  margin: 0 auto;\n");
        html.append("}\n\n");

        html.append(".email-header {\n");
        html.append("  padding-bottom: 10px;\n");
        html.append("  border-bottom: 1px solid ").append(borderColor).append(";\n");
        html.append("  margin-bottom: 10px;\n");
        html.append("}\n\n");
        html.append(".email-header h1 {\n");
        html.append("  margin: 0 0 10px 0;\n");
        html.append("  font-size: ").append(fontSize + 4).append("px;\n");
        html.append("  font-weight: bold;\n");
        html.append("}\n\n");
        html.append(".email-meta div {\n");
        html.append("  margin-bottom: 2px;\n");
        html.append("  font-size: ").append(fontSize - 1).append("px;\n");
        html.append("}\n\n");

        html.append(".email-body {\n");
        html.append("  word-wrap: break-word;\n");
        html.append("}\n\n");

        html.append(".attachment-section {\n");
        html.append("  margin-top: 15px;\n");
        html.append("  padding: 10px;\n");
        html.append("  background-color: ")
                .append(StyleConstants.ATTACHMENT_BACKGROUND_COLOR)
                .append(";\n");
        html.append("  border: 1px solid ")
                .append(StyleConstants.ATTACHMENT_BORDER_COLOR)
                .append(";\n");
        html.append("  border-radius: 3px;\n");
        html.append("}\n\n");
        html.append(".attachment-section h3 {\n");
        html.append("  margin: 0 0 8px 0;\n");
        html.append("  font-size: ").append(fontSize + 1).append("px;\n");
        html.append("}\n\n");
        html.append(".attachment-item {\n");
        html.append("  padding: 5px 0;\n");
        html.append("}\n\n");
        html.append(".attachment-icon {\n");
        html.append("  margin-right: 5px;\n");
        html.append("}\n\n");
        html.append(".attachment-details, .attachment-type {\n");
        html.append("  font-size: ").append(fontSize - 2).append("px;\n");
        html.append("  color: #555555;\n");
        html.append("}\n\n");
        html.append(".attachment-inclusion-note, .attachment-info-note {\n");
        html.append("  margin-top: 8px;\n");
        html.append("  padding: 6px;\n");
        html.append("  font-size: ").append(fontSize - 2).append("px;\n");
        html.append("  border-radius: 3px;\n");
        html.append("}\n\n");
        html.append(".attachment-inclusion-note {\n");
        html.append("  background-color: #e6ffed;\n");
        html.append("  border: 1px solid #d4f7dc;\n");
        html.append("  color: #006420;\n");
        html.append("}\n\n");
        html.append(".attachment-info-note {\n");
        html.append("  background-color: #fff9e6;\n");
        html.append("  border: 1px solid #fff0c2;\n");
        html.append("  color: #664d00;\n");
        html.append("}\n\n");
        html.append(".attachment-link-container {\n");
        html.append("  display: flex;\n");
        html.append("  align-items: center;\n");
        html.append("  padding: 8px;\n");
        html.append("  background-color: #f8f9fa;\n");
        html.append("  border: 1px solid #dee2e6;\n");
        html.append("  border-radius: 4px;\n");
        html.append("  margin: 4px 0;\n");
        html.append("}\n\n");
        html.append(".attachment-link-container:hover {\n");
        html.append("  background-color: #e9ecef;\n");
        html.append("}\n\n");
        html.append(".attachment-note {\n");
        html.append("  font-size: ").append(fontSize - 3).append("px;\n");
        html.append("  color: #6c757d;\n");
        html.append("  font-style: italic;\n");
        html.append("  margin-left: 8px;\n");
        html.append("}\n\n");

        // Basic image styling: ensure images are responsive but not overly constrained.
        html.append("img {\n");
        html.append("  max-width: 100%;\n"); // Make images responsive to container width
        html.append("  height: auto;\n"); // Maintain aspect ratio
        html.append("  display: block;\n"); // Avoid extra space below images
        html.append("}\n\n");
    }
