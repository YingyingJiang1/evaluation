    private static String getPlatformTag() {
        String os = System.getProperty("os.name").toLowerCase();
        String arch = System.getProperty("os.arch").toLowerCase();
        if (os.contains("linux")) {
            if (arch.equals("amd64") || arch.equals("x86_64") || arch.contains("x64")) {
                return "linux-x64";
            } else if (arch.equals("aarch64") || arch.contains("arm64")) {
                return "linux-arm64";
            } else if (arch.equals("aarch32") || arch.contains("arm")) {
                return "linux-arm32";
            } else if (arch.contains("86")) {
                return "linux-x86";
            } else if (arch.contains("ppc64")) {
                return "linux-ppc64le";
            }
        } else if (os.contains("mac")) {
            return "macos";
        }
        throw new UnsupportedOperationException("Unsupported platform: " + os + "-" + arch);
    }
