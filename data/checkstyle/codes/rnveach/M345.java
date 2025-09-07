    @Override
    public AbstractImportControl locateFinest(String forPkg, String forFileName) {
        AbstractImportControl finestMatch = null;
        // Check if we are a match.
        if (matchesExactly(forPkg, forFileName)) {
            finestMatch = this;
        }
        return finestMatch;
    }
