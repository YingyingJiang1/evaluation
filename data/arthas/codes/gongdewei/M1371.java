    public static String getCookieValue(Set<Cookie> cookies, String cookieName) {
        for (Cookie cookie : cookies) {
            if(cookie.name().equals(cookieName)){
                return cookie.value();
            }
        }
        return null;
    }
