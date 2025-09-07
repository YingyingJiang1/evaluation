    private static String getTagTextFromProperty(DetailNode nodeLi, DetailNode propertyMeta) {
        final Optional<DetailNode> tagNodeOpt = getFirstChildOfType(nodeLi,
                JavadocTokenTypes.JAVADOC_INLINE_TAG, propertyMeta.getIndex() + 1);
        DetailNode tagNode = null;
        if (tagNodeOpt.isPresent()) {
            tagNode = tagNodeOpt.orElseThrow();
        }
        return getTextFromTag(tagNode);
    }
