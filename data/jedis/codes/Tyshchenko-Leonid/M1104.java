    @Override
    public boolean equals(Object o) {
      if (this == o) return true;
      if (o == null || getClass() != o.getClass()) return false;
      return Arrays.equals(raw, ((Raw) o).raw);
    }
