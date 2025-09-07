    private void checkComponentParamTag(DetailAST ast,
                                        Collection<JavadocTag> tags,
                                        String recordComponentName) {

        final boolean found = tags
            .stream()
            .filter(JavadocTag::isParamTag)
            .anyMatch(tag -> tag.getFirstArg().indexOf(recordComponentName) == 0);

        if (!found) {
            log(ast, MSG_MISSING_TAG, JavadocTagInfo.PARAM.getText()
                + SPACE + recordComponentName);
        }
    }
