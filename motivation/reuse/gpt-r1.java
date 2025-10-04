public ExtendContext negateExpressionSmart(ExtendContext expCtx, MyParser parser) {
    ExtendToken op = (ExtendToken) getOp(expCtx, parser);
    String reversedOp = compareOpMap.get(op.getText());
    
    if (reversedOp != null) {
        // reverse compare operator
        op.setType(parser.getType(reversedOp));
        op.setText(reversedOp);
        return expCtx;
    }

    reversedOp = logicalOpMap.get(op.getText());
    ExtendContext exp = expCtx;
    
    if (reversedOp != null) {
        if (isAlreadyWrappedWithParen(expCtx, parser) || expCtx.getChildCount() == 1) {
            exp = expCtx;
        } else {
            exp = createParenExpression(expCtx, parser);
        }
    }
    
    // expression -> !expression or !expression -> expression
    ExtendContext notExp = ParseTreeUtil.getInstance().negateExpression(exp, parser);
    return notExp;
}

/**
 * @apiNote Create a negative expression of @expCtx,
 * @implNote just wrap the original expression and add a "!" token in the front.
 */
public ExtendContext negateExpression(ExtendContext expCtx, MyParser parser) {
    ExtendContext parent = (ExtendContext) expCtx.getParent();
    
    if (expCtx.start.getText().equals("!")) {
        ExtendContext notExpression = (ExtendContext) expCtx.getChild(1);
        notExpression.setParent(expCtx.getParent());
        return notExpression;
    } else {
        ExtendContext notExpression = (ExtendContext) parser.createExpression(parent, expCtx.invokingState);
        List<ParseTree> children = new ArrayList<>();
        
        ExtendContext wrapped = encapsulateExpWithParen(expCtx, parser);
        if (isAlreadyWrappedWithParen(expCtx, parser) || expCtx.getChildCount() == 1) {
            wrapped = expCtx;
        } else {
            wrapped = createParenExpression(expCtx, parser);
        }

        ParseTree bangChild = new TerminalNodeImpl(parser.getTokenFactory().create(parser.getBang(), "!"));
        children.add(bangChild);
        children.add(wrapped);
        
        notExpression.children.clear();
        notExpression.addChildren(children);
        return notExpression;
    }
}

/**
 * 检查表达式是否已经用括号包裹
 */
private boolean isAlreadyWrappedWithParen(ExtendContext expCtx, MyParser parser) {
    return expCtx.getChildCount() == 3
            && expCtx.getChild(0) instanceof TerminalNode ter1 
            && ter1.getSymbol().getType() == parser.getLParen()
            && expCtx.getChild(2) instanceof TerminalNode ter2 
            && ter2.getSymbol().getType() == parser.getRParen();
}

/**
 * 创建括号表达式
 */
private ExtendContext createParenExpression(ExtendContext expCtx, MyParser parser) {
    Token lParen = parser.getTokenFactory().create(parser.getLParen(), "(");
    Token rParen = parser.getTokenFactory().create(parser.getRParen(), ")");
    
    ExtendContext parent = (ExtendContext) expCtx.getParent();
    ExtendContext parenExpression = (ExtendContext) parser.createExpression(parent, expCtx.invokingState);
    
    List<ParseTree> children = new ArrayList<>();
    children.add(new TerminalNodeImpl(lParen));
    children.add(expCtx);
    children.add(new TerminalNodeImpl(rParen));
    
    parenExpression.children.clear();
    parenExpression.addChildren(children);
    return parenExpression;
}