    private static DetailAstImpl createImaginary(int tokenType) {
        final DetailAstImpl detailAst = new DetailAstImpl();
        detailAst.setType(tokenType);
        detailAst.setText(TokenUtil.getTokenName(tokenType));
        return detailAst;
    }
