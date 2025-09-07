    private static List<ModulePropertyDetails> createProperties(Element properties) {
        final NodeList propertyList = properties.getElementsByTagName("property");
        final int propertyListLength = propertyList.getLength();
        final List<ModulePropertyDetails> result = new ArrayList<>(propertyListLength);
        for (int i = 0; i < propertyListLength; i++) {
            final ModulePropertyDetails propertyDetails = new ModulePropertyDetails();
            final Element prop = (Element) propertyList.item(i);
            propertyDetails.setName(getAttributeValue(prop, XML_TAG_NAME));
            propertyDetails.setType(getAttributeValue(prop, "type"));
            final String defaultValueTag = "default-value";
            if (prop.hasAttribute(defaultValueTag)) {
                propertyDetails.setDefaultValue(getAttributeValue(prop, defaultValueTag));
            }
            final String validationTypeTag = "validation-type";
            if (prop.hasAttribute(validationTypeTag)) {
                propertyDetails.setValidationType(getAttributeValue(prop, validationTypeTag));
            }
            propertyDetails.setDescription(getDirectChildsByTag(prop, XML_TAG_DESCRIPTION)
                    .get(0).getFirstChild().getNodeValue());
            result.add(propertyDetails);
        }
        return result;
    }
