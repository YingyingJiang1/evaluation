public ExtendContext negateExpressionSmart(ExtendContext expCtx, MyParser parser) {
    ExtendToken op = (ExtendToken) getOp(expCtx, parser);
    String reversedOp = compareOpMap.get(op.getText());
    if (reversedOp != null) {
        // reverse compare or logical operator
        op.setType(parser.getType(reversedOp));
        op.setText(reversedOp);
        return expCtx;
    } else {
        reversedOp = logicalOpMap.get(op.getText());
        ExtendContext exp = expCtx;
        if (reversedOp != null) {
            exp = ensureProperlyWrappedExpression(expCtx, parser);
        }
        return ParseTreeUtil.getInstance().negateExpression(exp, parser);
    }
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
        ParseTree bangChild = new TerminalNodeImpl(parser.getTokenFactory().create(parser.getBang(), "!"));
        
        ExtendContext wrapped = ensureProperlyWrappedExpression(expCtx, parser);
        
        children.add(bangChild);
        children.add(wrapped);
        notExpression.children.clear();
        notExpression.addChildren(children);
        return notExpression;
    }
}

/**
 * 确保表达式被适当包装，避免不必要的括号嵌套
 * 如果表达式已经是括号表达式或单子表达式，则直接返回原表达式
 */
private ExtendContext ensureProperlyWrappedExpression(ExtendContext expCtx, MyParser parser) {
    // 检查是否已经是括号表达式或单子表达式
    if (isAlreadyProperlyWrapped(expCtx, parser)) {
        return expCtx;
    }
    
    // 需要添加括号包装
    return encapsulateExpWithParen(expCtx, parser);
}

/**
 * 判断表达式是否已经被适当包装（括号表达式或单子表达式）
 */
private boolean isAlreadyProperlyWrapped(ExtendContext expCtx, MyParser parser) {
    // 单子表达式
    if (expCtx.getChildCount() == 1) {
        return true;
    }
    
    // 括号表达式
    if (expCtx.getChildCount() == 3
            && expCtx.getChild(0) instanceof TerminalNode ter1 
            && ter1.getSymbol().getType() == parser.getLParen()
            && expCtx.getChild(2) instanceof TerminalNode ter2 
            && ter2.getSymbol().getType() == parser.getRParen()) {
        return true;
    }
    
    return false;
}

/**
 * 用括号包装表达式
 */
private ExtendContext encapsulateExpWithParen(ExtendContext expCtx, MyParser parser) {
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