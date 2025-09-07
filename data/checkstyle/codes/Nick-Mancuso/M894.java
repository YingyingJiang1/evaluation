        private void advanceChildToEnd() {
            while (child.getNextSibling() != null) {
                child = child.getNextSibling();
            }
        }
