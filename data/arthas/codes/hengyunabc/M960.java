    public void addRetransformClass(String className) {
        if (retransformClasses == null) {
            retransformClasses = new ArrayList<String>();
        }
        retransformClasses.add(className);
        retransformCount++;
    }
