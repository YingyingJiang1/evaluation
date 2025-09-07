        public int getWidth() {

            if (!isAutoResize) {
                return width;
            }

            int maxWidth = 0;
            for (String data : dataList) {
                final Scanner scanner = new Scanner(new StringReader(data));
                try {
                    while (scanner.hasNext()) {
                        maxWidth = max(StringUtils.length(scanner.nextLine()), maxWidth);
                    }
                } finally {
                    scanner.close();
                }
            }

            return maxWidth;
        }
