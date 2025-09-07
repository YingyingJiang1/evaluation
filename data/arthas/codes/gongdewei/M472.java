    private static TableElement renderTable(Collection<ClassLoaderVO> classLoaderInfos) {
        TableElement table = new TableElement().leftCellPadding(1).rightCellPadding(1);
        table.add(new RowElement().style(Decoration.bold.bold()).add("name", "loadedCount", "hash", "parent"));
        for (ClassLoaderVO classLoaderVO : classLoaderInfos) {
            table.row(classLoaderVO.getName(), "" + classLoaderVO.getLoadedCount(), classLoaderVO.getHash(), classLoaderVO.getParent());
        }
        return table;
    }
