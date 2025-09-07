        public Frame pop() {
            finish();
            parent.addReferencedTypes(referencedTypes);
            return parent;
        }
