    @Option(shortName = "s", longName = "settings")
    @Description("Settings file(s), e.g. profile or default. See JRE_HOME/lib/jfr (STRING , default)")
    public void setSettings(String settings) {
        this.settings = settings;
    }
