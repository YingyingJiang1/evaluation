                @Override
                public boolean handle(List<String> classNames, int segment) {
                    process.appendResult(new SearchClassModel(classNames, segment));
                    return true;
                }
