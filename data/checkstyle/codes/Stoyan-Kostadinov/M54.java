    private void processMacroStart(XmlPullParser parser) throws MacroExecutionException {
        macroName = parser.getAttributeValue(null, Attribute.NAME.toString());

        if (macroName == null || macroName.isEmpty()) {
            final String message = String.format(Locale.ROOT,
                    "The '%s' attribute for the '%s' tag is required.",
                    Attribute.NAME, MACRO_TAG);
            throw new MacroExecutionException(message);
        }
    }
