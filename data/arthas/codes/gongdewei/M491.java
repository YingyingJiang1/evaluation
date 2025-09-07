    public String drawTree(TraceNode root) {

        //reset status
        maxCostNode = null;
        findMaxCostNode(root);

        final StringBuilder treeSB = new StringBuilder(2048);

        final Ansi highlighted = Ansi.ansi().fg(Ansi.Color.RED);

        recursive(0, true, "", root, new Callback() {

            @Override
            public void callback(int deep, boolean isLast, String prefix, TraceNode node) {
                treeSB.append(prefix).append(isLast ? STEP_FIRST_CHAR : STEP_NORMAL_CHAR);
                renderNode(treeSB, node, highlighted);
                if (!StringUtils.isBlank(node.getMark())) {
                    treeSB.append(" [").append(node.getMark()).append(node.marks() > 1 ? "," + node.marks() : "").append("]");
                }
                treeSB.append("\n");
            }

        });

        return treeSB.toString();
    }
