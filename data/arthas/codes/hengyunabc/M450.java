    private String drawParameters() {
        final StringBuilder paramsSB = new StringBuilder();
        final Class<?>[] paramTypes = method.getParameterTypes();
        if (paramTypes.length > 0) {
            for (Class<?> clazz : paramTypes) {
                paramsSB.append(StringUtils.classname(clazz)).append("\n");
            }
        }
        return paramsSB.toString();
    }
