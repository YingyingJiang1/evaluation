    @Argument(index = 0, argName = "cmd", required = true)
    @Description("command name (start status stop dump)")
    public void setCmd(String cmd) {
        this.cmd = cmd;
    }
