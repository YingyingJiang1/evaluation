    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        if (oldVersion == 2) {
            db.execSQL("ALTER TABLE " + MISSIONS_TABLE_NAME_v2 + " ADD COLUMN " + KEY_KIND + " TEXT;");
            oldVersion++;
        }

        if (oldVersion == 3) {
            final String KEY_LOCATION = "location";
            final String KEY_NAME = "name";

            db.execSQL(MISSIONS_CREATE_TABLE);

            Cursor cursor = db.query(MISSIONS_TABLE_NAME_v2, null, null,
                    null, null, null, KEY_TIMESTAMP);

            int count = cursor.getCount();
            if (count > 0) {
                db.beginTransaction();
                while (cursor.moveToNext()) {
                    ContentValues values = new ContentValues();
                    values.put(KEY_SOURCE, cursor.getString(cursor.getColumnIndex(KEY_SOURCE)));
                    values.put(KEY_DONE, cursor.getString(cursor.getColumnIndex(KEY_DONE)));
                    values.put(KEY_TIMESTAMP, cursor.getLong(cursor.getColumnIndex(KEY_TIMESTAMP)));
                    values.put(KEY_KIND, cursor.getString(cursor.getColumnIndex(KEY_KIND)));
                    values.put(KEY_PATH, Uri.fromFile(
                            new File(
                                    cursor.getString(cursor.getColumnIndex(KEY_LOCATION)),
                                    cursor.getString(cursor.getColumnIndex(KEY_NAME))
                            )
                    ).toString());

                    db.insert(FINISHED_TABLE_NAME, null, values);
                }
                db.setTransactionSuccessful();
                db.endTransaction();
            }

            cursor.close();
            db.execSQL("DROP TABLE " + MISSIONS_TABLE_NAME_v2);
        }
    }
