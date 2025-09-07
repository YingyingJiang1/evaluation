    @Override
    public void complete(Completion completion) {
        HotSpotDiagnosticMXBean hotSpotDiagnosticMXBean = ManagementFactory
                        .getPlatformMXBean(HotSpotDiagnosticMXBean.class);
        List<VMOption> diagnosticOptions = hotSpotDiagnosticMXBean.getDiagnosticOptions();
        List<String> names = new ArrayList<String>(diagnosticOptions.size());
        for (VMOption option : diagnosticOptions) {
            names.add(option.getName());
        }
        CompletionUtils.complete(completion, names);
    }
