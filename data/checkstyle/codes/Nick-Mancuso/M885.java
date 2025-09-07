    private void processChildren(DetailAstImpl parent, List<? extends ParseTree> children) {
        children.forEach(child -> {
            if (child instanceof TerminalNode) {
                // Child is a token, create a new DetailAstImpl and add it to parent
                parent.addChild(create((TerminalNode) child));
            }
            else {
                // Child is another rule context; visit it, create token, and add to parent
                parent.addChild(visit(child));
            }
        });
    }
