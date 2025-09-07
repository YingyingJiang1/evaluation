    private static void registerCustomTypes(ConvertUtilsBean cub) {
        cub.register(new PatternConverter(), Pattern.class);
        cub.register(new PatternArrayConverter(), Pattern[].class);
        cub.register(new SeverityLevelConverter(), SeverityLevel.class);
        cub.register(new ScopeConverter(), Scope.class);
        cub.register(new UriConverter(), URI.class);
        cub.register(new RelaxedAccessModifierArrayConverter(), AccessModifierOption[].class);
    }
