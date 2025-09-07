    private static List<String> readFile(String path) throws MacroExecutionException {
        try {
            final Path exampleFilePath = Path.of(path);
            return Files.readAllLines(exampleFilePath);
        }
        catch (IOException ioException) {
            final String message = String.format(Locale.ROOT, "Failed to read %s", path);
            throw new MacroExecutionException(message, ioException);
        }
    }
