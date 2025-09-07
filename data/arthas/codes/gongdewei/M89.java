                @Override
                public void actionPerformed(ActionEvent e) {
                    try {
                        consoleReader.getCursorBuffer().clear(); // clear current line
                        telnet.getOutputStream().write(CTRL_C);
                        telnet.getOutputStream().flush();
                    } catch (Exception e1) {
                        e1.printStackTrace();
                    }
                }
