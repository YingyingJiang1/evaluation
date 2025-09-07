    @Override
    public boolean equals(Object o) {
        if (o == this)
            return true;
        if (!(o instanceof Pair))
            return false;

        Pair other = (Pair) o;

        if (x == null) {
            if (other.x != null)
                return false;
        } else {
            if (!x.equals(other.x))
                return false;
        }
        if (y == null) {
            if (other.y != null)
                return false;
        } else {
            if (!y.equals(other.y))
                return false;
        }
        return true;
    }
