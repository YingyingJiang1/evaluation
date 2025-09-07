    public static List<int[]> loadCommandHistory(File file) {
        BufferedReader br = null;
        List<int[]> history = new ArrayList<>();
        try {
            br = new BufferedReader(new InputStreamReader(new FileInputStream(file)));
            String line;
            while ((line = br.readLine()) != null) {
                history.add(Helper.toCodePoints(line));
            }
        } catch (IOException e) {
            // ignore
        } finally {
            try {
                if (br != null) {
                    br.close();
                }
            } catch (IOException ioe) {
                // ignore
            }
        }
        return history;
    }
