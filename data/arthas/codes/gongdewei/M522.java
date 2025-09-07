    private static TableElement drawGcInfo(List<GcInfoVO> gcInfos) {
        TableElement table = new TableElement(1, 1).rightCellPadding(1);
        table.add(new RowElement().style(Decoration.bold.fg(Color.black).bg(Color.white)).add("GC", ""));
        for (GcInfoVO gcInfo : gcInfos) {
            table.add(new RowElement().style(Decoration.bold.bold()).add("gc." + gcInfo.getName() + ".count",
                    "" + gcInfo.getCollectionCount()));
            table.row("gc." + gcInfo.getName() + ".time(ms)", "" + gcInfo.getCollectionTime());
        }
        return table;
    }
