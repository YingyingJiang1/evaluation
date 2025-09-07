    private long parseSessionTimeout(String timeout) {
        if (timeout == null || timeout.isEmpty()) {
            return 30 * 60 * 1000; // Default: 30 minutes
        }

        try {
            String value = timeout.replaceAll("[^\\d.]", "");
            String unit = timeout.replaceAll("[\\d.]", "");

            double numericValue = Double.parseDouble(value);

            return switch (unit.toLowerCase()) {
                case "s" -> (long) (numericValue * 1000);
                case "m" -> (long) (numericValue * 60 * 1000);
                case "h" -> (long) (numericValue * 60 * 60 * 1000);
                case "d" -> (long) (numericValue * 24 * 60 * 60 * 1000);
                default -> (long) (numericValue * 60 * 1000); // Default to minutes
            };
        } catch (Exception e) {
            log.warn("Could not parse session timeout '{}', using default", timeout);
            return 30 * 60 * 1000; // Default: 30 minutes
        }
    }
