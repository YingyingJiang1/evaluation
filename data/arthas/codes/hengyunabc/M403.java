    public TreeView begin(String data) {
        Node n = current.find(data);
        if (n != null) {
            current = n;
        } else {
            current = new Node(current, data);
        }
        current.markBegin();
        return this;
    }
