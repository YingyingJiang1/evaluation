    public List<Token> getHiddenAfter() {
        List<Token> returnList = null;
        if (hiddenAfter != null) {
            returnList = UnmodifiableCollectionUtil.unmodifiableList(hiddenAfter);
        }
        return returnList;
    }
