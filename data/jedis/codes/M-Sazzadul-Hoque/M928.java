  @Override
  public String getPassword() {
    char[] password = credentialsProvider.get().getPassword();
    return password == null ? null : new String(password);
  }
