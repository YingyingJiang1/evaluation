    private static void addLastSibling(DetailAstImpl self, DetailAstImpl sibling) {
        DetailAstImpl nextSibling = self;
        if (nextSibling != null) {
            while (nextSibling.getNextSibling() != null) {
                nextSibling = nextSibling.getNextSibling();
            }
            nextSibling.setNextSibling(sibling);
        }
    }
