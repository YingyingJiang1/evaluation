    public static String randomString(int length) {
        StringBuilder sb = new StringBuilder(length);
        for (int i = 0; i < length; i++)
            sb.append(AB.charAt(ThreadLocalRandom.current().nextInt(AB.length())));
        return sb.toString();
    }
