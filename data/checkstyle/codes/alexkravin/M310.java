    @Override
    public int[] getAcceptableTokens() {
        return new int[] {
            TokenTypes.CTOR_DEF,
            TokenTypes.METHOD_DEF,
            TokenTypes.EXPR,
            TokenTypes.LAND,
            TokenTypes.BAND,
            TokenTypes.LOR,
            TokenTypes.BOR,
            TokenTypes.BXOR,
            TokenTypes.COMPACT_CTOR_DEF,
        };
    }
