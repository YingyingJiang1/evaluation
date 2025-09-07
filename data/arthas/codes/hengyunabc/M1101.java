    public ShellImpl init() {
        term.interruptHandler(new InterruptHandler(this));
        term.suspendHandler(new SuspendHandler(this));
        term.closeHandler(new CloseHandler(this));

        if (welcome != null && welcome.length() > 0) {
            term.write(welcome + "\n");
        }
        return this;
    }
