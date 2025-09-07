    public final void setCustomImportOrderRules(String... rules) {
        Arrays.stream(rules)
                .map(GROUP_SEPARATOR_PATTERN::split)
                .flatMap(Arrays::stream)
                .forEach(this::addRulesToList);

        customImportOrderRules.add(NON_GROUP_RULE_GROUP);
    }
