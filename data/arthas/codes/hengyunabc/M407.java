        public String toString() {
            StringBuilder sb = new StringBuilder();
            if (times <= 1) {
                sb.append("[").append(getCostInMillis(getCost())).append(TIME_UNIT).append("] ");
            } else {
                sb.append("[min=").append(getCostInMillis(minCost)).append(TIME_UNIT).append(",max=")
                        .append(getCostInMillis(maxCost)).append(TIME_UNIT).append(",total=")
                        .append(getCostInMillis(totalCost)).append(TIME_UNIT).append(",count=")
                        .append(times).append("] ");
            }
            return sb.toString();
        }
