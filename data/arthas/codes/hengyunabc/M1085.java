    public Command getCommand(String commandName) {
        Command command = null;
        for (CommandResolver resolver : resolvers) {
            // 内建命令在ShellLineHandler里提前处理了，所以这里不需要再查找内建命令
            if (resolver instanceof BuiltinCommandPack) {
                command = getCommand(resolver, commandName);
                if (command != null) {
                    break;
                }
            }
        }
        return command;
    }
