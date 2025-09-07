    private AccessResult localCheckAccess(String inPkg, String inFileName, String forImport) {
        AccessResult localCheckAccessResult = AccessResult.UNKNOWN;
        for (AbstractImportRule importRule : rules) {
            // Check if an import rule is only meant to be applied locally.
            if (!importRule.isLocalOnly() || matchesExactly(inPkg, inFileName)) {
                final AccessResult result = importRule.verifyImport(forImport);
                if (result != AccessResult.UNKNOWN) {
                    localCheckAccessResult = result;
                    break;
                }
            }
        }
        return localCheckAccessResult;
    }
