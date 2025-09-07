    protected void abortProcess(CommandProcess process, int limit) {
        process.write("Command execution times exceed limit: " + limit
                + ", so command will exit. You can set it with -n option.\n");
        process.end();
    }
