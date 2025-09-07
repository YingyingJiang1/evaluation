    @Override
    public void auditFinished(AuditEvent event) {
        if (isXmlHeaderPrinted) {
            writer.println("</suppressions>");
        }

        writer.flush();
        if (closeStream) {
            writer.close();
        }
    }
