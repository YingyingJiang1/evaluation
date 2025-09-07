    @Override
    public void parse(Reader source, Sink sink, String reference) throws ParseException {
        try (StringWriter contentWriter = new StringWriter()) {
            IOUtil.copy(source, contentWriter);
            sourceContent = contentWriter.toString();
            super.parse(new StringReader(sourceContent), sink, reference);
        }
        catch (IOException ioException) {
            throw new ParseException("Error reading the input source", ioException);
        }
        finally {
            sourceContent = null;
        }
    }
