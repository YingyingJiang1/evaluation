    public String computeUsageLine(String prefix, CLI cli) {
        // initialise the string buffer
        StringBuilder buff;
        if (prefix == null) {
            buff = new StringBuilder("  ");
        } else {
            buff = new StringBuilder("  ").append(prefix);
            if (!prefix.endsWith(" ")) {
                buff.append(" ");
            }
        }

        buff.append(cli.getName()).append(" ");

        if (getOptionComparator() != null) {
            Collections.sort(cli.getOptions(), getOptionComparator());
        }

        // iterate over the options
        for (Option option : cli.getOptions()) {
            appendOption(buff, option);
            buff.append(" ");
        }

        // iterate over the arguments
        for (Argument arg : cli.getArguments()) {
            appendArgument(buff, arg, arg.isRequired());
            buff.append(" ");
        }

        return buff.toString();
    }
