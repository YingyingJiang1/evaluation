    @Nullable
    private static String findFollowedBlockTagName(DetailNode tag) {
        final DetailNode htmlElement = findFirstHtmlElementAfter(tag);
        String blockTagName = null;

        if (htmlElement != null) {
            blockTagName = getHtmlElementName(htmlElement);
        }

        return blockTagName;
    }
