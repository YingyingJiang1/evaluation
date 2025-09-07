    private static Intent applyInitialPathToPickerIntent(@NonNull final Context ctx,
                                                         @NonNull final Intent intent,
                                                         @Nullable final Uri initialPath,
                                                         @Nullable final String filename) {

        if (NewPipeSettings.useStorageAccessFramework(ctx)) {
            if (initialPath == null) {
                return intent; // nothing to do, no initial path provided
            }

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                return intent.putExtra(DocumentsContract.EXTRA_INITIAL_URI, initialPath);
            } else {
                return intent; // can't set initial path on API < 26
            }

        } else {
            if (initialPath == null && filename == null) {
                return intent; // nothing to do, no initial path and no file name provided
            }

            File file;
            if (initialPath == null) {
                // The only way to set the previewed filename in non-SAF FilePicker is to set a
                // starting path ending with that filename. So when the initialPath is null but
                // filename isn't just default to the external storage directory.
                file = Environment.getExternalStorageDirectory();
            } else {
                try {
                    file = Utils.getFileForUri(initialPath);
                } catch (final Throwable ignored) {
                    // getFileForUri() can't decode paths to 'storage', fallback to this
                    file = new File(initialPath.toString());
                }
            }

            // remove any filename at the end of the path (get the parent directory in that case)
            if (!file.exists() || !file.isDirectory()) {
                file = file.getParentFile();
                if (file == null || !file.exists()) {
                    // default to the external storage directory in case of an invalid path
                    file = Environment.getExternalStorageDirectory();
                }
                // else: file is surely a directory
            }

            if (filename != null) {
                // append a filename so that the non-SAF FilePicker shows it as the preview
                file = new File(file, filename);
            }

            return intent
                    .putExtra(FilePickerActivityHelper.EXTRA_START_PATH, file.getAbsolutePath());
        }
    }
