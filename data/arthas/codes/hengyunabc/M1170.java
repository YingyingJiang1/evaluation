    @Override
    public TermServer termHandler(Handler<Term> handler) {
        termHandler = handler;
        return this;
    }
