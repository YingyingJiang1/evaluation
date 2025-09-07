    public static int nullSafeHashCode(Object obj) {
        if(obj == null) {
            return 0;
        } else {
            if(obj.getClass().isArray()) {
                if(obj instanceof Object[]) {
                    return nullSafeHashCode((Object[])((Object[])obj));
                }

                if(obj instanceof boolean[]) {
                    return nullSafeHashCode((boolean[])((boolean[])obj));
                }

                if(obj instanceof byte[]) {
                    return nullSafeHashCode((byte[])((byte[])obj));
                }

                if(obj instanceof char[]) {
                    return nullSafeHashCode((char[])((char[])obj));
                }

                if(obj instanceof double[]) {
                    return nullSafeHashCode((double[])((double[])obj));
                }

                if(obj instanceof float[]) {
                    return nullSafeHashCode((float[])((float[])obj));
                }

                if(obj instanceof int[]) {
                    return nullSafeHashCode((int[])((int[])obj));
                }

                if(obj instanceof long[]) {
                    return nullSafeHashCode((long[])((long[])obj));
                }

                if(obj instanceof short[]) {
                    return nullSafeHashCode((short[])((short[])obj));
                }
            }

            return obj.hashCode();
        }
    }
