    @Override
    public void initialize() {
        // allow monitor to know exactly at what point in time these stats are for so they can be plotted accurately
        metricsRegistry.newGauge(createMetricName("currentTime"), new Gauge<Long>() {
            @Override
            public Long value() {
                return System.currentTimeMillis();
            }
        });

        // cumulative counts
        safelyCreateCumulativeCountForEvent("countRequestsBatched", new Func0<HystrixRollingNumberEvent>() {
            @Override
            public HystrixRollingNumberEvent call() {
                return HystrixRollingNumberEvent.COLLAPSER_REQUEST_BATCHED;
            }
        });
        safelyCreateCumulativeCountForEvent("countBatches", new Func0<HystrixRollingNumberEvent>() {
            @Override
            public HystrixRollingNumberEvent call() {
                return HystrixRollingNumberEvent.COLLAPSER_BATCH;
            }
        });
        safelyCreateCumulativeCountForEvent("countResponsesFromCache", new Func0<HystrixRollingNumberEvent>() {
            @Override
            public HystrixRollingNumberEvent call() {
                return HystrixRollingNumberEvent.RESPONSE_FROM_CACHE;
            }
        });

        // rolling counts
        safelyCreateRollingCountForEvent("rollingRequestsBatched", new Func0<HystrixRollingNumberEvent>() {
            @Override
            public HystrixRollingNumberEvent call() {
                return HystrixRollingNumberEvent.COLLAPSER_REQUEST_BATCHED;
            }
        });
        safelyCreateRollingCountForEvent("rollingBatches", new Func0<HystrixRollingNumberEvent>() {
            @Override
            public HystrixRollingNumberEvent call() {
                return HystrixRollingNumberEvent.COLLAPSER_BATCH;
            }
        });
        safelyCreateRollingCountForEvent("rollingCountResponsesFromCache", new Func0<HystrixRollingNumberEvent>() {
            @Override
            public HystrixRollingNumberEvent call() {
                return HystrixRollingNumberEvent.RESPONSE_FROM_CACHE;
            }
        });

        // batch size metrics
        metricsRegistry.newGauge(createMetricName("batchSize_mean"), new Gauge<Integer>() {
            @Override
            public Integer value() {
                return metrics.getBatchSizeMean();
            }
        });
        metricsRegistry.newGauge(createMetricName("batchSize_percentile_25"), new Gauge<Integer>() {
            @Override
            public Integer value() {
                return metrics.getBatchSizePercentile(25);
            }
        });
        metricsRegistry.newGauge(createMetricName("batchSize_percentile_50"), new Gauge<Integer>() {
            @Override
            public Integer value() {
                return metrics.getBatchSizePercentile(50);
            }
        });
        metricsRegistry.newGauge(createMetricName("batchSize_percentile_75"), new Gauge<Integer>() {
            @Override
            public Integer value() {
                return metrics.getBatchSizePercentile(75);
            }
        });
        metricsRegistry.newGauge(createMetricName("batchSize_percentile_90"), new Gauge<Integer>() {
            @Override
            public Integer value() {
                return metrics.getBatchSizePercentile(90);
            }
        });
        metricsRegistry.newGauge(createMetricName("batchSize_percentile_99"), new Gauge<Integer>() {
            @Override
            public Integer value() {
                return metrics.getBatchSizePercentile(99);
            }
        });
        metricsRegistry.newGauge(createMetricName("batchSize_percentile_995"), new Gauge<Integer>() {
            @Override
            public Integer value() {
                return metrics.getBatchSizePercentile(99.5);
            }
        });

        // shard size metrics
        metricsRegistry.newGauge(createMetricName("shardSize_mean"), new Gauge<Integer>() {
            @Override
            public Integer value() {
                return metrics.getShardSizeMean();
            }
        });
        metricsRegistry.newGauge(createMetricName("shardSize_percentile_25"), new Gauge<Integer>() {
            @Override
            public Integer value() {
                return metrics.getShardSizePercentile(25);
            }
        });
        metricsRegistry.newGauge(createMetricName("shardSize_percentile_50"), new Gauge<Integer>() {
            @Override
            public Integer value() {
                return metrics.getShardSizePercentile(50);
            }
        });
        metricsRegistry.newGauge(createMetricName("shardSize_percentile_75"), new Gauge<Integer>() {
            @Override
            public Integer value() {
                return metrics.getShardSizePercentile(75);
            }
        });
        metricsRegistry.newGauge(createMetricName("shardSize_percentile_90"), new Gauge<Integer>() {
            @Override
            public Integer value() {
                return metrics.getShardSizePercentile(90);
            }
        });
        metricsRegistry.newGauge(createMetricName("shardSize_percentile_99"), new Gauge<Integer>() {
            @Override
            public Integer value() {
                return metrics.getShardSizePercentile(99);
            }
        });
        metricsRegistry.newGauge(createMetricName("shardSize_percentile_995"), new Gauge<Integer>() {
            @Override
            public Integer value() {
                return metrics.getShardSizePercentile(99.5);
            }
        });

        // properties (so the values can be inspected and monitored)
        metricsRegistry.newGauge(createMetricName("propertyValue_rollingStatisticalWindowInMilliseconds"), new Gauge<Number>() {
            @Override
            public Number value() {
                return properties.metricsRollingStatisticalWindowInMilliseconds().get();
            }
        });

        metricsRegistry.newGauge(createMetricName("propertyValue_requestCacheEnabled"), new Gauge<Boolean>() {
            @Override
            public Boolean value() {
                return properties.requestCacheEnabled().get();
            }
        });

        metricsRegistry.newGauge(createMetricName("propertyValue_maxRequestsInBatch"), new Gauge<Number>() {
            @Override
            public Number value() {
                return properties.maxRequestsInBatch().get();
            }
        });

        metricsRegistry.newGauge(createMetricName("propertyValue_timerDelayInMilliseconds"), new Gauge<Number>() {
            @Override
            public Number value() {
                return properties.timerDelayInMilliseconds().get();
            }
        });
    }
