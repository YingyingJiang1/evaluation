    private static void collectMethodDeclarations(Deque<AbstractFrame> frameStack,
                                                  DetailAST ast, AbstractFrame frame) {
        final DetailAST methodFrameNameIdent = ast.findFirstToken(TokenTypes.IDENT);
        final DetailAST mods = ast.findFirstToken(TokenTypes.MODIFIERS);
        if (mods.findFirstToken(TokenTypes.LITERAL_STATIC) == null) {
            ((ClassFrame) frame).addInstanceMethod(methodFrameNameIdent);
        }
        else {
            ((ClassFrame) frame).addStaticMethod(methodFrameNameIdent);
        }
        frameStack.addFirst(new MethodFrame(frame, methodFrameNameIdent));
    }
