    private static String constructMessageKeyUrl(Class<?> clss, String messageKey) {
        return "https://github.com/search?q="
                + "path%3Asrc%2Fmain%2Fresources%2F"
                + clss.getPackage().getName().replace(".", "%2F")
                + "%20path%3A**%2Fmessages*.properties+repo%3Acheckstyle%2F"
                + "checkstyle+%22" + messageKey + "%22";
    }
