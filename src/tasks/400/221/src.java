    public static void main(String[] args) {
        if (args.length < 3) {
            usage();
            return;
        }
        String host = args[0];
        int port = Integer.parseInt(args[1]);
        String op = args[2];
        if (op.equalsIgnoreCase("gettracemask")) {
            getTraceMask(host, port);
        } else if (op.equalsIgnoreCase("settracemask")) {
            setTraceMask(host, port, args[3]);
        } else if (op.equalsIgnoreCase("ruok")) {
            ruok(host, port);
        } else if (op.equalsIgnoreCase("kill")) {
            kill(host, port);
        } else if (op.equalsIgnoreCase("stat")) {
            stat(host, port);
        } else if (op.equalsIgnoreCase("dump")) {
            dump(host, port);
        } else {
            System.out.println("Unrecognized op: " + op);
        }
    }
