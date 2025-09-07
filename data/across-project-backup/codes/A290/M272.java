    @Override
    public void endElement(String uri, String localName, String name) {
        if (name == null) {
            return;
        }
        switch (name) {
            case ExcelXmlConstants.SHAREDSTRINGS_T_TAG:
            case ExcelXmlConstants.SHAREDSTRINGS_X_T_TAG:
            case ExcelXmlConstants.SHAREDSTRINGS_NS2_T_TAG:
                if (currentElementData != null) {
                    if (currentData == null) {
                        currentData = new StringBuilder();
                    }
                    currentData.append(currentElementData);
                }
                isTagt = false;
                break;
            case ExcelXmlConstants.SHAREDSTRINGS_SI_TAG:
            case ExcelXmlConstants.SHAREDSTRINGS_X_SI_TAG:
            case ExcelXmlConstants.SHAREDSTRINGS_NS2_SI_TAG:
                if (currentData == null) {
                    readCache.put(null);
                } else {
                    readCache.put(utfDecode(currentData.toString()));
                }
                break;
            case ExcelXmlConstants.SHAREDSTRINGS_RPH_TAG:
            case ExcelXmlConstants.SHAREDSTRINGS_X_RPH_TAG:
            case ExcelXmlConstants.SHAREDSTRINGS_NS2_RPH_TAG:
                ignoreTagt = false;
                break;
            default:
                // ignore
        }
    }
