    @Override
    public void beginTree(DetailAST rootAST) {
        frames = new HashMap<>();
        current.clear();

        final Deque<AbstractFrame> frameStack = new LinkedList<>();
        DetailAST curNode = rootAST;
        while (curNode != null) {
            collectDeclarations(frameStack, curNode);
            DetailAST toVisit = curNode.getFirstChild();
            while (curNode != null && toVisit == null) {
                endCollectingDeclarations(frameStack, curNode);
                toVisit = curNode.getNextSibling();
                curNode = curNode.getParent();
            }
            curNode = toVisit;
        }
    }
