    @Option(shortName = "s", longName = "search-express")
    @Description("Search-expression, to search the time fragments by ognl express.\n" +
            "The structure of 'advice' like conditional expression")
    public void setSearchExpress(String searchExpress) {
        this.searchExpress = searchExpress;
    }
