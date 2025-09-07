    public Http2Headers getEndHeader() {
        Http2Headers endHeader = new DefaultHttp2Headers().status("200");
        headers.forEach(endHeader::set);
        return endHeader;
    }
