    void openActionChooserDialog() {
        final LayoutInflater inflater = LayoutInflater.from(context);
        final SingleChoiceDialogViewBinding binding =
                SingleChoiceDialogViewBinding.inflate(inflater);

        final AlertDialog alertDialog = new AlertDialog.Builder(context)
                .setTitle(SLOT_TITLES[i])
                .setView(binding.getRoot())
                .setCancelable(true)
                .create();

        final View.OnClickListener radioButtonsClickListener = v -> {
            selectedAction = NotificationConstants.ALL_ACTIONS[v.getId()];
            updateInfo();
            alertDialog.dismiss();
        };

        for (int id = 0; id < NotificationConstants.ALL_ACTIONS.length; ++id) {
            final int action = NotificationConstants.ALL_ACTIONS[id];
            final RadioButton radioButton = ListRadioIconItemBinding.inflate(inflater)
                    .getRoot();

            // if present set action icon with correct color
            final int iconId = NotificationConstants.ACTION_ICONS[action];
            if (iconId != 0) {
                radioButton.setCompoundDrawablesRelativeWithIntrinsicBounds(0, 0, iconId, 0);

                final var color = ColorStateList.valueOf(ThemeHelper
                        .resolveColorFromAttr(context, android.R.attr.textColorPrimary));
                TextViewCompat.setCompoundDrawableTintList(radioButton, color);
            }

            radioButton.setText(NotificationConstants.getActionName(context, action));
            radioButton.setChecked(action == selectedAction);
            radioButton.setId(id);
            radioButton.setLayoutParams(new RadioGroup.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
            radioButton.setOnClickListener(radioButtonsClickListener);
            binding.list.addView(radioButton);
        }
        alertDialog.show();

        if (DeviceUtils.isTv(context)) {
            FocusOverlayView.setupFocusObserver(alertDialog);
        }
    }
