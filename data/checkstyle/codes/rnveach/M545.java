    public static IndentLevel addAcceptable(IndentLevel base, int... additions) {
        final IndentLevel result = new IndentLevel();
        result.levels.or(base.levels);
        for (int addition : additions) {
            result.levels.set(addition);
        }
        return result;
    }
