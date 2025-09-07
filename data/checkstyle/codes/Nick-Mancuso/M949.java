    private void updateTreeTable(String xpath, Deque<DetailAST> nodes) {
        if (nodes.isEmpty()) {
            xpathEditor.setText("No elements matching XPath query '"
                    + xpath + "' found.");
        }
        else {
            for (DetailAST node : nodes) {
                expandTreeTableByPath(node);
                makeCodeSelection();
            }
            xpathEditor.setText(getAllMatchingXpathQueriesText(nodes));
        }
    }
