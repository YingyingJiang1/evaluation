    @Override
    public String getAttributeValue(NamespaceUri namespace, String localPart) {
        final String result;
        if (TEXT_ATTRIBUTE_NAME.equals(localPart)) {
            result = Optional.ofNullable(getAttributeNode())
                .map(AttributeNode::getStringValue)
                .orElse(null);
        }
        else {
            result = null;
        }
        return result;
    }
