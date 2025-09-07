    public void begin(String className, String methodName, int lineNumber, boolean isInvoking) {
        TraceNode child = findChild(current, className, methodName, lineNumber);
        if (child == null) {
            child = new MethodNode(className, methodName, lineNumber, isInvoking);
            current.addChild(child);
        }
        child.begin();
        current = child;
        nodeCount += 1;
    }
