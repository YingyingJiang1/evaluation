    public static boolean isTypeDeclaration(int type) {
        return type == TokenTypes.CLASS_DEF
                || type == TokenTypes.INTERFACE_DEF
                || type == TokenTypes.ANNOTATION_DEF
                || type == TokenTypes.ENUM_DEF
                || type == TokenTypes.RECORD_DEF;
    }
