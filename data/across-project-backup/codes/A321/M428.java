        private int computePercentile(double percent) {
            // Some just-in-case edge cases
            if (length <= 0) {
                return 0;
            } else if (percent <= 0.0) {
                return data[0];
            } else if (percent >= 100.0) {
                return data[length - 1];
            }

            // ranking (http://en.wikipedia.org/wiki/Percentile#Alternative_methods)
            double rank = (percent / 100.0) * length;

            // linear interpolation between closest ranks
            int iLow = (int) Math.floor(rank);
            int iHigh = (int) Math.ceil(rank);
            assert 0 <= iLow && iLow <= rank && rank <= iHigh && iHigh <= length;
            assert (iHigh - iLow) <= 1;
            if (iHigh >= length) {
                // Another edge case
                return data[length - 1];
            } else if (iLow == iHigh) {
                return data[iLow];
            } else {
                // Interpolate between the two bounding values
                return (int) (data[iLow] + (rank - iLow) * (data[iHigh] - data[iLow]));
            }
        }
