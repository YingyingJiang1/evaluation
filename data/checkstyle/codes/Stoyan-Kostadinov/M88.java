        private static List<DetailNode> getDescriptionNodes(DetailNode javadoc) {
            final DetailNode[] children = javadoc.getChildren();
            final List<DetailNode> descriptionNodes = new ArrayList<>();
            for (final DetailNode child : children) {
                if (isEndOfDescription(child)) {
                    break;
                }
                descriptionNodes.add(child);
            }
            return descriptionNodes;
        }
