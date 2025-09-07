    private String translate(String key) {
        if (key.length() == 6 && key.startsWith("\"\\C-") && key.endsWith("\"")) {
            char ch = key.charAt(4);
            if ((ch >= 'a' && ch <= 'z') || ch == '?') {
                return "Ctrl + " + ch;
            }
        }

        if (key.equals("\"\\e[D\"")) {
            return "Left arrow";
        } else if (key.equals("\"\\e[C\"")) {
            return "Right arrow";
        } else if (key.equals("\"\\e[B\"")) {
            return "Down arrow";
        } else if (key.equals("\"\\e[A\"")) {
            return "Up arrow";
        }

        return key;
    }
