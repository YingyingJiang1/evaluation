    @Override
    public void process(CommandProcess process) {
        if (!process.session().isTty()) {
            process.end(-1, "Command 'keymap' is only support tty session.");
            return;
        }

        InputStream inputrc = Helper.loadInputRcFile();
        try {
            TableElement table = new TableElement(1, 1, 2).leftCellPadding(1).rightCellPadding(1);
            table.row(true, label("Shortcut").style(Decoration.bold.bold()),
                            label("Description").style(Decoration.bold.bold()),
                            label("Name").style(Decoration.bold.bold()));

            BufferedReader br = new BufferedReader(new InputStreamReader(inputrc));
            String line;
            while ((line = br.readLine()) != null) {
                line = line.trim();
                if (line.startsWith("#") || "".equals(line)) {
                    continue;
                }
                String[] strings = line.split(":");
                if (strings.length == 2) {
                    table.row(strings[0], translate(strings[0]), strings[1]);
                } else {
                    table.row(line);
                }

            }
            process.write(RenderUtil.render(table, process.width()));
        } catch (IOException e) {
            logger.error("read inputrc file error.", e);
        } finally {
            IOUtils.close(inputrc);
            process.end();
        }
    }
