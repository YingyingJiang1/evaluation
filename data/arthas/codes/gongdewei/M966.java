    public void end() {
        current.end();
        if (current.parent() != null) {
            //TODO 为什么会到达这里？ 调用end次数比begin多？
            current = current.parent();
        }
    }
