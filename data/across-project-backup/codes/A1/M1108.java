    @Override
    public Object build(Object data) {
      if (data == null) {
        return null;
      }

      if (!(data instanceof byte[])) {
        return data;
      }
      String str = STRING.build(data);
      if (str.charAt(0) == '{') {
        try {
          return new JSONObject(str);
        } catch (Exception ex) {
        }
      } else if (str.charAt(0) == '[') {
        try {
          return new JSONArray(str);
        } catch (Exception ex) {
        }
      }
      return str;
    }
