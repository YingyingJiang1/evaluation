    @Override
    public void complete(Completion completion) {
        if (!CompletionUtils.completeClassName(completion)) {
            super.complete(completion);
        }
    }
