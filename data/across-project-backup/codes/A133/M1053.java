    private Process createCommandProcess(Command command, ListIterator<CliToken> tokens, int jobId, Term term, ResultDistributor resultDistributor) throws IOException {
        List<CliToken> remaining = new ArrayList<CliToken>();
        List<CliToken> pipelineTokens = new ArrayList<CliToken>();
        boolean isPipeline = false;
        RedirectHandler redirectHandler = null;
        List<Function<String, String>> stdoutHandlerChain = new ArrayList<Function<String, String>>();
        String cacheLocation = null;
        while (tokens.hasNext()) {
            CliToken remainingToken = tokens.next();
            if (remainingToken.isText()) {
                String tokenValue = remainingToken.value();
                if ("|".equals(tokenValue)) {
                    isPipeline = true;
                    // 将管道符|之后的部分注入为输出链上的handler
                    injectHandler(stdoutHandlerChain, pipelineTokens);
                    continue;
                } else if (">>".equals(tokenValue) || ">".equals(tokenValue)) {
                    String name = getRedirectFileName(tokens);
                    if (name == null) {
                        // 如果没有指定重定向文件名，那么重定向到以jobid命名的缓存中
                        name = LogUtil.cacheDir() + File.separator + Constants.PID + File.separator + jobId;
                        cacheLocation = name;

                        if (getRedirectJobCount() == 8) {
                            throw new IllegalStateException("The amount of async command that saving result to file can't > 8");
                        }
                    }
                    redirectHandler = new RedirectHandler(name, ">>".equals(tokenValue));
                    break;
                }
            }
            if (isPipeline) {
                pipelineTokens.add(remainingToken);
            } else {
                remaining.add(remainingToken);
            }
        }
        injectHandler(stdoutHandlerChain, pipelineTokens);
        if (redirectHandler != null) {
            stdoutHandlerChain.add(redirectHandler);
            term.write("redirect output file will be: " + redirectHandler.getFilePath()+"\n");
        } else {
            stdoutHandlerChain.add(new TermHandler(term));
            if (GlobalOptions.isSaveResult) {
                stdoutHandlerChain.add(new RedirectHandler());
            }
        }
        ProcessOutput processOutput = new ProcessOutput(stdoutHandlerChain, cacheLocation, term);
        ProcessImpl process = new ProcessImpl(command, remaining, command.processHandler(), processOutput, resultDistributor);
        process.setTty(term);
        return process;
    }
