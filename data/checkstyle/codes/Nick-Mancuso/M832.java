    @Override
    public DetailAstImpl visitResourceSpecification(
            JavaLanguageParser.ResourceSpecificationContext ctx) {
        final DetailAstImpl resourceSpecification =
                createImaginary(TokenTypes.RESOURCE_SPECIFICATION);
        processChildren(resourceSpecification, ctx.children);
        return resourceSpecification;
    }
