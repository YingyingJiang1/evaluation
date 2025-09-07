  private static String validateLibNameSuffix(String suffix) {
    if (suffix == null || suffix.trim().isEmpty()) {
      return null;
    }

    for (int i = 0; i < suffix.length(); i++) {
      char c = suffix.charAt(i);
      if (c < ' ' || c > '~' || BRACES.contains(c)) {
        throw new JedisValidationException("lib-name suffix cannot contain braces, newlines or "
            + "special characters.");
      }
    }

    return suffix.replaceAll("\\s", "-");
  }
