    public void addChild(ClassLoaderVO child){
        if (this.children == null){
            this.children = new ArrayList<ClassLoaderVO>();
        }
        this.children.add(child);
    }
