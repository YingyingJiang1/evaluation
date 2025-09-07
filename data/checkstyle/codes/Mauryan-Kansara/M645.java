    @Override
    public void visitToken(DetailAST ast) {
        // list of all child ASTs
        final List<DetailAST> children = getChildList(ast);

        // find first constructor
        final DetailAST firstConstructor = children.stream()
                .filter(ConstructorsDeclarationGroupingCheck::isConstructor)
                .findFirst()
                .orElse(null);

        if (firstConstructor != null) {

            // get all children AST after the first constructor
            final List<DetailAST> childrenAfterFirstConstructor =
                    children.subList(children.indexOf(firstConstructor), children.size());

            // find the first index of non-constructor AST after the first constructor, if present
            final Optional<Integer> indexOfFirstNonConstructor = childrenAfterFirstConstructor
                    .stream()
                    .filter(currAst -> !isConstructor(currAst))
                    .findFirst()
                    .map(children::indexOf);

            // list of all children after first non-constructor AST
            final List<DetailAST> childrenAfterFirstNonConstructor = indexOfFirstNonConstructor
                    .map(index -> children.subList(index, children.size()))
                    .orElseGet(ArrayList::new);

            // create a list of all constructors that are not grouped to log
            final List<DetailAST> constructorsToLog = childrenAfterFirstNonConstructor.stream()
                    .filter(ConstructorsDeclarationGroupingCheck::isConstructor)
                    .collect(Collectors.toUnmodifiableList());

            // find the last grouped constructor
            final DetailAST lastGroupedConstructor = childrenAfterFirstConstructor.stream()
                    .takeWhile(ConstructorsDeclarationGroupingCheck::isConstructor)
                    .reduce((first, second) -> second)
                    .orElse(firstConstructor);

            // log all constructors that are not grouped
            constructorsToLog
                    .forEach(ctor -> log(ctor, MSG_KEY, lastGroupedConstructor.getLineNo()));
        }
    }
