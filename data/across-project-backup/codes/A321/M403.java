    private static Platform determinePlatformReflectively() {
        if (System.getProperty("com.google.appengine.runtime.environment") == null) {
            return Platform.STANDARD;
        }
        // GAE_LONG_APP_ID is only set in the GAE Flexible Environment, where we want standard threading
        if (System.getenv("GAE_LONG_APP_ID") != null) {
            return Platform.APPENGINE_FLEXIBLE;
        }
        try {
            // If the current environment is null, we're not inside AppEngine.
            boolean isInsideAppengine = Class.forName("com.google.apphosting.api.ApiProxy")
                    .getMethod("getCurrentEnvironment")
                    .invoke(null) != null;
            return isInsideAppengine ? Platform.APPENGINE_STANDARD : Platform.STANDARD;
        } catch (ClassNotFoundException e) {
            // If ApiProxy doesn't exist, we're not on AppEngine at all.
            return Platform.STANDARD;
        } catch (InvocationTargetException e) {
            // If ApiProxy throws an exception, we're not in a proper AppEngine environment.
            return Platform.STANDARD;
        } catch (IllegalAccessException e) {
            // If the method isn't accessible, we're not on a supported version of AppEngine;
            return Platform.STANDARD;
        } catch (NoSuchMethodException e) {
            // If the method doesn't exist, we're not on a supported version of AppEngine;
            return Platform.STANDARD;
        }
    }
