    public static boolean containsElement(Object[] array, Object element) {
        if(array == null) {
            return false;
        } else {
            Object[] var2 = array;
            int var3 = array.length;

            for(int var4 = 0; var4 < var3; ++var4) {
                Object arrayEle = var2[var4];
                if(nullSafeEquals(arrayEle, element)) {
                    return true;
                }
            }

            return false;
        }
    }
