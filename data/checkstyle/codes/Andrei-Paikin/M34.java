    private static String getText(DetailNode parentNode) {
        return Arrays.stream(parentNode.getChildren())
                .filter(child -> child.getType() == JavadocTokenTypes.TEXT)
                .map(node -> QUOTE_PATTERN.matcher(node.getText().trim()).replaceAll(""))
                .collect(Collectors.joining(" "));
    }
