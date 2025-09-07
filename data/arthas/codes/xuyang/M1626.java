    public void enhance(ArthasStreamObserver arthasStreamObserver) {
        EnhancerAffect effect = null;
        try {
            Instrumentation inst = arthasStreamObserver.getInstrumentation();
            AdviceListener listener = getAdviceListener(arthasStreamObserver);
            if (listener == null) {
                logger.error("advice listener is null");
                String msg = "advice listener is null, check arthas log";
//                arthasStreamObserver.appendResult(new EnhancerModel(effect, false, msg));
                arthasStreamObserver.end(-1, msg);
                return;
            }
            boolean skipJDKTrace = false;
            if(listener instanceof AbstractTraceAdviceListener) {
                skipJDKTrace = ((AbstractTraceAdviceListener) listener).getCommand().isSkipJDKTrace();
            }

            Enhancer enhancer = new Enhancer(listener, listener instanceof InvokeTraceable, skipJDKTrace, getClassNameMatcher(), getClassNameExcludeMatcher(), getMethodNameMatcher());
            // 注册通知监听器
            arthasStreamObserver.register(listener, enhancer);
            effect = enhancer.enhance(inst, this.maxNumOfMatchedClass);
            if (effect.getThrowable() != null) {
                String msg = "error happens when enhancing class: "+effect.getThrowable().getMessage();
//                arthasStreamObserver.appendResult(new EnhancerModel(effect, false, msg));
                arthasStreamObserver.end(-1, msg + ", check arthas log: " + LogUtil.loggingFile());
                return;
            }

            if (effect.cCnt() == 0 || effect.mCnt() == 0) {
                // no class effected
                if (!StringUtils.isEmpty(effect.getOverLimitMsg())) {
                    String msg = "no class effected";
//                    arthasStreamObserver.appendResult(new EnhancerModel(effect, false));
                    arthasStreamObserver.end(-1, msg);
                    return;
                }
                // might be method code too large
//                arthasStreamObserver.appendResult(new EnhancerModel(effect, false, "No class or method is affected"));

                String smCommand = Ansi.ansi().fg(Ansi.Color.GREEN).a("sm CLASS_NAME METHOD_NAME").reset().toString();
                String optionsCommand = Ansi.ansi().fg(Ansi.Color.GREEN).a("options unsafe true").reset().toString();
                String javaPackage = Ansi.ansi().fg(Ansi.Color.GREEN).a("java.*").reset().toString();
                String resetCommand = Ansi.ansi().fg(Ansi.Color.GREEN).a("reset CLASS_NAME").reset().toString();
                String logStr = Ansi.ansi().fg(Ansi.Color.GREEN).a(LogUtil.loggingFile()).reset().toString();
                String issueStr = Ansi.ansi().fg(Ansi.Color.GREEN).a("https://github.com/alibaba/arthas/issues/47").reset().toString();
                String msg = "No class or method is affected, try:\n"
                        + "1. Execute `" + smCommand + "` to make sure the method you are tracing actually exists (it might be in your parent class).\n"
                        + "2. Execute `" + optionsCommand + "`, if you want to enhance the classes under the `" + javaPackage + "` package.\n"
                        + "3. Execute `" + resetCommand + "` and try again, your method body might be too large.\n"
                        + "4. Match the constructor, use `<init>`, for example: `watch demo.MathGame <init>`\n"
                        + "5. Check arthas log: " + logStr + "\n"
                        + "6. Visit " + issueStr + " for more details.";
                arthasStreamObserver.end(-1, msg);
                return;
            }
            arthasStreamObserver.appendResult(new EnhancerModel(effect, true));

            //异步执行，在RpcAdviceListener中结束
        } catch (Throwable e) {
            String msg = "error happens when enhancing class: "+e.getMessage();
            logger.error(msg, e);
//            arthasStreamObserver.appendResult(new EnhancerModel(effect, false, msg));
            arthasStreamObserver.end(-1, msg);
        }
    }
