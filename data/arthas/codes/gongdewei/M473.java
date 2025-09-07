    private static Element renderTree(Collection<ClassLoaderVO> classLoaderInfos) {
        TreeElement root = new TreeElement();
        for (ClassLoaderVO classLoader : classLoaderInfos) {
            TreeElement child = new TreeElement(classLoader.getName());
            root.addChild(child);
            renderSubtree(child, classLoader);
        }
        return root;
    }
