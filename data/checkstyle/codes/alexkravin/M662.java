    @Override
    public void visitToken(DetailAST ast) {
        switch (ast.getType()) {
            case TokenTypes.OBJBLOCK:
                enterBlock();
                break;
            case TokenTypes.LITERAL_FOR:
            case TokenTypes.FOR_ITERATOR:
            case TokenTypes.FOR_EACH_CLAUSE:
                // we need that Tokens only at leaveToken()
                break;
            case TokenTypes.ASSIGN:
            case TokenTypes.PLUS_ASSIGN:
            case TokenTypes.MINUS_ASSIGN:
            case TokenTypes.STAR_ASSIGN:
            case TokenTypes.DIV_ASSIGN:
            case TokenTypes.MOD_ASSIGN:
            case TokenTypes.SR_ASSIGN:
            case TokenTypes.BSR_ASSIGN:
            case TokenTypes.SL_ASSIGN:
            case TokenTypes.BAND_ASSIGN:
            case TokenTypes.BXOR_ASSIGN:
            case TokenTypes.BOR_ASSIGN:
            case TokenTypes.INC:
            case TokenTypes.POST_INC:
            case TokenTypes.DEC:
            case TokenTypes.POST_DEC:
                checkIdent(ast);
                break;
            default:
                throw new IllegalStateException(ILLEGAL_TYPE_OF_TOKEN + ast);
        }
    }
