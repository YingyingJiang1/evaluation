    private static void convertExecutionToJson(JsonGenerator json, HystrixRequestEvents.ExecutionSignature executionSignature, List<Integer> latencies) throws IOException {
        json.writeStartObject();
        json.writeStringField("name", executionSignature.getCommandName());
        json.writeArrayFieldStart("events");
        ExecutionResult.EventCounts eventCounts = executionSignature.getEventCounts();
        for (HystrixEventType eventType: HystrixEventType.values()) {
            if (eventType != HystrixEventType.COLLAPSED) {
                if (eventCounts.contains(eventType)) {
                    int eventCount = eventCounts.getCount(eventType);
                    if (eventCount > 1) {
                        json.writeStartObject();
                        json.writeStringField("name", eventType.name());
                        json.writeNumberField("count", eventCount);
                        json.writeEndObject();
                    } else {
                        json.writeString(eventType.name());
                    }
                }
            }
        }
        json.writeEndArray();
        json.writeArrayFieldStart("latencies");
        for (int latency: latencies) {
            json.writeNumber(latency);
        }
        json.writeEndArray();
        if (executionSignature.getCachedCount() > 0) {
            json.writeNumberField("cached", executionSignature.getCachedCount());
        }
        if (executionSignature.getEventCounts().contains(HystrixEventType.COLLAPSED)) {
            json.writeObjectFieldStart("collapsed");
            json.writeStringField("name", executionSignature.getCollapserKey().name());
            json.writeNumberField("count", executionSignature.getCollapserBatchSize());
            json.writeEndObject();
        }
        json.writeEndObject();
    }
