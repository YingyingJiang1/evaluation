  public static Schema from(Field... fields) {
    Schema schema = new Schema();
    for (Field field : fields) {
      schema.addField(field);
    }
    return schema;
  }
