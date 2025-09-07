    private static TableElement renderStat(Map<String, ClassLoaderStat> classLoaderStats) {
        TableElement table = new TableElement().leftCellPadding(1).rightCellPadding(1);
        table.add(new RowElement().style(Decoration.bold.bold()).add("name", "numberOfInstances", "loadedCountTotal"));
        for (Map.Entry<String, ClassLoaderStat> entry : classLoaderStats.entrySet()) {
            table.row(entry.getKey(), "" + entry.getValue().getNumberOfInstance(), "" + entry.getValue().getLoadedCount());
        }
        return table;
    }
