        public Deque<VariableDesc> getUpdatedCopyOfVarStack(DetailAST literalNewAst) {
            final DetailAST updatedScope = literalNewAst;
            final Deque<VariableDesc> instAndClassVarDeque = new ArrayDeque<>();
            instanceAndClassVarStack.forEach(instVar -> {
                final VariableDesc variableDesc = new VariableDesc(instVar.getName(),
                        updatedScope);
                variableDesc.registerAsInstOrClassVar();
                instAndClassVarDeque.push(variableDesc);
            });
            return instAndClassVarDeque;
        }
