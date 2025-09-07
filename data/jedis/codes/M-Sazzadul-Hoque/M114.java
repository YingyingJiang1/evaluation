    protected static <E> SetFromList<E> of(List<E> list) {
      if (list == null) {
        return null;
      }
      return new SetFromList<>(list);
    }
