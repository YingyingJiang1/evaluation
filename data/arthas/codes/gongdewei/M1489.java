    public static void processClassNames(Collection<Class<?>> classes, int pageSize, PaginationHandler<List<String>> handler) {
        List<String> classNames = new ArrayList<String>(pageSize);
        int segment = 0;
        for (Class aClass : classes) {
            classNames.add(aClass.getName());
            //slice segment
            if(classNames.size() >= pageSize) {
                handler.handle(classNames, segment++);
                classNames = new ArrayList<String>(pageSize);
            }
        }
        //last segment
        if (classNames.size() > 0) {
            handler.handle(classNames, segment++);
        }
    }
