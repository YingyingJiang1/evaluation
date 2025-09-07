    @Override
    public LibraryInfo build(Object data) {
      if (data == null) return null;
      List list = (List) data;
      if (list.isEmpty()) return null;

      if (list.get(0) instanceof KeyValue) {
        String libname = null, enginename = null, librarycode = null;
        List<Map<String, Object>> functions = null;
        for (KeyValue kv : (List<KeyValue>) list) {
          switch (BuilderFactory.STRING.build(kv.getKey())) {
            case "library_name":
              libname = BuilderFactory.STRING.build(kv.getValue());
              break;
            case "engine":
              enginename = BuilderFactory.STRING.build(kv.getValue());
              break;
            case "functions":
              functions = ((List<Object>) kv.getValue()).stream().map(o -> ENCODED_OBJECT_MAP.build(o)).collect(Collectors.toList());
              break;
            case "library_code":
              librarycode = BuilderFactory.STRING.build(kv.getValue());
              break;
          }
        }
        return new LibraryInfo(libname, enginename, functions, librarycode);
      }

      String libname = STRING.build(list.get(1));
      String engine = STRING.build(list.get(3));
      List<Object> rawFunctions = (List<Object>) list.get(5);
      List<Map<String, Object>> functions = rawFunctions.stream().map(o -> ENCODED_OBJECT_MAP.build(o)).collect(Collectors.toList());
      if (list.size() <= 6) {
        return new LibraryInfo(libname, engine, functions);
      }
      String code = STRING.build(list.get(7));
      return new LibraryInfo(libname, engine, functions, code);
    }
