    @Override
    public void complete(Completion completion) {
        List<OptionCompleteHandler> handlers = new ArrayList<OptionCompleteHandler>();

        handlers.add(new OptionCompleteHandler() {

            @Override
            public boolean matchName(String token) {
                return "-a".equals(token) || "--action".equals(token);
            }

            @Override
            public boolean complete(Completion completion) {
                return CompletionUtils.complete(completion, actions());
            }

        });

        handlers.add(new OptionCompleteHandler() {
            @Override
            public boolean matchName(String token) {
                return "--className".equals(token);
            }

            @Override
            public boolean complete(Completion completion) {
                return CompletionUtils.completeClassName(completion);
            }
        });

        if (CompletionUtils.completeOptions(completion, handlers)) {
            return;
        }

        super.complete(completion);
    }
