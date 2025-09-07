    private AttributeNode getAttributeNode() {
        if (attributeNode == ATTRIBUTE_NODE_UNINITIALIZED) {
            attributeNode = createAttributeNode();
        }
        return attributeNode;
    }
