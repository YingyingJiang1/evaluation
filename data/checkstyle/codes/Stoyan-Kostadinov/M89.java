        private static boolean isEndOfDescription(DetailNode child) {
            final DetailNode nextSibling = JavadocUtil.getNextSibling(child);
            final DetailNode secondNextSibling = JavadocUtil.getNextSibling(nextSibling);
            final DetailNode thirdNextSibling = JavadocUtil.getNextSibling(secondNextSibling);

            return child.getType() == JavadocTokenTypes.NEWLINE
                        && nextSibling.getType() == JavadocTokenTypes.LEADING_ASTERISK
                        && secondNextSibling.getType() == JavadocTokenTypes.NEWLINE
                        && thirdNextSibling.getType() == JavadocTokenTypes.LEADING_ASTERISK;
        }
