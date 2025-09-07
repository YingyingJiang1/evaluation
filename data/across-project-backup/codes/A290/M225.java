        public static String format(Number num) {
            String result = df.format(num);
            StringBuilder sb = new StringBuilder();
            String seg1, seg2, seg3;
            int len = result.length();
            if (len <= 4) {
                return result;
            }

            seg3 = result.substring(len - 4, len);
            seg2 = result.substring(Math.max(0, len - 7), len - 4);
            seg1 = result.substring(Math.max(0, len - 10), Math.max(0, len - 7));

            if (seg1.trim().length() > 0) {
                sb.append('(').append(seg1).append(") ");
            }
            if (seg2.trim().length() > 0) {
                sb.append(seg2).append('-');
            }
            sb.append(seg3);
            return sb.toString();
        }
