    public double rate() {
        long c = count.get();
        int countLength = 0;
        long sum = 0;
        if (c > values.length()) {
            countLength = values.length();
        } else {
            countLength = (int) c;
        }

        for (int i = 0; i < countLength; ++i) {
            sum += values.get(i);
        }

        return sum / (double) countLength;
    }
