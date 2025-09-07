    private void showError(@NonNull DownloadMission mission) {
        @StringRes int msg = R.string.general_error;
        String msgEx = null;

        switch (mission.errCode) {
            case 416:
                msg = R.string.error_http_unsupported_range;
                break;
            case 404:
                msg = R.string.error_http_not_found;
                break;
            case ERROR_NOTHING:
                return;// this never should happen
            case ERROR_FILE_CREATION:
                msg = R.string.error_file_creation;
                break;
            case ERROR_HTTP_NO_CONTENT:
                msg = R.string.error_http_no_content;
                break;
            case ERROR_PATH_CREATION:
                msg = R.string.error_path_creation;
                break;
            case ERROR_PERMISSION_DENIED:
                msg = R.string.permission_denied;
                break;
            case ERROR_SSL_EXCEPTION:
                msg = R.string.error_ssl_exception;
                break;
            case ERROR_UNKNOWN_HOST:
                msg = R.string.error_unknown_host;
                break;
            case ERROR_CONNECT_HOST:
                msg = R.string.error_connect_host;
                break;
            case ERROR_POSTPROCESSING_STOPPED:
                msg = R.string.error_postprocessing_stopped;
                break;
            case ERROR_POSTPROCESSING:
            case ERROR_POSTPROCESSING_HOLD:
                showError(mission, UserAction.DOWNLOAD_POSTPROCESSING, R.string.error_postprocessing_failed);
                return;
            case ERROR_INSUFFICIENT_STORAGE:
                msg = R.string.error_insufficient_storage_left;
                break;
            case ERROR_UNKNOWN_EXCEPTION:
                if (mission.errObject != null) {
                    showError(mission, UserAction.DOWNLOAD_FAILED, R.string.general_error);
                    return;
                } else {
                    msg = R.string.msg_error;
                    break;
                }
            case ERROR_PROGRESS_LOST:
                msg = R.string.error_progress_lost;
                break;
            case ERROR_TIMEOUT:
                msg = R.string.error_timeout;
                break;
            case ERROR_RESOURCE_GONE:
                msg = R.string.error_download_resource_gone;
                break;
            default:
                if (mission.errCode >= 100 && mission.errCode < 600) {
                    msgEx = "HTTP " + mission.errCode;
                } else if (mission.errObject == null) {
                    msgEx = "(not_decelerated_error_code)";
                } else {
                    showError(mission, UserAction.DOWNLOAD_FAILED, msg);
                    return;
                }
                break;
        }

        AlertDialog.Builder builder = new AlertDialog.Builder(mContext);

        if (msgEx != null)
            builder.setMessage(msgEx);
        else
            builder.setMessage(msg);

        // add report button for non-HTTP errors (range 100-599)
        if (mission.errObject != null && (mission.errCode < 100 || mission.errCode >= 600)) {
            @StringRes final int mMsg = msg;
            builder.setPositiveButton(R.string.error_report_title, (dialog, which) ->
                    showError(mission, UserAction.DOWNLOAD_FAILED, mMsg)
            );
        }

        builder.setNegativeButton(R.string.ok, (dialog, which) -> dialog.cancel())
                .setTitle(mission.storage.getName())
                .show();
    }
