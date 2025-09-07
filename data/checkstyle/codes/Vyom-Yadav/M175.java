    @Override
    public void auditFinished(AuditEvent event) {
        errorWriter.flush();
        if (closeErrorWriter) {
            errorWriter.close();
        }
    }
