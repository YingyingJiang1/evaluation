    public static List<String> runNative(String[] cmdToRunWithArgs) {
        Process p = null;
        try {
            p = Runtime.getRuntime().exec(cmdToRunWithArgs);
        } catch (SecurityException e) {
            AnsiLog.trace("Couldn't run command {}:", Arrays.toString(cmdToRunWithArgs));
            AnsiLog.trace(e);
            return new ArrayList<String>(0);
        } catch (IOException e) {
            AnsiLog.trace("Couldn't run command {}:", Arrays.toString(cmdToRunWithArgs));
            AnsiLog.trace(e);
            return new ArrayList<String>(0);
        }

        ArrayList<String> sa = new ArrayList<String>();
        BufferedReader reader = new BufferedReader(new InputStreamReader(p.getInputStream()));
        try {
            String line;
            while ((line = reader.readLine()) != null) {
                sa.add(line);
            }
            p.waitFor();
        } catch (IOException e) {
            AnsiLog.trace("Problem reading output from {}:", Arrays.toString(cmdToRunWithArgs));
            AnsiLog.trace(e);
            return new ArrayList<String>(0);
        } catch (InterruptedException ie) {
            AnsiLog.trace("Problem reading output from {}:", Arrays.toString(cmdToRunWithArgs));
            AnsiLog.trace(ie);
            Thread.currentThread().interrupt();
        } finally {
            IOUtils.close(reader);
        }
        return sa;
    }
