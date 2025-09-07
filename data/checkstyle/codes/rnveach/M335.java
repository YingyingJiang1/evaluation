    private static AbstractImportRule createImportRule(String qName, Attributes attributes)
            throws SAXException {
        // Need to handle either "pkg" or "class" attribute.
        // May have "exact-match" for "pkg"
        // May have "local-only"
        final boolean isAllow = ALLOW_ELEMENT_NAME.equals(qName);
        final boolean isLocalOnly = attributes.getValue("local-only") != null;
        final String pkg = attributes.getValue(PKG_ATTRIBUTE_NAME);
        final boolean regex = containsRegexAttribute(attributes);
        final AbstractImportRule rule;
        if (pkg == null) {
            // handle class names which can be normal class names or regular
            // expressions
            final String clazz = safeGet(attributes, "class");
            rule = new ClassImportRule(isAllow, isLocalOnly, clazz, regex);
        }
        else {
            final boolean exactMatch =
                    attributes.getValue("exact-match") != null;
            rule = new PkgImportRule(isAllow, isLocalOnly, pkg, exactMatch, regex);
        }
        return rule;
    }
