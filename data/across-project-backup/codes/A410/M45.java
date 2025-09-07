    public static List<PlaylistLocalItem> merge(
            final List<PlaylistMetadataEntry> localPlaylists,
            final List<PlaylistRemoteEntity> remotePlaylists) {

        // This algorithm is similar to the merge operation in merge sort.
        final List<PlaylistLocalItem> result = new ArrayList<>(
                localPlaylists.size() + remotePlaylists.size());
        final List<PlaylistLocalItem> itemsWithSameIndex = new ArrayList<>();

        int i = 0;
        int j = 0;
        while (i < localPlaylists.size()) {
            while (j < remotePlaylists.size()) {
                if (remotePlaylists.get(j).getDisplayIndex()
                        <= localPlaylists.get(i).getDisplayIndex()) {
                    addItem(result, remotePlaylists.get(j), itemsWithSameIndex);
                    j++;
                } else {
                    break;
                }
            }
            addItem(result, localPlaylists.get(i), itemsWithSameIndex);
            i++;
        }
        while (j < remotePlaylists.size()) {
            addItem(result, remotePlaylists.get(j), itemsWithSameIndex);
            j++;
        }
        addItemsWithSameIndex(result, itemsWithSameIndex);

        return result;
    }
