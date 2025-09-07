    public static Set<Path> getXdocsTemplatesFilePaths() throws MacroExecutionException {
        final Path directory = Path.of("src/site/xdoc");
        try (Stream<Path> stream = Files.find(directory, Integer.MAX_VALUE,
                (path, attr) -> {
                    return attr.isRegularFile()
                            && path.toString().endsWith(".xml.template");
                })) {
            return stream.collect(Collectors.toUnmodifiableSet());
        }
        catch (IOException ioException) {
            throw new MacroExecutionException("Failed to find xdocs templates", ioException);
        }
    }
