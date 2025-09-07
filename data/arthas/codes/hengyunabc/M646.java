    @Override
    public void process(final CommandProcess process) {
        // 检查参数
        checkArguments();

        // ctrl-C support
        process.interruptHandler(new CommandInterruptHandler(process));
        // q exit support
        process.stdinHandler(new QExitHandler(process));

        if (isTimeTunnel) {
            enhance(process);
        } else if (isPlay) {
            processPlay(process);
        } else if (isList) {
            processList(process);
        } else if (isDeleteAll) {
            processDeleteAll(process);
        } else if (isDelete) {
            processDelete(process);
        } else if (hasSearchExpress()) {
            processSearch(process);
        } else if (index != null) {
            if (hasWatchExpress()) {
                processWatch(process);
            } else {
                processShow(process);
            }
        }
    }
