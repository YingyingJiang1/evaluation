    @Override
    public DetailAstImpl visitSuperExp(JavaLanguageParser.SuperExpContext ctx) {
        final DetailAstImpl bop = create(ctx.bop);
        bop.addChild(visit(ctx.expr()));
        bop.addChild(create(ctx.LITERAL_SUPER()));
        DetailAstImpl superSuffixParent = visit(ctx.superSuffix());

        if (superSuffixParent == null) {
            superSuffixParent = bop;
        }
        else {
            DetailAstImpl firstChild = superSuffixParent;
            while (firstChild.getFirstChild() != null) {
                firstChild = firstChild.getFirstChild();
            }
            firstChild.addPreviousSibling(bop);
        }

        return superSuffixParent;
    }
