        public void addTableRow(TableElement table) {
            double usage = used / (double) (max == -1 || max == Long.MIN_VALUE ? total : max) * 100;
            if (Double.isNaN(usage) || Double.isInfinite(usage)) {
                usage = 0;
            }
            table.row(name, format(used), format(total), format(max), String.format("%.2f%%", usage));
        }
