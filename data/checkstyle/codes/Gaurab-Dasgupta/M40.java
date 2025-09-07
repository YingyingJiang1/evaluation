    public static void write(ModuleDetails moduleDetails) throws TransformerException,
            ParserConfigurationException {
        final DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
        dbFactory.setAttribute(XMLConstants.ACCESS_EXTERNAL_DTD, "");
        dbFactory.setAttribute(XMLConstants.ACCESS_EXTERNAL_SCHEMA, "");
        final DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
        final Document doc = dBuilder.newDocument();

        final Element rootElement = doc.createElement("checkstyle-metadata");
        final Element rootChild = doc.createElement("module");
        rootElement.appendChild(rootChild);

        doc.appendChild(rootElement);

        final Element checkModule = doc.createElement(moduleDetails.getModuleType().getLabel());
        rootChild.appendChild(checkModule);

        checkModule.setAttribute(XML_TAG_NAME, moduleDetails.getName());
        checkModule.setAttribute("fully-qualified-name",
                moduleDetails.getFullQualifiedName());
        checkModule.setAttribute("parent", moduleDetails.getParent());

        final Element desc = doc.createElement(XML_TAG_DESCRIPTION);
        final Node cdataDesc = doc.createCDATASection(moduleDetails.getDescription());
        desc.appendChild(cdataDesc);
        checkModule.appendChild(desc);
        createPropertySection(moduleDetails, checkModule, doc);
        if (!moduleDetails.getViolationMessageKeys().isEmpty()) {
            final Element messageKeys = doc.createElement("message-keys");
            for (String msg : moduleDetails.getViolationMessageKeys()) {
                final Element messageKey = doc.createElement("message-key");
                messageKey.setAttribute("key", msg);
                messageKeys.appendChild(messageKey);
            }
            checkModule.appendChild(messageKeys);
        }

        writeToFile(doc, moduleDetails);
    }
