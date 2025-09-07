    public static JavaObject toJavaObjectWithExpand(Object obj, int expand){
        int depth;
        if(expand <= 0){
            depth = MAX_DEPTH - 1;
        }else if(expand >= MAX_DEPTH){
            depth = 0;
        }else {
            depth = MAX_DEPTH - expand;
        }
        return toJavaObject(obj, depth);
    }
