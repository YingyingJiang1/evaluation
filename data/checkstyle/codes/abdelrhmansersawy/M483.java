    public static List<String> getLines(String headerFile, URI uri) {
        final List<String> readerLines = new ArrayList<>();
        try (LineNumberReader lineReader = new LineNumberReader(
                new InputStreamReader(
                        new BufferedInputStream(uri.toURL().openStream()),
                        StandardCharsets.UTF_8)
        )) {
            String line;
            do {
                line = lineReader.readLine();
                if (line != null) {
                    readerLines.add(line);
                }
            } while (line != null);
        }
        catch (final IOException exc) {
            throw new IllegalArgumentException("unable to load header file " + headerFile, exc);
        }

        if (readerLines.isEmpty()) {
            throw new IllegalArgumentException("Header file is empty: " + headerFile);
        }
        return readerLines;
    }
