  private static void appendNum(StringBuilder sb, double n, boolean inclusive) {
    if (!inclusive) {
      sb.append("(");
    }
    if (n == Double.NEGATIVE_INFINITY) {
      sb.append("-inf");
    } else if (n == Double.POSITIVE_INFINITY) {
      sb.append("inf");
    } else {
      sb.append(n);
    }
  }
