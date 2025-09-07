    @Override
    protected IndentLevel getIndentImpl() {
        return new IndentLevel(getParent().getIndent(),
            getIndentCheck().getCaseIndent());
    }
