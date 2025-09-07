    @Override
    protected void handleStartTag(XmlPullParser parser, Sink sink) throws MacroExecutionException {
        final String tagName = parser.getName();
        if (tagName.equals(DOCUMENT_TAG.toString())) {
            sink.body();
            sink.rawText(parser.getText());
        }
        else if (tagName.equals(MACRO_TAG.toString()) && !isSecondParsing()) {
            processMacroStart(parser);
            setIgnorableWhitespace(true);
        }
        else if (tagName.equals(PARAM.toString()) && !isSecondParsing()) {
            processParamStart(parser, sink);
        }
        else {
            sink.rawText(parser.getText());
        }
    }
