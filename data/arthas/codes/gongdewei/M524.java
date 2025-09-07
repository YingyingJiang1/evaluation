    private static TableElement drawRuntimeInfo(RuntimeInfoVO runtimeInfo) {
        TableElement table = new TableElement(1, 1).rightCellPadding(1);
        table.add(new RowElement().style(Decoration.bold.fg(Color.black).bg(Color.white)).add("Runtime", ""));

        table.row("os.name", runtimeInfo.getOsName());
        table.row("os.version", runtimeInfo.getOsVersion());
        table.row("java.version", runtimeInfo.getJavaVersion());
        table.row("java.home", runtimeInfo.getJavaHome());
        table.row("systemload.average", String.format("%.2f", runtimeInfo.getSystemLoadAverage()));
        table.row("processors", "" + runtimeInfo.getProcessors());
        table.row("timestamp/uptime", new Date(runtimeInfo.getTimestamp()).toString() + "/" + runtimeInfo.getUptime() + "s");
        return table;
    }
