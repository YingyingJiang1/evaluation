    @Override
    public void initialize() {
        // allow monitor to know exactly at what point in time these stats are for so they can be plotted accurately
        metricRegistry.register(createMetricName("currentTime"), new Gauge<Long>() {
            @Override
            public Long getValue() {
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
        metricRegistry.register(createMetricName("batchSize_mean"), new Gauge<Integer>() {
            @Override
            public Integer getValue() {
                return metrics.getBatchSizeMean();
            }
        });
        metricRegistry.register(createMetricName("batchSize_percentile_25"), new Gauge<Integer>() {
            @Override
            public Integer getValue() {
                return metrics.getBatchSizePercentile(25);
            }
        });
        metricRegistry.register(createMetricName("batchSize_percentile_50"), new Gauge<Integer>() {
            @Override
            public Integer getValue() {
                return metrics.getBatchSizePercentile(50);
            }
        });
        metricRegistry.register(createMetricName("batchSize_percentile_75"), new Gauge<Integer>() {
            @Override
            public Integer getValue() {
                return metrics.getBatchSizePercentile(75);
            }
        });
        metricRegistry.register(createMetricName("batchSize_percentile_90"), new Gauge<Integer>() {
            @Override
            public Integer getValue() {
                return metrics.getBatchSizePercentile(90);
            }
        });
        metricRegistry.register(createMetricName("batchSize_percentile_99"), new Gauge<Integer>() {
            @Override
            public Integer getValue() {
                return metrics.getBatchSizePercentile(99);
            }
        });
        metricRegistry.register(createMetricName("batchSize_percentile_995"), new Gauge<Integer>() {
            @Override
            public Integer getValue() {
                return metrics.getBatchSizePercentile(99.5);
            }
        });

        // shard size metrics
        metricRegistry.register(createMetricName("shardSize_mean"), new Gauge<Integer>() {
            @Override
            public Integer getValue() {
                return metrics.getShardSizeMean();
            }
        });
        metricRegistry.register(createMetricName("shardSize_percentile_25"), new Gauge<Integer>() {
            @Override
            public Integer getValue() {
                return metrics.getShardSizePercentile(25);
            }
        });
        metricRegistry.register(createMetricName("shardSize_percentile_50"), new Gauge<Integer>() {
            @Override
            public Integer getValue() {
                return metrics.getShardSizePercentile(50);
            }
        });
        metricRegistry.register(createMetricName("shardSize_percentile_75"), new Gauge<Integer>() {
            @Override
            public Integer getValue() {
                return metrics.getShardSizePercentile(75);
            }
        });
        metricRegistry.register(createMetricName("shardSize_percentile_90"), new Gauge<Integer>() {
            @Override
            public Integer getValue() {
                return metrics.getShardSizePercentile(90);
            }
        });
        metricRegistry.register(createMetricName("shardSize_percentile_99"), new Gauge<Integer>() {
            @Override
            public Integer getValue() {
                return metrics.getShardSizePercentile(99);
            }
        });
        metricRegistry.register(createMetricName("shardSize_percentile_995"), new Gauge<Integer>() {
            @Override
            public Integer getValue() {
                return metrics.getShardSizePercentile(99.5);
            }
        });

        // properties (so the values can be inspected and monitored)
        metricRegistry.register(createMetricName("propertyValue_rollingStatisticalWindowInMilliseconds"), new Gauge<Number>() {
            @Override
            public Number getValue() {
                return properties.metricsRollingStatisticalWindowInMilliseconds().get();
            }
        });

        metricRegistry.register(createMetricName("propertyValue_requestCacheEnabled"), new Gauge<Boolean>() {
            @Override
            public Boolean getValue() {
                return properties.requestCacheEnabled().get();
            }
        });

        metricRegistry.register(createMetricName("propertyValue_maxRequestsInBatch"), new Gauge<Number>() {
            @Override
            public Number getValue() {
                return properties.maxRequestsInBatch().get();
            }
        });

        metricRegistry.register(createMetricName("propertyValue_timerDelayInMilliseconds"), new Gauge<Number>() {
            @Override
            public Number getValue() {
                return properties.timerDelayInMilliseconds().get();
            }
        });
    }
