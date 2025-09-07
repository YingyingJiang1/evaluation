  private static char[] toCharArray(CharSequence seq) {
    final int len = seq.length();
    char[] arr = new char[len];
    for (int i = 0; i < len; i++) {
      arr[i] = seq.charAt(i);
    }
    return arr;
  }
