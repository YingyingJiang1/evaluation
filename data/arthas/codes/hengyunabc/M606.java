    private List<String> events() {
        List<String> result = new ArrayList<String>();

        String execute;
        try {
            /**
             * <pre>
               Basic events:
                  cpu
                  alloc
                  lock
                  wall
                  itimer
             * </pre>
             */
            execute = this.profilerInstance().execute("list");
        } catch (Throwable e) {
            // ignore
            return result;
        }
        String lines[] = execute.split("\\r?\\n");

        for (String line : lines) {
            if (line.startsWith(" ")) {
                result.add(line.trim());
            }
        }
        return result;
    }
