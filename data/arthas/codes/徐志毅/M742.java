    @Override
    public void complete(Completion completion) {
        int argumentIndex = CompletionUtils.detectArgumentIndex(completion);
        if (argumentIndex == 1) {
            if (!completeBeanName(completion)) {
                super.complete(completion);
            }
            return;
        } else if (argumentIndex == 2) {
            if (!completeAttributeName(completion)) {
                super.complete(completion);
            }
            return;
        }
        super.complete(completion);
    }
