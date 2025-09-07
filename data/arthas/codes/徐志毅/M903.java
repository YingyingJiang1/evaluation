    private boolean isMatchOptionAnnotation(Field optionField, Matcher<String> optionNameMatcher) {
        if (!optionField.isAnnotationPresent(Option.class)) {
            return false;
        }
        final Option optionAnnotation = optionField.getAnnotation(Option.class);
        return optionAnnotation != null && optionNameMatcher.matching(optionAnnotation.name());
    }
