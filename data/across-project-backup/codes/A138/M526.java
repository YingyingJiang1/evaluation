    private TableElement drawTomcatInfo(TomcatInfoVO tomcatInfo) {
        if (tomcatInfo == null) {
            return null;
        }

        //header
        TableElement table = new TableElement(1, 1).rightCellPadding(1);
        table.add(new RowElement().style(Decoration.bold.fg(Color.black).bg(Color.white)).add("Tomcat", ""));

        if (tomcatInfo.getConnectorStats() != null) {
            for (TomcatInfoVO.ConnectorStats connectorStat : tomcatInfo.getConnectorStats()) {
                table.add(new RowElement().style(Decoration.bold.bold()).add("connector", connectorStat.getName()));
                table.row("QPS", String.format("%.2f", connectorStat.getQps()));
                table.row("RT(ms)", String.format("%.2f", connectorStat.getRt()));
                table.row("error/s", String.format("%.2f", connectorStat.getError()));
                table.row("received/s", formatBytes(connectorStat.getReceived()));
                table.row("sent/s", formatBytes(connectorStat.getSent()));
            }
        }

        if (tomcatInfo.getThreadPools() != null) {
            for (TomcatInfoVO.ThreadPool threadPool : tomcatInfo.getThreadPools()) {
                table.add(new RowElement().style(Decoration.bold.bold()).add("threadpool", threadPool.getName()));
                table.row("busy", "" + threadPool.getBusy());
                table.row("total", "" + threadPool.getTotal());
            }
        }
        return table;
    }
