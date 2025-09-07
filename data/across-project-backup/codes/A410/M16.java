    private void handleCookiesFromUrl(@Nullable final String url) {
        if (MainActivity.DEBUG) {
            Log.d(TAG, "handleCookiesFromUrl: url=" + (url == null ? "null" : url));
        }

        if (url == null) {
            return;
        }

        final String cookies = CookieManager.getInstance().getCookie(url);
        handleCookies(cookies);

        // sometimes cookies are inside the url
        final int abuseStart = url.indexOf("google_abuse=");
        if (abuseStart != -1) {
            final int abuseEnd = url.indexOf("+path");

            try {
                handleCookies(Utils.decodeUrlUtf8(url.substring(abuseStart + 13, abuseEnd)));
            } catch (final StringIndexOutOfBoundsException e) {
                if (MainActivity.DEBUG) {
                    Log.e(TAG, "handleCookiesFromUrl: invalid google abuse starting at "
                            + abuseStart + " and ending at " + abuseEnd + " for url " + url, e);
                }
            }
        }
    }
