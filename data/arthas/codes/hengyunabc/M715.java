    public static void drawPlayResult(TableElement table, ObjectVO returnObjVO,
                               int sizeLimit, double cost) {
        // 执行成功:输出成功状态
        table.row("IS-RETURN", "" + true);
        table.row("IS-EXCEPTION", "" + false);
        table.row("COST(ms)", "" + cost);

        // 执行成功:输出成功结果
        if (returnObjVO.needExpand()) {
            table.row("RETURN-OBJ", new ObjectView(sizeLimit, returnObjVO).draw());
        } else {
            table.row("RETURN-OBJ", "" + StringUtils.objectToString(returnObjVO.getObject()));
        }
    }
