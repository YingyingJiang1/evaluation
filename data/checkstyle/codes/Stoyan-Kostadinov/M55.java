    private void processParamStart(XmlPullParser parser, Sink sink) throws MacroExecutionException {
        if (macroName != null && !macroName.isEmpty()) {
            final String paramName = parser
                    .getAttributeValue(null, Attribute.NAME.toString());
            final String paramValue = parser
                    .getAttributeValue(null, Attribute.VALUE.toString());

            if (paramName == null
                    || paramValue == null
                    || paramName.isEmpty()
                    || paramValue.isEmpty()) {
                final String message = String.format(Locale.ROOT,
                        "'%s' and '%s' attributes for the '%s' tag are required"
                                + " inside the '%s' tag.",
                        Attribute.NAME, Attribute.VALUE, PARAM, MACRO_TAG);
                throw new MacroExecutionException(message);
            }

            macroParameters.put(paramName, paramValue);
        }
        else {
            sink.rawText(parser.getText());
        }
    }
