    public static String nullSafeToString(Object obj) {
        if(obj == null) {
            return "null";
        } else if(obj instanceof String) {
            return (String)obj;
        } else if(obj instanceof Object[]) {
            return nullSafeToString((Object[])((Object[])obj));
        } else if(obj instanceof boolean[]) {
            return nullSafeToString((boolean[])((boolean[])obj));
        } else if(obj instanceof byte[]) {
            return nullSafeToString((byte[])((byte[])obj));
        } else if(obj instanceof char[]) {
            return nullSafeToString((char[])((char[])obj));
        } else if(obj instanceof double[]) {
            return nullSafeToString((double[])((double[])obj));
        } else if(obj instanceof float[]) {
            return nullSafeToString((float[])((float[])obj));
        } else if(obj instanceof int[]) {
            return nullSafeToString((int[])((int[])obj));
        } else if(obj instanceof long[]) {
            return nullSafeToString((long[])((long[])obj));
        } else if(obj instanceof short[]) {
            return nullSafeToString((short[])((short[])obj));
        } else {
            String str = obj.toString();
            return str != null?str:"";
        }
    }
