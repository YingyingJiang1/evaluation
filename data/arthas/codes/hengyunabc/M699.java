        @Override
        public ScriptSupportCommand.Output println(String string) {
            process.write(string).write("\n");
            return this;
        }
