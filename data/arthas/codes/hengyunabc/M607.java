    private Set<String> actions() {
        Set<String> values = new HashSet<String>();
        for (ProfilerAction action : ProfilerAction.values()) {
            values.add(action.toString());
        }
        return values;
    }
