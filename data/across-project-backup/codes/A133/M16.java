    private static File findJava(String javaHome) {
        String[] paths = { "bin/java", "bin/java.exe", "../bin/java", "../bin/java.exe" };

        List<File> javaList = new ArrayList<File>();
        for (String path : paths) {
            File javaFile = new File(javaHome, path);
            if (javaFile.exists()) {
                AnsiLog.debug("Found java: " + javaFile.getAbsolutePath());
                javaList.add(javaFile);
            }
        }

        if (javaList.isEmpty()) {
            AnsiLog.debug("Can not find java/java.exe under current java home: " + javaHome);
            return null;
        }

        // find the shortest path, jre path longer than jdk path
        if (javaList.size() > 1) {
            Collections.sort(javaList, new Comparator<File>() {
                @Override
                public int compare(File file1, File file2) {
                    try {
                        return file1.getCanonicalPath().length() - file2.getCanonicalPath().length();
                    } catch (IOException e) {
                        // ignore
                    }
                    return -1;
                }
            });
        }
        return javaList.get(0);
    }
