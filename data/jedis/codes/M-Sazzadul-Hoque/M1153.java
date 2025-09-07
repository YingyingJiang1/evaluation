  public static int compare(Tuple t1, Tuple t2) {
    int compScore = Double.compare(t1.score, t2.score);
    if (compScore != 0) return compScore;

    return ByteArrayComparator.compare(t1.element, t2.element);
  }
