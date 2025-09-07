            @Override
            public void callback(int deep, boolean isLast, String prefix, TraceNode node) {
                treeSB.append(prefix).append(isLast ? STEP_FIRST_CHAR : STEP_NORMAL_CHAR);
                renderNode(treeSB, node, highlighted);
                if (!StringUtils.isBlank(node.getMark())) {
                    treeSB.append(" [").append(node.getMark()).append(node.marks() > 1 ? "," + node.marks() : "").append("]");
                }
                treeSB.append("\n");
            }
