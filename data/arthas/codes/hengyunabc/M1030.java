    private void handleExit() {
        String msg = Ansi.ansi().fg(Ansi.Color.GREEN).a("Session has been terminated.\n"
                + "Arthas is still running in the background.\n"
                + "To completely shutdown arthas, please execute the 'stop' command.\n").reset().toString();
        term.write(msg);
        term.close();
    }
