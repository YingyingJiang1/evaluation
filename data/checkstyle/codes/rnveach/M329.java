    public AccessResult checkAccess(String inPkg, String inFileName, String forImport) {
        final AccessResult result;
        final AccessResult returnValue = localCheckAccess(inPkg, inFileName, forImport);
        if (returnValue != AccessResult.UNKNOWN) {
            result = returnValue;
        }
        else if (parent == null) {
            if (strategyOnMismatch == MismatchStrategy.ALLOWED) {
                result = AccessResult.ALLOWED;
            }
            else {
                result = AccessResult.DISALLOWED;
            }
        }
        else {
            if (strategyOnMismatch == MismatchStrategy.ALLOWED) {
                result = AccessResult.ALLOWED;
            }
            else if (strategyOnMismatch == MismatchStrategy.DISALLOWED) {
                result = AccessResult.DISALLOWED;
            }
            else {
                result = parent.checkAccess(inPkg, inFileName, forImport);
            }
        }
        return result;
    }
