    private List<TypeDeclDesc> typeDeclWithSameName(String superClassName) {
        return typeDeclAstToTypeDeclDesc.values().stream()
                .filter(typeDeclDesc -> {
                    return hasSameNameAsSuperClass(superClassName, typeDeclDesc);
                })
                .collect(Collectors.toUnmodifiableList());
    }
