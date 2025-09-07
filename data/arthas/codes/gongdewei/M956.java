    public void end() {
        endTimestamp = System.nanoTime();

        long cost = getCost();
        if (cost < minCost) {
            minCost = cost;
        }
        if (cost > maxCost) {
            maxCost = cost;
        }
        times++;
        totalCost += cost;
    }
