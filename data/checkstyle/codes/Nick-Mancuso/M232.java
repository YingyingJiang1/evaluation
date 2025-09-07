    public static boolean isOnRecord(DetailAST blockComment) {
        return isOnPlainToken(blockComment, TokenTypes.RECORD_DEF, TokenTypes.LITERAL_RECORD)
            || isOnTokenWithModifiers(blockComment, TokenTypes.RECORD_DEF)
            || isOnTokenWithAnnotation(blockComment, TokenTypes.RECORD_DEF);
    }
