    public static void completeUsage(Completion completion, CLI cli) {
        Tty tty = completion.session().get(Session.TTY);
        String usage = StyledUsageFormatter.styledUsage(cli, tty.width());
        completion.complete(Collections.singletonList(usage));
    }
