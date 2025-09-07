    protected void setupChild(Configuration childConf)
            throws CheckstyleException {
        if (childConf != null) {
            throw new CheckstyleException(childConf.getName() + " is not allowed as a child in "
                    + configuration.getName() + ". Please review 'Parent Module' section "
                    + "for this Check in web documentation if Check is standard.");
        }
    }
