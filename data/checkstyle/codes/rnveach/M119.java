    private void printXmlHeader() {
        if (!isXmlHeaderPrinted) {
            writer.println("<?xml version=\"1.0\" encoding=\"UTF-8\"?>");
            writer.println("<!DOCTYPE suppressions PUBLIC");
            writer.println("    \"-//Checkstyle//DTD SuppressionXpathFilter Experimental "
                    + "Configuration 1.2//EN\"");
            writer.println("    \"https://checkstyle.org/dtds/"
                    + "suppressions_1_2_xpath_experimental.dtd\">");
            writer.println("<suppressions>");
            isXmlHeaderPrinted = true;
        }
    }
