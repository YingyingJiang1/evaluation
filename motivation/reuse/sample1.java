在确保代码功能不变的前提下，提取代码公共逻辑，达到代码复用，减少重复率。注意：必须确保函数功能的不变性。同时，你应该避免过度抽象，不要为了复用而复用。

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
                if (expCtx.getChildCount() == 3
                        && expCtx.getChild(0) instanceof TerminalNode ter1 && ter1.getSymbol().getType() == parser.getLParen()) {
                    if (expCtx.getChild(2) instanceof TerminalNode ter2 && ter2.getSymbol().getType() == parser.getRParen()) {
                        exp =  expCtx;
                    }
                }

                if (expCtx.getChildCount() == 1) {
                    exp =  expCtx;
                }

                Token lParen = parser.getTokenFactory().create(parser.getLParen(), "("), rParen = parser.getTokenFactory().create(parser.getRParen(), ")");
                ExtendContext parent = (ExtendContext) expCtx.getParent();
                ExtendContext parenExpression = (ExtendContext) parser.createExpression(parent, expCtx.invokingState);
                List<ParseTree> children = new ArrayList<>();
                children.add(new TerminalNodeImpl(lParen));
                children.add(expCtx);
                children.add(new TerminalNodeImpl(rParen));
                parenExpression.children.clear();
                parenExpression.addChildren(children);
                exp =  parenExpression;
            }
            return ParseTreeUtil.getInstance().negateExpression(exp, parser);
        }
    }

    /**
   * @apiNote Create a negative expression of @expCtx,
   * @implNote just wrap the original expression and add a "!" token in the front.
   * @param expCtx
   * @return
   */
    public ExtendContext negateExpression(ExtendContext expCtx, MyParser parser) {
        ExtendContext parent = (ExtendContext) expCtx.getParent();
        if(expCtx.start.getText().equals("!")) {
            ExtendContext notExpression = (ExtendContext) expCtx.getChild(1);;
            notExpression.setParent(expCtx.getParent());
            return notExpression;
        } else {
            ExtendContext notExpression = (ExtendContext) parser.createExpression(parent, expCtx.invokingState);
            List<ParseTree> children = new ArrayList<>();
            ParseTree bangChild = new TerminalNodeImpl(parser.getTokenFactory().create(parser.getBang(), "!"));
            ExtendContext wrapped = encapsulateExpWithParen(expCtx, parser);
            if (expCtx.getChildCount() == 3
                        && expCtx.getChild(0) instanceof TerminalNode ter1 && ter1.getSymbol().getType() == parser.getLParen()
                && expCtx.getChild(2) instanceof TerminalNode ter2 && ter2.getSymbol().getType() == parser.getRParen()) {
                    wrapped =  expCtx;
            }

            if (expCtx.getChildCount() == 1) {
                wrapped =  expCtx;
            }

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
            wrapped =  parenExpression;

            children.add(bangChild);
            children.add(wrapped);
            notExpression.children.clear();
            notExpression.addChildren(children);
            return notExpression;
        }
    }