    private Set<String> actions() {
        Set<String> values = new HashSet<String>();
        for (VmToolAction action : VmToolAction.values()) {
            values.add(action.toString());
        }
        return values;
    }
