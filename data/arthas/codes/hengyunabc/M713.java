    public static void drawParameters(TableElement table, ObjectVO[] params) {
        if (params != null) {
            int paramIndex = 0;
            for (ObjectVO param : params) {
                if (param.needExpand()) {
                    table.row("PARAMETERS[" + paramIndex++ + "]", new ObjectView(param).draw());
                } else {
                    table.row("PARAMETERS[" + paramIndex++ + "]", "" + StringUtils.objectToString(param));
                }
            }
        }
    }
