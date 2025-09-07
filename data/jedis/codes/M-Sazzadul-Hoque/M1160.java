    @Override
    public List<LibraryInfo> build(Object data) {
      List<Object> list = (List<Object>) data;
      return list.stream().map(o -> LibraryInfo.LIBRARY_INFO.build(o)).collect(Collectors.toList());
    }
