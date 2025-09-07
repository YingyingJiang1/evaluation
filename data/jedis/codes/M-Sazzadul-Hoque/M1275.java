    @Override
    public Document build(Object data) {
      List<KeyValue> list = (List<KeyValue>) data;
      String id = null;
      Double score = null;
      Map<String, Object> fields = null;
      for (KeyValue kv : list) {
        String key = BuilderFactory.STRING.build(kv.getKey());
        switch (key) {
          case ID_STR:
            id = BuilderFactory.STRING.build(kv.getValue());
            break;
          case SCORE_STR:
            score = BuilderFactory.DOUBLE.build(kv.getValue());
            break;
          case FIELDS_STR:
            fields = makeFieldsMap(isFieldDecode, kv.getValue());
            break;
        }
      }
      return new Document(id, score, fields);
    }
