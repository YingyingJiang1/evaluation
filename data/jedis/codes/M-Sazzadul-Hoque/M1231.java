        static Resource from(URL url) {

            Objects.requireNonNull(url, "URL must not be null");

            return () -> url.openConnection().getInputStream();
        }
