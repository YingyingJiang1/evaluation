        public void addTableRow(TableElement table, Style.Composite style) {
            double usage = used / (double) (max == -1 || max == Long.MIN_VALUE ? total : max) * 100;
            if (Double.isNaN(usage) || Double.isInfinite(usage)) {
                usage = 0;
            }
            table.add(new RowElement().style(style).add(name, format(used), format(total), format(max),
                    String.format("%.2f%%", usage)));
        }
