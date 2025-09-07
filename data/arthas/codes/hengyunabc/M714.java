    public static void drawWatchResults(TableElement table, Map<Integer, ObjectVO> watchResults, Integer sizeLimit) {
        for (Map.Entry<Integer, ObjectVO> entry : watchResults.entrySet()) {
            ObjectVO objectVO = entry.getValue();
            table.row("" + entry.getKey(), "" +
                    (objectVO.needExpand() ? new ObjectView(sizeLimit, objectVO).draw() : StringUtils.objectToString(objectVO.getObject())));
        }
    }
