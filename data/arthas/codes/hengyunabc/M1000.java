    @Override
    public String result() {
        if (result != null) {
            return result;
        }

        return total.get() + "\n";
    }
