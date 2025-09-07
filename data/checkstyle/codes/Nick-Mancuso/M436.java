    private static List<String> getRecordComponentNames(DetailAST node) {
        final DetailAST components = node.findFirstToken(TokenTypes.RECORD_COMPONENTS);
        final List<String> componentList = new ArrayList<>();

        if (components != null) {
            TokenUtil.forEachChild(components,
                TokenTypes.RECORD_COMPONENT_DEF, component -> {
                    final DetailAST ident = component.findFirstToken(TokenTypes.IDENT);
                    componentList.add(ident.getText());
                });
        }

        return componentList;
    }
