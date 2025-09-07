    @Override
    public boolean equals(Object obj) {
        if (obj == this) {
            return true;
        } else if (obj instanceof CliTokenImpl) {
            CliTokenImpl that = (CliTokenImpl) obj;
            return text == that.text && value.equals(that.value);
        }
        return false;
    }
