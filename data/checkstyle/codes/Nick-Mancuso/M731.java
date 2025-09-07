    private static boolean astTypeIsClassOrEnumOrRecordDef(int tokenType) {
        return tokenType == TokenTypes.CLASS_DEF
                || tokenType == TokenTypes.RECORD_DEF
                || tokenType == TokenTypes.ENUM_DEF;
    }
