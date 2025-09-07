    public static void saveCommandHistory(List<int[]> history, File file) {
        try (OutputStream out = new BufferedOutputStream(openOutputStream(file, false))) {
            for (int[] command : history) {
                String commandStr = Helper.fromCodePoints(command);
                if (isAuthCommand(commandStr)) {
                    command = AUTH_CODEPOINTS;
                }

                for (int i : command) {
                    out.write(i);
                }
                out.write('\n');
            }
        } catch (IOException e) {
            // ignore
        }
        // ignore
    }
