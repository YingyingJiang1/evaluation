    void checkPending() {
        if (stdinHandler != null && readline.hasEvent()) {
            stdinHandler.handle(Helper.fromCodePoints(readline.nextEvent().buffer().array()));
            checkPending();
        }
    }
