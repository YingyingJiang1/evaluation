    @Override
    public String toString() {
        return String.format("Affect(row-cnt:%d) cost in %s ms.",
                rCnt(),
                cost());
    }
