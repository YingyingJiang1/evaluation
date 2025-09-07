    private static String getRepoUrl(String repoUrl, boolean http) {
        if (repoUrl.endsWith("/")) {
            repoUrl = repoUrl.substring(0, repoUrl.length() - 1);
        }

        if (http && repoUrl.startsWith("https")) {
            repoUrl = "http" + repoUrl.substring("https".length());
        }
        return repoUrl;
    }
