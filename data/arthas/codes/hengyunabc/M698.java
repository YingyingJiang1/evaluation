        @Override
        public ScriptSupportCommand.Output print(String string) {
            process.write(string);
            return this;
        }
