    @Override
    public void complete(final Completion completion) {
        final AnnotatedCommand instance;
        try {
            instance = clazz.newInstance();
        } catch (Exception e) {
            super.complete(completion);
            return;
        }

        try {
            instance.complete(completion);
        } catch (Throwable t) {
            completion.complete(Collections.<String>emptyList());
        }
    }
