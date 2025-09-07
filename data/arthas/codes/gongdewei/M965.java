    private boolean matchNode(TraceNode node, String className, String methodName, int lineNumber) {
        if (node instanceof MethodNode) {
            MethodNode methodNode = (MethodNode) node;
            if (lineNumber != methodNode.getLineNumber()) return false;
            if (className != null ? !className.equals(methodNode.getClassName()) : methodNode.getClassName() != null) return false;
            return methodName != null ? methodName.equals(methodNode.getMethodName()) : methodNode.getMethodName() == null;
        }
        return false;
    }
