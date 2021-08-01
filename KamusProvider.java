package smk.adzikro.mydictionary;

import android.app.SearchManager;
import android.content.ContentProvider;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.net.Uri;
import android.provider.BaseColumns;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;

import smk.adzikro.mydictionary.database.Kamus;

/**
 * Created by server on 10/21/17.
 */

public class KamusProvider extends ContentProvider {
    private static final String AUTHORITY = "smk.adzikro.mydictionary";
    private static final String BASE_PATH = "KamusProvider";
    public static final Uri CONTENT_URI = Uri.parse("content://"+AUTHORITY+"/"+BASE_PATH);

    public static final int KLIK_PD_KATA = 0;
    public static final int DAPAT_ARTI_KATA = 1;
    public static final int KATA_YG_KELUAR = 2;
    private static final int REFRESH_SHORTCUT = 3;
    private static final UriMatcher cocokan_URI_nya = buatkan_UriMatcher_nya();
    private static final String TAG ="Provider" ;
    private Kamus kamusku;
    public static final String MIME_TYPE_KATA = ContentResolver.CURSOR_DIR_BASE_TYPE +
            "/"+AUTHORITY;
    public static final String MIME_TYPE_ARTI_NYA = ContentResolver.CURSOR_ITEM_BASE_TYPE +
            "/"+AUTHORITY;

    private static UriMatcher buatkan_UriMatcher_nya() {
        UriMatcher mencocokanURI =  new UriMatcher(UriMatcher.NO_MATCH);
        mencocokanURI.addURI(AUTHORITY, BASE_PATH, KLIK_PD_KATA);
        mencocokanURI.addURI(AUTHORITY, BASE_PATH+"/#", DAPAT_ARTI_KATA);
        mencocokanURI.addURI(AUTHORITY, SearchManager.SUGGEST_URI_PATH_QUERY, KATA_YG_KELUAR);
        mencocokanURI.addURI(AUTHORITY, SearchManager.SUGGEST_URI_PATH_QUERY + "/*", KATA_YG_KELUAR);
        mencocokanURI.addURI(AUTHORITY, SearchManager.SUGGEST_URI_PATH_SHORTCUT, REFRESH_SHORTCUT);
        mencocokanURI.addURI(AUTHORITY, SearchManager.SUGGEST_URI_PATH_SHORTCUT + "/*", REFRESH_SHORTCUT);
        return mencocokanURI;
    }

    @Override
    public boolean onCreate() {
        Log.e(TAG,"provider onCreate");
        kamusku= new Kamus(getContext());
        return true;
    }

    @Override
    public Cursor query(Uri uri, String[] perkiraanKata, String pilihan, String[] pilihDariSemuaKataYgAda,
                        String menurutAbjad) {
        Log.e(TAG, "tah uri "+uri.toString());
        Log.e(TAG, "tah type uri "+getType(uri));
        switch (cocokan_URI_nya.match(uri)) {
            case KATA_YG_KELUAR:
                if (pilihDariSemuaKataYgAda == null) {
                    throw new IllegalArgumentException(
                            "tidak ada kata yg mau di pilih di: " + uri);
                }
                return getSuggestions(pilihDariSemuaKataYgAda[0]);
            case KLIK_PD_KATA:
                if (pilihDariSemuaKataYgAda == null) {
                    throw new IllegalArgumentException(
                            "tidak ada data yg mau di pilih di: " + uri);
                }
                return search(pilihDariSemuaKataYgAda[0]);
            case DAPAT_ARTI_KATA:
                return getWord(uri);
            default:
                throw new IllegalArgumentException("tak mengenal alamat Uri: " + uri);
        }
    }
    private Cursor getSuggestions(String deretanKataYgMuncul) {
        deretanKataYgMuncul = deretanKataYgMuncul.toLowerCase();
        String[] kolom_nya = new String[] {
                BaseColumns._ID,
                Kamus.FIELD_KATA,
                Kamus.FIELD_ARTI,
                SearchManager.SUGGEST_COLUMN_INTENT_DATA_ID};

        return kamusku.getWordMatches(deretanKataYgMuncul, kolom_nya);
    }

    private Cursor search(String kataYgDiCari) {
        kataYgDiCari = kataYgDiCari.toLowerCase();
        String[] kolom_nya = new String[] {
                BaseColumns._ID,
                Kamus.FIELD_KATA,
                Kamus.FIELD_ARTI
        };
        return kamusku.getWordMatches(kataYgDiCari, kolom_nya);
    }
    private Cursor getWord(Uri uri) {
        String rowId = uri.getLastPathSegment();
        String[] columns = new String[] {
                BaseColumns._ID,
                Kamus.FIELD_KATA,
                Kamus.FIELD_ARTI};
        return kamusku.getWord(rowId, columns);
    }

    @Nullable
    @Override
    public String getType(@NonNull Uri uri) {
        switch (cocokan_URI_nya.match(uri)) {
            case KLIK_PD_KATA:
                return MIME_TYPE_KATA;
            case DAPAT_ARTI_KATA:
                return MIME_TYPE_ARTI_NYA;
            case KATA_YG_KELUAR:
                return SearchManager.SUGGEST_MIME_TYPE;
            case REFRESH_SHORTCUT:
                return SearchManager.SHORTCUT_MIME_TYPE;
            default:
                throw new IllegalArgumentException("Unknown URL " + uri);
        }
    }

    @Nullable
    @Override
    public Uri insert(@NonNull Uri uri, @Nullable ContentValues values) {
        return null;
    }

    @Override
    public int delete(@NonNull Uri uri, @Nullable String selection, @Nullable String[] selectionArgs) {
        return 0;
    }

    @Override
    public int update(@NonNull Uri uri, @Nullable ContentValues values, @Nullable String selection, @Nullable String[] selectionArgs) {
        return 0;
    }
}
