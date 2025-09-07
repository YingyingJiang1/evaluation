  @Override
  public void addParams(CommandArguments args) {
    if (latest) {
      args.add(LATEST);
    }

    if (withLabels) {
      args.add(WITHLABELS);
    } else if (selectedLabels != null) {
      args.add(SELECTED_LABELS);
      for (String label : selectedLabels) {
        args.add(label);
      }
    }
  }
