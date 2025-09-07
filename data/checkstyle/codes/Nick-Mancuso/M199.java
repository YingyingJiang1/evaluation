    public static List<NodeInfo> getXpathItems(String xpath, AbstractNode rootNode)
            throws XPathException {
        final XPathEvaluator xpathEvaluator = new XPathEvaluator(Configuration.newConfiguration());
        final XPathExpression xpathExpression = xpathEvaluator.createExpression(xpath);
        final XPathDynamicContext xpathDynamicContext = xpathExpression
                .createDynamicContext(rootNode);
        final List<Item> items = xpathExpression.evaluate(xpathDynamicContext);
        return UnmodifiableCollectionUtil.unmodifiableList(items, NodeInfo.class);
    }
