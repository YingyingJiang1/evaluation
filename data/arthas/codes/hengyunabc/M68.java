    private List<Map<String, Object>> getErrorList() {
        List<Map<String, Object>> messages = new ArrayList<Map<String, Object>>();
        if (diagnostics != null) {
            for (Diagnostic<? extends JavaFileObject> diagnostic : diagnostics) {
                Map<String, Object> message = new HashMap<String, Object>(2);
                message.put("line", diagnostic.getLineNumber());
                message.put("message", diagnostic.getMessage(Locale.US));
                messages.add(message);
            }

        }
        return messages;
    }
