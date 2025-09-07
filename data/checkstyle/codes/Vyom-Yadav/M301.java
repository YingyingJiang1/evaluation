    private static int getAnonSuperTypeMatchingCount(String patternTypeDeclaration,
                                                    String typeDeclarationToBeMatched) {
        final int typeDeclarationToBeMatchedLength = typeDeclarationToBeMatched.length();
        final int minLength = Math
            .min(typeDeclarationToBeMatchedLength, patternTypeDeclaration.length());
        final char packageSeparator = PACKAGE_SEPARATOR.charAt(0);
        final boolean shouldCountBeUpdatedAtLastCharacter =
            typeDeclarationToBeMatchedLength > minLength
                && typeDeclarationToBeMatched.charAt(minLength) == packageSeparator;

        int result = 0;
        for (int idx = 0;
             idx < minLength
                 && patternTypeDeclaration.charAt(idx) == typeDeclarationToBeMatched.charAt(idx);
             idx++) {

            if (idx == minLength - 1 && shouldCountBeUpdatedAtLastCharacter
                || patternTypeDeclaration.charAt(idx) == packageSeparator) {
                result = idx;
            }
        }
        return result;
    }
