    public static AccessModifierOption getSurroundingAccessModifier(DetailAST node) {
        AccessModifierOption returnValue = null;
        for (DetailAST token = node;
             returnValue == null && !TokenUtil.isRootNode(token);
             token = token.getParent()) {
            final int type = token.getType();
            if (type == TokenTypes.CLASS_DEF
                || type == TokenTypes.INTERFACE_DEF
                || type == TokenTypes.ANNOTATION_DEF
                || type == TokenTypes.ENUM_DEF) {
                returnValue = getAccessModifierFromModifiersToken(token);
            }
            else if (type == TokenTypes.LITERAL_NEW) {
                break;
            }
        }

        return returnValue;
    }
