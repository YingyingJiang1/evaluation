    @Override
    public int[] getRequiredTokens() {
        return new int[] {
            TokenTypes.LITERAL_CATCH,
            TokenTypes.IDENT,
        };
    }
