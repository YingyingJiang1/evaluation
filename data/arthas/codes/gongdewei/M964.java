    private TraceNode findChild(TraceNode node, String className, String methodName, int lineNumber) {
        List<TraceNode> childList = node.getChildren();
        if (childList != null) {
            //less memory than foreach/iterator
            for (int i = 0; i < childList.size(); i++) {
                TraceNode child = childList.get(i);
                if (matchNode(child, className, methodName, lineNumber)) {
                    return child;
                }
            }
        }
        return null;
    }
