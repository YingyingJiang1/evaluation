    @Override
    public JSONArray build(Object data) {
      if (data == null) {
        return null;
      }
      String str = STRING.build(data);
      try {
        return new JSONArray(str);
      } catch (JSONException ex) { // This is not necessary but we are doing this
        // just to make it safe for com.vaadin.external.google:android-json library
        throw new JedisException(ex);
      }
    }
