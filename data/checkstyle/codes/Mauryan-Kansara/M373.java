    private void checkParagraphTag(DetailNode tag) {
        if (!isNestedParagraph(tag)) {
            final DetailNode newLine = getNearestEmptyLine(tag);
            if (isFirstParagraph(tag)) {
                log(tag.getLineNumber(), tag.getColumnNumber(), MSG_REDUNDANT_PARAGRAPH);
            }
            else if (newLine == null || tag.getLineNumber() - newLine.getLineNumber() != 1) {
                log(tag.getLineNumber(), tag.getColumnNumber(), MSG_LINE_BEFORE);
            }

            final String blockTagName = findFollowedBlockTagName(tag);
            if (blockTagName != null) {
                log(tag.getLineNumber(), tag.getColumnNumber(),
                        MSG_PRECEDED_BLOCK_TAG, blockTagName);
            }

            if (!allowNewlineParagraph && isImmediatelyFollowedByNewLine(tag)) {
                log(tag.getLineNumber(), tag.getColumnNumber(), MSG_MISPLACED_TAG);
            }
            if (isImmediatelyFollowedByText(tag)) {
                log(tag.getLineNumber(), tag.getColumnNumber(), MSG_MISPLACED_TAG);
            }
        }
    }
