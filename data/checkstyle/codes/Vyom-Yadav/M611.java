    private boolean hasSameNameAsSuperClass(String superClassName, TypeDeclDesc typeDeclDesc) {
        final boolean result;
        if (packageName == null && typeDeclDesc.getDepth() == 0) {
            result = typeDeclDesc.getQualifiedName().equals(superClassName);
        }
        else {
            result = typeDeclDesc.getQualifiedName()
                    .endsWith(PACKAGE_SEPARATOR + superClassName);
        }
        return result;
    }
