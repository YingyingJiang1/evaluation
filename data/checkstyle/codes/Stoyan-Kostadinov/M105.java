    @Override
    public void link(String href) {
        final MutableAttributeSet attributes = new SinkEventAttributeSet();
        attributes.addAttribute(SinkEventAttributes.HREF, href);
        writeStartTag(HtmlMarkup.A, attributes);
    }
