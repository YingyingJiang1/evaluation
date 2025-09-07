    public List<Token> getHiddenBefore() {
        List<Token> returnList = null;
        if (hiddenBefore != null) {
            returnList = UnmodifiableCollectionUtil.unmodifiableList(hiddenBefore);
        }
        return returnList;
    }
