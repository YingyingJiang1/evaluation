    public List<String> getExtensionTypes(boolean output, String operationName) {
        if (outputToFileTypes.size() == 0) {
            outputToFileTypes.put("PDF", Arrays.asList("pdf"));
            outputToFileTypes.put(
                    "IMAGE",
                    Arrays.asList(
                            "png", "jpg", "jpeg", "gif", "webp", "bmp", "tif", "tiff", "svg", "psd",
                            "ai", "eps"));
            outputToFileTypes.put(
                    "ZIP",
                    Arrays.asList("zip", "rar", "7z", "tar", "gz", "bz2", "xz", "lz", "lzma", "z"));
            outputToFileTypes.put("WORD", Arrays.asList("doc", "docx", "odt", "rtf"));
            outputToFileTypes.put("CSV", Arrays.asList("csv"));
            outputToFileTypes.put("JS", Arrays.asList("js", "jsx"));
            outputToFileTypes.put("HTML", Arrays.asList("html", "htm", "xhtml"));
            outputToFileTypes.put("JSON", Arrays.asList("json"));
            outputToFileTypes.put("TXT", Arrays.asList("txt", "text", "md", "markdown"));
            outputToFileTypes.put("PPT", Arrays.asList("ppt", "pptx", "odp"));
            outputToFileTypes.put("XML", Arrays.asList("xml", "xsd", "xsl"));
            outputToFileTypes.put(
                    "BOOK", Arrays.asList("epub", "mobi", "azw3", "fb2", "txt", "docx"));
            // type.
        }
        if (apiDocsJsonRootNode == null || apiDocumentation.size() == 0) {
            loadApiDocumentation();
        }
        if (!apiDocumentation.containsKey(operationName)) {
            return null;
        }
        ApiEndpoint endpoint = apiDocumentation.get(operationName);
        String description = endpoint.getDescription();
        Pattern pattern = null;
        if (output) {
            pattern = Pattern.compile("Output:(\\w+)");
        } else {
            pattern = Pattern.compile("Input:(\\w+)");
        }
        Matcher matcher = pattern.matcher(description);
        while (matcher.find()) {
            String type = matcher.group(1).toUpperCase();
            if (outputToFileTypes.containsKey(type)) {
                return outputToFileTypes.get(type);
            }
        }
        return null;
    }
