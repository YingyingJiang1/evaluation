    @Override
    public int hashCode() {
        int hashCode = 1;
        if (x != null)
            hashCode = x.hashCode();
        if (y != null)
            hashCode = (hashCode * 31) + y.hashCode();
        return hashCode;
    }
