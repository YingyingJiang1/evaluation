    private void onItemSelectedSetFileName() {
        final String fileName = FilenameUtils.createFilename(getContext(), currentInfo.getName());
        final String prevFileName = Optional.ofNullable(dialogBinding.fileName.getText())
                .map(Object::toString)
                .orElse("");

        if (prevFileName.isEmpty()
                || prevFileName.equals(fileName)
                || prevFileName.startsWith(getString(R.string.caption_file_name, fileName, ""))) {
            // only update the file name field if it was not edited by the user

            switch (dialogBinding.videoAudioGroup.getCheckedRadioButtonId()) {
                case R.id.audio_button:
                case R.id.video_button:
                    if (!prevFileName.equals(fileName)) {
                        // since the user might have switched between audio and video, the correct
                        // text might already be in place, so avoid resetting the cursor position
                        dialogBinding.fileName.setText(fileName);
                    }
                    break;

                case R.id.subtitle_button:
                    final String setSubtitleLanguageCode = subtitleStreamsAdapter
                            .getItem(selectedSubtitleIndex).getLanguageTag();
                    // this will reset the cursor position, which is bad UX, but it can't be avoided
                    dialogBinding.fileName.setText(getString(
                            R.string.caption_file_name, fileName, setSubtitleLanguageCode));
                    break;
            }
        }
    }
