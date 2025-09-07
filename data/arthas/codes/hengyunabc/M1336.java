    public static boolean isCompatibleWithThrowsClause(Throwable ex, Class... declaredExceptions) {
        if(!isCheckedException(ex)) {
            return true;
        } else {
            if(declaredExceptions != null) {
                Class[] var2 = declaredExceptions;
                int var3 = declaredExceptions.length;

                for(int var4 = 0; var4 < var3; ++var4) {
                    Class declaredException = var2[var4];
                    if(declaredException.isInstance(ex)) {
                        return true;
                    }
                }
            }

            return false;
        }
    }
