    @Override
    public String draw() {
        StringBuilder buf = new StringBuilder();
        try {
            if (GlobalOptions.isUsingJson) {
                return JSON.toJSONString(object, JSONWriter.Feature.IgnoreErrorGetter);
            }
            renderObject(object, 0, deep, buf);
            return buf.toString();
        } catch (ObjectTooLargeException e) {
            buf.append(" Object size exceeds size limit: ")
                    .append(maxObjectLength)
                    .append(", try to specify -M size_limit in your command, check the help command for more.");
            return buf.toString();
        } catch (Throwable t) {
            logger.error("ObjectView draw error, object class: {}", object.getClass(), t);
            return "ERROR DATA!!! object class: " + object.getClass() + ", exception class: " + t.getClass()
                    + ", exception message: " + t.getMessage();
        }
    }
