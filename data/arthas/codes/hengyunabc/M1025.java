    private Job createJob(List<CliToken> tokens) {
        Job job;
        try {
            job = shell.createJob(tokens);
        } catch (Exception e) {
            term.echo(e.getMessage() + "\n");
            shell.readline();
            return null;
        }
        return job;
    }
