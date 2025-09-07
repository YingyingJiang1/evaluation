        private static Details getDetailsForCase(DetailAST caseNode) {
            final DetailAST caseParent = caseNode.getParent();
            final int parentType = caseParent.getType();
            final Optional<DetailAST> lcurly;
            final DetailAST statementList;

            if (parentType == TokenTypes.SWITCH_RULE) {
                statementList = caseParent.findFirstToken(TokenTypes.SLIST);
                lcurly = Optional.ofNullable(statementList);
            }
            else {
                statementList = caseNode.getNextSibling();
                lcurly = Optional.ofNullable(statementList)
                         .map(DetailAST::getFirstChild)
                         .filter(node -> node.getType() == TokenTypes.SLIST);
            }
            final DetailAST rcurly = lcurly.map(DetailAST::getLastChild)
                    .filter(child -> !isSwitchExpression(caseParent))
                    .orElse(null);
            final Optional<DetailAST> nextToken =
                    Optional.ofNullable(lcurly.map(DetailAST::getNextSibling)
                    .orElseGet(() -> getNextToken(caseParent)));

            return new Details(lcurly.orElse(null), rcurly, nextToken.orElse(null), true);
        }
