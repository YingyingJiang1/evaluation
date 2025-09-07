    public static void main(String[] args) throws Exception {

        try {
            int status = process(args, new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    System.exit(STATUS_OK);
                }
            });
            System.exit(status);
        } catch (Throwable e) {
            e.printStackTrace();
            CLI cli = CLIConfigurator.define(TelnetConsole.class);
            System.out.println(usage(cli));
            System.exit(STATUS_ERROR);
        }

    }
