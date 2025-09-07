        private static void addAstChild(DetailAstPair pair, DetailAstImpl ast) {
            if (ast != null) {
                if (pair.root == null) {
                    pair.root = ast;
                }
                else {
                    pair.child.setNextSibling(ast);
                }
                pair.child = ast;
            }
        }
