    private void checkCounters(MethodCounter counter, DetailAST ast) {
        checkMax(maxPrivate, counter.value(Scope.PRIVATE),
                 MSG_PRIVATE_METHODS, ast);
        checkMax(maxPackage, counter.value(Scope.PACKAGE),
                 MSG_PACKAGE_METHODS, ast);
        checkMax(maxProtected, counter.value(Scope.PROTECTED),
                 MSG_PROTECTED_METHODS, ast);
        checkMax(maxPublic, counter.value(Scope.PUBLIC),
                 MSG_PUBLIC_METHODS, ast);
        checkMax(maxTotal, counter.getTotal(), MSG_MANY_METHODS, ast);
    }
