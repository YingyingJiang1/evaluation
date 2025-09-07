    @Override
    public List<TSElement> build(Object data) {
      return ((List<Object>) data).stream().map((pairObject) -> (List<Object>) pairObject)
          .map((pairList) -> new TSElement(BuilderFactory.LONG.build(pairList.get(0)),
              BuilderFactory.DOUBLE.build(pairList.get(1))))
          .collect(Collectors.toList());
    }
