    private Map<String, List<Object>> extractAttributes(Assertion assertion) {
        Map<String, List<Object>> attributes = new HashMap<>();

        for (AttributeStatement attributeStatement : assertion.getAttributeStatements()) {
            for (Attribute attribute : attributeStatement.getAttributes()) {
                String attributeName = attribute.getName();
                List<Object> values = new ArrayList<>();

                for (XMLObject xmlObject : attribute.getAttributeValues()) {
                    // Get the text content directly
                    String value = xmlObject.getDOM().getTextContent();
                    if (value != null && !value.trim().isEmpty()) {
                        values.add(value);
                    }
                }

                if (!values.isEmpty()) {
                    // Store with both full URI and last part of the URI
                    attributes.put(attributeName, values);
                    String shortName = attributeName.substring(attributeName.lastIndexOf('/') + 1);
                    attributes.put(shortName, values);
                }
            }
        }

        return attributes;
    }
