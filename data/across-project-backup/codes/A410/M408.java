    @SuppressLint("RtlHardcoded")
    public WindowManager.LayoutParams retrievePopupLayoutParamsFromPrefs() {
        final SharedPreferences prefs = getPlayer().getPrefs();
        final Context context = getPlayer().getContext();

        final boolean popupRememberSizeAndPos = prefs.getBoolean(
                context.getString(R.string.popup_remember_size_pos_key), true);
        final float defaultSize = context.getResources().getDimension(R.dimen.popup_default_width);
        final float popupWidth = popupRememberSizeAndPos
                ? prefs.getFloat(context.getString(R.string.popup_saved_width_key), defaultSize)
                : defaultSize;
        final float popupHeight = getMinimumVideoHeight(popupWidth);

        final WindowManager.LayoutParams params = new WindowManager.LayoutParams(
                (int) popupWidth, (int) popupHeight,
                popupLayoutParamType(),
                IDLE_WINDOW_FLAGS,
                PixelFormat.TRANSLUCENT);
        params.gravity = Gravity.LEFT | Gravity.TOP;
        params.softInputMode = WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE;

        final int centerX = (int) (screenWidth / 2f - popupWidth / 2f);
        final int centerY = (int) (screenHeight / 2f - popupHeight / 2f);
        params.x = popupRememberSizeAndPos
                ? prefs.getInt(context.getString(R.string.popup_saved_x_key), centerX) : centerX;
        params.y = popupRememberSizeAndPos
                ? prefs.getInt(context.getString(R.string.popup_saved_y_key), centerY) : centerY;

        return params;
    }
