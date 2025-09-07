    @Override
    public DetailAstImpl visitPermittedSubclassesAndInterfaces(
            JavaLanguageParser.PermittedSubclassesAndInterfacesContext ctx) {
        final DetailAstImpl literalPermits =
                create(TokenTypes.PERMITS_CLAUSE, (Token) ctx.LITERAL_PERMITS().getPayload());
        // 'LITERAL_PERMITS' is child[0]
        processChildren(literalPermits, ctx.children.subList(1, ctx.children.size()));
        return literalPermits;
    }
