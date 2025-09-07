    private void initCommands(List<String> disabledCommands) {
        List<Class<? extends AnnotatedCommand>> commandClassList = new ArrayList<Class<? extends AnnotatedCommand>>(33);
        commandClassList.add(HelpCommand.class);
        commandClassList.add(AuthCommand.class);
        commandClassList.add(KeymapCommand.class);
        commandClassList.add(SearchClassCommand.class);
        commandClassList.add(SearchMethodCommand.class);
        commandClassList.add(ClassLoaderCommand.class);
        commandClassList.add(JadCommand.class);
        commandClassList.add(GetStaticCommand.class);
        commandClassList.add(MonitorCommand.class);
        commandClassList.add(StackCommand.class);
        commandClassList.add(ThreadCommand.class);
        commandClassList.add(TraceCommand.class);
        commandClassList.add(WatchCommand.class);
        commandClassList.add(TimeTunnelCommand.class);
        commandClassList.add(JvmCommand.class);
        commandClassList.add(MemoryCommand.class);
        commandClassList.add(PerfCounterCommand.class);
        // commandClassList.add(GroovyScriptCommand.class);
        commandClassList.add(OgnlCommand.class);
        commandClassList.add(MemoryCompilerCommand.class);
        commandClassList.add(RedefineCommand.class);
        commandClassList.add(RetransformCommand.class);
        commandClassList.add(DashboardCommand.class);
        commandClassList.add(DumpClassCommand.class);
        commandClassList.add(HeapDumpCommand.class);
        commandClassList.add(JulyCommand.class);
        commandClassList.add(ThanksCommand.class);
        commandClassList.add(OptionsCommand.class);
        commandClassList.add(ClsCommand.class);
        commandClassList.add(ResetCommand.class);
        commandClassList.add(VersionCommand.class);
        commandClassList.add(SessionCommand.class);
        commandClassList.add(SystemPropertyCommand.class);
        commandClassList.add(SystemEnvCommand.class);
        commandClassList.add(VMOptionCommand.class);
        commandClassList.add(LoggerCommand.class);
        commandClassList.add(HistoryCommand.class);
        commandClassList.add(CatCommand.class);
        commandClassList.add(Base64Command.class);
        commandClassList.add(EchoCommand.class);
        commandClassList.add(PwdCommand.class);
        commandClassList.add(MBeanCommand.class);
        commandClassList.add(GrepCommand.class);
        commandClassList.add(TeeCommand.class);
        commandClassList.add(ProfilerCommand.class);
        commandClassList.add(VmToolCommand.class);
        commandClassList.add(StopCommand.class);
        try {
            if (ClassLoader.getSystemClassLoader().getResource("jdk/jfr/Recording.class") != null) {
                commandClassList.add(JFRCommand.class);
            }
        } catch (Throwable e) {
            logger.error("This jdk version not support jfr command");
        }

        for (Class<? extends AnnotatedCommand> clazz : commandClassList) {
            Name name = clazz.getAnnotation(Name.class);
            if (name != null && name.value() != null) {
                if (disabledCommands.contains(name.value())) {
                    continue;
                }
            }
            commands.add(Command.create(clazz));
        }
    }
