    private void scrapeContent(DetailNode ast) {
        if (ast.getType() == JavadocTokenTypes.PARAGRAPH) {
            if (isParentText(ast)) {
                parentSectionStartIdx = getParentIndexOf(ast);
                moduleDetails.setParent(getParentText(ast));
            }
            else if (isViolationMessagesText(ast)) {
                scrapingViolationMessageList = true;
            }
            else if (exampleSectionStartIdx == -1
                    && isExamplesText(ast)) {
                exampleSectionStartIdx = getParentIndexOf(ast);
            }
        }
        else if (ast.getType() == JavadocTokenTypes.LI) {
            if (isPropertyList(ast)) {
                if (propertySectionStartIdx == -1) {
                    propertySectionStartIdx = getParentIndexOf(ast);
                }
                moduleDetails.addToProperties(createProperties(ast));
            }
            else if (scrapingViolationMessageList) {
                moduleDetails.addToViolationMessages(getViolationMessages(ast));
            }
        }
    }
