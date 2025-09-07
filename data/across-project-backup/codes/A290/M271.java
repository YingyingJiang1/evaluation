    @Override
    public void startElement(String uri, String localName, String name, Attributes attributes) {
        if (name == null) {
            return;
        }
        switch (name) {
            case ExcelXmlConstants.SHAREDSTRINGS_T_TAG:
            case ExcelXmlConstants.SHAREDSTRINGS_X_T_TAG:
            case ExcelXmlConstants.SHAREDSTRINGS_NS2_T_TAG:
                currentElementData = null;
                isTagt = true;
                break;
            case ExcelXmlConstants.SHAREDSTRINGS_SI_TAG:
            case ExcelXmlConstants.SHAREDSTRINGS_X_SI_TAG:
            case ExcelXmlConstants.SHAREDSTRINGS_NS2_SI_TAG:
                currentData = null;
                break;
            case ExcelXmlConstants.SHAREDSTRINGS_RPH_TAG:
            case ExcelXmlConstants.SHAREDSTRINGS_X_RPH_TAG:
            case ExcelXmlConstants.SHAREDSTRINGS_NS2_RPH_TAG:
                ignoreTagt = true;
                break;
            default:
                // ignore
        }
    }
