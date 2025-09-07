    private void leaveCompilationUnit() {
        anonInnerClassHolders.forEach(holder -> {
            iterateOverBlockContainingLocalAnonInnerClass(holder, new ArrayDeque<>());
        });
    }
