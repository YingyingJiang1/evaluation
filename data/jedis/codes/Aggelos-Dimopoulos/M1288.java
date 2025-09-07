  private static void appendNum(StringBuilder sb, long n, boolean inclusive) {
    if (!inclusive) {
      sb.append("(");
    }
    if (n == Long.MIN_VALUE) {
      sb.append("-inf");
    } else if (n == Long.MAX_VALUE) {
      sb.append("inf");
    } else {
      sb.append(Long.toString(n));
    }
  }
