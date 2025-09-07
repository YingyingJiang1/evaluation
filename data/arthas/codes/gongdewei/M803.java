            @Override
            public boolean handle(List<String> classNames, int segment) {
                process.appendResult(new ClassLoaderModel().setClassSet(new ClassSetVO(classLoaderVO, classNames, segment)));
                affect.rCnt(classNames.size());
                return !checkInterrupted(process);
            }
