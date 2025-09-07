    private static void populateModule(Element mod, ModuleDetails moduleDetails) {
        moduleDetails.setName(getAttributeValue(mod, XML_TAG_NAME));
        moduleDetails.setFullQualifiedName(getAttributeValue(mod, "fully-qualified-name"));
        moduleDetails.setParent(getAttributeValue(mod, "parent"));
        moduleDetails.setDescription(getDirectChildsByTag(mod, XML_TAG_DESCRIPTION).get(0)
                .getFirstChild().getNodeValue());
        final List<Element> properties = getDirectChildsByTag(mod, "properties");
        if (!properties.isEmpty()) {
            final List<ModulePropertyDetails> modulePropertyDetailsList =
                    createProperties(properties.get(0));
            moduleDetails.addToProperties(modulePropertyDetailsList);
        }
        final List<String> messageKeys =
                getListContentByAttribute(mod,
                        "message-keys", "message-key", "key");
        if (messageKeys != null) {
            moduleDetails.addToViolationMessages(messageKeys);
        }
    }
