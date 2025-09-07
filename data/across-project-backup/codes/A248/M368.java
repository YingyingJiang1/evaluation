    private static String decodeQuotedPrintable(String encodedText, String charset) {
        StringBuilder result = new StringBuilder();
        for (int i = 0; i < encodedText.length(); i++) {
            char c = encodedText.charAt(i);
            switch (c) {
                case '=' -> {
                    if (i + 2 < encodedText.length()) {
                        String hex = encodedText.substring(i + 1, i + 3);
                        try {
                            int value = Integer.parseInt(hex, 16);
                            result.append((char) value);
                            i += 2; // Skip the hex digits
                        } catch (NumberFormatException e) {
                            // If hex parsing fails, keep the original character
                            result.append(c);
                        }
                    } else {
                        result.append(c);
                    }
                }
                case '_' -> // In RFC 2047, underscore represents space
                        result.append(' ');
                default -> result.append(c);
            }
        }

        // Convert bytes to proper charset
        byte[] bytes = result.toString().getBytes(StandardCharsets.ISO_8859_1);
        return new String(bytes, Charset.forName(charset));
    }
