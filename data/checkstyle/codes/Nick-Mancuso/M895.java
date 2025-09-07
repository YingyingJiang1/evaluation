        private static void makeAstRoot(DetailAstPair pair, DetailAstImpl ast) {
            ast.addChild(pair.root);
            pair.child = pair.root;
            pair.advanceChildToEnd();
            pair.root = ast;
        }
