    private List<String> diagnosticToString(List<Diagnostic<? extends JavaFileObject>> diagnostics) {

        List<String> diagnosticMessages = new ArrayList<String>();

        for (Diagnostic<? extends JavaFileObject> diagnostic : diagnostics) {
            diagnosticMessages.add(
                            "line: " + diagnostic.getLineNumber() + ", message: " + diagnostic.getMessage(Locale.US));
        }

        return diagnosticMessages;

    }
