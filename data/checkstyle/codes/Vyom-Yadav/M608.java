    private TypeDeclDesc getSuperClassOfAnonInnerClass(DetailAST literalNewAst) {
        TypeDeclDesc obtainedClass = null;
        final String shortNameOfClass = CheckUtil.getShortNameOfAnonInnerClass(literalNewAst);
        if (packageName != null && shortNameOfClass.startsWith(packageName)) {
            final Optional<TypeDeclDesc> classWithCompletePackageName =
                    typeDeclAstToTypeDeclDesc.values()
                    .stream()
                    .filter(typeDeclDesc -> {
                        return typeDeclDesc.getQualifiedName().equals(shortNameOfClass);
                    })
                    .findFirst();
            if (classWithCompletePackageName.isPresent()) {
                obtainedClass = classWithCompletePackageName.orElseThrow();
            }
        }
        else {
            final List<TypeDeclDesc> typeDeclWithSameName = typeDeclWithSameName(shortNameOfClass);
            if (!typeDeclWithSameName.isEmpty()) {
                obtainedClass = getClosestMatchingTypeDeclaration(
                        anonInnerAstToTypeDeclDesc.get(literalNewAst).getQualifiedName(),
                        typeDeclWithSameName);
            }
        }
        return obtainedClass;
    }
