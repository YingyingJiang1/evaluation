        @Override
        protected AbstractFrame getIfContains(DetailAST identToFind, boolean lookForMethod) {
            final AbstractFrame frame;

            if (!lookForMethod
                    && containsFieldOrVariable(identToFind)) {
                frame = this;
            }
            else if (getParent().getType() == FrameType.TRY_WITH_RESOURCES_FRAME) {
                // Skip try-with-resources frame because resources cannot be accessed from catch
                frame = getParent().getParent().getIfContains(identToFind, lookForMethod);
            }
            else {
                frame = getParent().getIfContains(identToFind, lookForMethod);
            }
            return frame;
        }
