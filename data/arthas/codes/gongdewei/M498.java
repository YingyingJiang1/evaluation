    private void initResultViews() {
        try {
            registerView(RowAffectView.class);

            //basic1000
            registerView(StatusView.class);
            registerView(VersionView.class);
            registerView(MessageView.class);
            registerView(HelpView.class);
            //registerView(HistoryView.class);
            registerView(EchoView.class);
            registerView(CatView.class);
            registerView(Base64View.class);
            registerView(OptionsView.class);
            registerView(SystemPropertyView.class);
            registerView(SystemEnvView.class);
            registerView(PwdView.class);
            registerView(VMOptionView.class);
            registerView(SessionView.class);
            registerView(ResetView.class);
            registerView(ShutdownView.class);

            //klass100
            registerView(ClassLoaderView.class);
            registerView(DumpClassView.class);
            registerView(GetStaticView.class);
            registerView(JadView.class);
            registerView(MemoryCompilerView.class);
            registerView(OgnlView.class);
            registerView(RedefineView.class);
            registerView(RetransformView.class);
            registerView(SearchClassView.class);
            registerView(SearchMethodView.class);

            //logger
            registerView(LoggerView.class);

            //monitor2000
            registerView(DashboardView.class);
            registerView(JvmView.class);
            registerView(MemoryView.class);
            registerView(MBeanView.class);
            registerView(PerfCounterView.class);
            registerView(ThreadView.class);
            registerView(ProfilerView.class);
            registerView(EnhancerView.class);
            registerView(MonitorView.class);
            registerView(StackView.class);
            registerView(TimeTunnelView.class);
            registerView(TraceView.class);
            registerView(WatchView.class);
            registerView(VmToolView.class);
            registerView(JFRView.class);

        } catch (Throwable e) {
            logger.error("register result view failed", e);
        }
    }
