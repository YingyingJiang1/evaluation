    private void setupMainFrame() {
        frame = new JFrame("Stirling-PDF");
        frame.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        frame.setUndecorated(true);
        frame.setOpacity(0.0f);

        JPanel contentPane = new JPanel(new BorderLayout());
        contentPane.setDoubleBuffered(true);
        contentPane.add(browser.getUIComponent(), BorderLayout.CENTER);
        frame.setContentPane(contentPane);

        frame.addWindowListener(
                new java.awt.event.WindowAdapter() {
                    @Override
                    public void windowClosing(java.awt.event.WindowEvent windowEvent) {
                        cleanup();
                        System.exit(0);
                    }
                });

        frame.setSize(UIScaling.scaleWidth(1280), UIScaling.scaleHeight(800));
        frame.setLocationRelativeTo(null);

        loadIcon();
    }
