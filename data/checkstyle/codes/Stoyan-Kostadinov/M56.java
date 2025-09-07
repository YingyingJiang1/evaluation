    private void processMacroEnd(Sink sink) throws MacroExecutionException {
        final MacroRequest request = new MacroRequest(sourceContent,
                new XdocsTemplateParser(), macroParameters,
                new File(TEMP_DIR));

        try {
            executeMacro(macroName, request, sink);
        }
        catch (MacroNotFoundException exception) {
            final String message = String.format(Locale.ROOT, "Macro '%s' not found.", macroName);
            throw new MacroExecutionException(message, exception);
        }

        reinitializeMacroFields();
    }
