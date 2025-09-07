    private static void createParentModuleParagraph(XdocSink sink, Class<?> clss, String moduleName)
            throws MacroExecutionException {
        final String parentModule = SiteUtil.getParentModule(clss);
        final String linkToParentModule = getLinkToParentModule(parentModule, moduleName);

        sink.setInsertNewline(false);
        sink.paragraph();
        sink.setInsertNewline(true);
        final String indentLevel10 = SiteUtil.getNewlineAndIndentSpaces(10);
        sink.rawText(indentLevel10);
        sink.link(linkToParentModule);
        sink.text(parentModule);
        sink.link_();
        final String indentLevel8 = SiteUtil.getNewlineAndIndentSpaces(8);
        sink.rawText(indentLevel8);
        sink.paragraph_();
    }
