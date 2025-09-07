    public static IndentLevel addAcceptable(IndentLevel base, IndentLevel addition) {
        final IndentLevel result = new IndentLevel();
        result.levels.or(base.levels);
        result.levels.or(addition.levels);
        return result;
    }
