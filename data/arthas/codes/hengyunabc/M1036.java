    @Override
    public void accept(TtyEvent event, Integer key) {
        switch (event) {
            case INTR:
                term.handleIntr(key);
                break;
            case EOF:
                term.handleEof(key);
                break;
            case SUSP:
                term.handleSusp(key);
                break;
        }
    }
