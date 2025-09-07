    public void readline(String prompt, Handler<String> lineHandler, Handler<Completion> completionHandler) {
        if (conn.getStdinHandler() != echoHandler) {
            throw new IllegalStateException();
        }
        if (inReadline) {
            throw new IllegalStateException();
        }
        inReadline = true;
        readline.readline(conn, prompt, new RequestHandler(this, lineHandler), new CompletionHandler(completionHandler, session));
    }
