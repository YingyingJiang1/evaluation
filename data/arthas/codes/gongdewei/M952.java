    public void addCommandVO(CommandVO commandVO){
        if (commands == null) {
            commands = new ArrayList<CommandVO>();
        }
        this.commands.add(commandVO);
    }
