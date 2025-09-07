    public static void drawThrowException(TableElement table, TimeFragmentVO tf) {
        if (tf.isThrow()) {
            //noinspection ThrowableResultOfMethodCallIgnored
            ObjectVO throwableVO = tf.getThrowExp();
            if (throwableVO.needExpand()) {
                table.row("THROW-EXCEPTION", new ObjectView(throwableVO).draw());
            } else {
                StringWriter stringWriter = new StringWriter();
                PrintWriter printWriter = new PrintWriter(stringWriter);
                try {
                    ((Throwable) throwableVO.getObject()).printStackTrace(printWriter);
                    table.row("THROW-EXCEPTION", stringWriter.toString());
                } finally {
                    printWriter.close();
                }
            }
        }
    }
