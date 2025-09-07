    private AbstractFrame findClassFrame(DetailAST name, boolean lookForMethod) {
        AbstractFrame frame = current.peek();

        while (true) {
            frame = findFrame(frame, name, lookForMethod);

            if (frame == null || frame instanceof ClassFrame) {
                break;
            }

            frame = frame.getParent();
        }

        return frame;
    }
