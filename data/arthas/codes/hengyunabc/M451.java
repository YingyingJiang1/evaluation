    private String drawReturn() {
        final StringBuilder returnSB = new StringBuilder();
        final Class<?> returnTypeClass = method.getReturnType();
        returnSB.append(StringUtils.classname(returnTypeClass)).append("\n");
        return returnSB.toString();
    }
