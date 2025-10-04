```java
/**
 * Utility: 尝试给表达式加上括号包装，避免重复逻辑
 */
private ExtendContext wrapWithParenIfNeeded(ExtendContext expCtx, MyParser parser) {
    // 如果已经是 (exp) 或单个子节点，就直接返回
    if (expCtx.getChildCount() == 3
            && expCtx.getChild(0) instanceof TerminalNode ter1 && ter1.getSymbol().getType() == parser.getLParen()
            && expCtx.getChild(2) instanceof TerminalNode ter2 && ter2.getSymbol().getType() == parser.getRParen()) {
        return expCtx;
    }
    if (expCtx.getChildCount() == 1) {
        return expCtx;
    }

    // 否则包装成 (exp)
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

public ExtendContext negateExpressionSmart(ExtendContext expCtx, MyParser parser) {
    ExtendToken op = (ExtendToken) getOp(expCtx, parser);
    String reversedOp = compareOpMap.get(op.getText());
    if (reversedOp != null) {
        // reverse compare or logical operator
        op.setType(parser.getType(reversedOp));
        op.setText(reversedOp);
        return expCtx;
    }

    reversedOp = logicalOpMap.get(op.getText());
    ExtendContext exp = expCtx;
    if (reversedOp != null) {
        exp = wrapWithParenIfNeeded(expCtx, parser);
    }

    // expression -> !expression or !expression -> expression
    return ParseTreeUtil.getInstance().negateExpression(exp, parser);
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

        ExtendContext wrapped = wrapWithParenIfNeeded(expCtx, parser);

        children.add(bangChild);
        children.add(wrapped);
        notExpression.children.clear();
        notExpression.addChildren(children);
        return notExpression;
    }
}
```
