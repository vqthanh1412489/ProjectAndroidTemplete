package com.google.android.gms.analytics.internal;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;
import android.net.Uri.Builder;
import android.os.Build.VERSION;
import android.text.TextUtils;
import com.facebook.internal.NativeProtocol;
import com.facebook.internal.Utility;
import com.facebook.share.internal.ShareConstants;
import com.google.android.exoplayer.C0989C;
import com.google.android.gms.common.internal.zzaa;
import com.google.android.gms.common.util.zzn;
import com.mp3download.zingmp3.BuildConfig;
import java.io.Closeable;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

class zzj extends zzd implements Closeable {
    private static final String dw;
    private static final String dx;
    private final zzal dA;
    private final zza dy;
    private final zzal dz;

    class zza extends SQLiteOpenHelper {
        final /* synthetic */ zzj dB;

        zza(zzj com_google_android_gms_analytics_internal_zzj, Context context, String str) {
            this.dB = com_google_android_gms_analytics_internal_zzj;
            super(context, str, null, 1);
        }

        private void zza(SQLiteDatabase sQLiteDatabase) {
            int i = 1;
            Set zzb = zzb(sQLiteDatabase, "hits2");
            String[] strArr = new String[]{"hit_id", "hit_string", "hit_time", "hit_url"};
            int i2 = 0;
            while (i2 < 4) {
                Object obj = strArr[i2];
                if (zzb.remove(obj)) {
                    i2++;
                } else {
                    String str = "Database hits2 is missing required column: ";
                    String valueOf = String.valueOf(obj);
                    throw new SQLiteException(valueOf.length() != 0 ? str.concat(valueOf) : new String(str));
                }
            }
            if (zzb.remove("hit_app_id")) {
                i = 0;
            }
            if (!zzb.isEmpty()) {
                throw new SQLiteException("Database hits2 has extra columns");
            } else if (i != 0) {
                sQLiteDatabase.execSQL("ALTER TABLE hits2 ADD COLUMN hit_app_id INTEGER");
            }
        }

        private boolean zza(SQLiteDatabase sQLiteDatabase, String str) {
            Object e;
            Throwable th;
            Cursor cursor = null;
            Cursor query;
            try {
                SQLiteDatabase sQLiteDatabase2 = sQLiteDatabase;
                query = sQLiteDatabase2.query("SQLITE_MASTER", new String[]{ShareConstants.WEB_DIALOG_PARAM_NAME}, "name=?", new String[]{str}, null, null, null);
                try {
                    boolean moveToFirst = query.moveToFirst();
                    if (query == null) {
                        return moveToFirst;
                    }
                    query.close();
                    return moveToFirst;
                } catch (SQLiteException e2) {
                    e = e2;
                    try {
                        this.dB.zzc("Error querying for table", str, e);
                        if (query != null) {
                            query.close();
                        }
                        return false;
                    } catch (Throwable th2) {
                        th = th2;
                        cursor = query;
                        if (cursor != null) {
                            cursor.close();
                        }
                        throw th;
                    }
                }
            } catch (SQLiteException e3) {
                e = e3;
                query = null;
                this.dB.zzc("Error querying for table", str, e);
                if (query != null) {
                    query.close();
                }
                return false;
            } catch (Throwable th3) {
                th = th3;
                if (cursor != null) {
                    cursor.close();
                }
                throw th;
            }
        }

        private Set<String> zzb(SQLiteDatabase sQLiteDatabase, String str) {
            Set<String> hashSet = new HashSet();
            Cursor rawQuery = sQLiteDatabase.rawQuery(new StringBuilder(String.valueOf(str).length() + 22).append("SELECT * FROM ").append(str).append(" LIMIT 0").toString(), null);
            try {
                String[] columnNames = rawQuery.getColumnNames();
                for (Object add : columnNames) {
                    hashSet.add(add);
                }
                return hashSet;
            } finally {
                rawQuery.close();
            }
        }

        private void zzb(SQLiteDatabase sQLiteDatabase) {
            int i = 0;
            Set zzb = zzb(sQLiteDatabase, "properties");
            String[] strArr = new String[]{"app_uid", "cid", "tid", NativeProtocol.WEB_DIALOG_PARAMS, "adid", "hits_count"};
            while (i < 6) {
                Object obj = strArr[i];
                if (zzb.remove(obj)) {
                    i++;
                } else {
                    String str = "Database properties is missing required column: ";
                    String valueOf = String.valueOf(obj);
                    throw new SQLiteException(valueOf.length() != 0 ? str.concat(valueOf) : new String(str));
                }
            }
            if (!zzb.isEmpty()) {
                throw new SQLiteException("Database properties table has extra columns");
            }
        }

        public SQLiteDatabase getWritableDatabase() {
            if (this.dB.dA.zzz(3600000)) {
                SQLiteDatabase writableDatabase;
                try {
                    writableDatabase = super.getWritableDatabase();
                } catch (SQLiteException e) {
                    this.dB.dA.start();
                    this.dB.zzew("Opening the database failed, dropping the table and recreating it");
                    this.dB.getContext().getDatabasePath(this.dB.zzade()).delete();
                    try {
                        writableDatabase = super.getWritableDatabase();
                        this.dB.dA.clear();
                    } catch (SQLiteException e2) {
                        this.dB.zze("Failed to open freshly created database", e2);
                        throw e2;
                    }
                }
                return writableDatabase;
            }
            throw new SQLiteException("Database open failed");
        }

        public void onCreate(SQLiteDatabase sQLiteDatabase) {
            zzx.zzfd(sQLiteDatabase.getPath());
        }

        public void onOpen(SQLiteDatabase sQLiteDatabase) {
            if (VERSION.SDK_INT < 15) {
                Cursor rawQuery = sQLiteDatabase.rawQuery("PRAGMA journal_mode=memory", null);
                try {
                    rawQuery.moveToFirst();
                } finally {
                    rawQuery.close();
                }
            }
            if (zza(sQLiteDatabase, "hits2")) {
                zza(sQLiteDatabase);
            } else {
                sQLiteDatabase.execSQL(zzj.dw);
            }
            if (zza(sQLiteDatabase, "properties")) {
                zzb(sQLiteDatabase);
            } else {
                sQLiteDatabase.execSQL("CREATE TABLE IF NOT EXISTS properties ( app_uid INTEGER NOT NULL, cid TEXT NOT NULL, tid TEXT NOT NULL, params TEXT NOT NULL, adid INTEGER NOT NULL, hits_count INTEGER NOT NULL, PRIMARY KEY (app_uid, cid, tid)) ;");
            }
        }

        public void onUpgrade(SQLiteDatabase sQLiteDatabase, int i, int i2) {
        }
    }

    static {
        dw = String.format("CREATE TABLE IF NOT EXISTS %s ( '%s' INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, '%s' INTEGER NOT NULL, '%s' TEXT NOT NULL, '%s' TEXT NOT NULL, '%s' INTEGER);", new Object[]{"hits2", "hit_id", "hit_time", "hit_url", "hit_string", "hit_app_id"});
        dx = String.format("SELECT MAX(%s) FROM %s WHERE 1;", new Object[]{"hit_time", "hits2"});
    }

    zzj(zzf com_google_android_gms_analytics_internal_zzf) {
        super(com_google_android_gms_analytics_internal_zzf);
        this.dz = new zzal(zzabz());
        this.dA = new zzal(zzabz());
        this.dy = new zza(this, com_google_android_gms_analytics_internal_zzf.getContext(), zzade());
    }

    private long zza(String str, String[] strArr, long j) {
        Cursor cursor = null;
        try {
            cursor = getWritableDatabase().rawQuery(str, strArr);
            if (cursor.moveToFirst()) {
                j = cursor.getLong(0);
                if (cursor != null) {
                    cursor.close();
                }
            } else if (cursor != null) {
                cursor.close();
            }
            return j;
        } catch (SQLiteException e) {
            zzd("Database error", str, e);
            throw e;
        } catch (Throwable th) {
            if (cursor != null) {
                cursor.close();
            }
        }
    }

    private void zzadd() {
        int zzafc = zzacb().zzafc();
        long zzacu = zzacu();
        if (zzacu > ((long) (zzafc - 1))) {
            List zzs = zzs((zzacu - ((long) zzafc)) + 1);
            zzd("Store full, deleting hits to make room, count", Integer.valueOf(zzs.size()));
            zzr(zzs);
        }
    }

    private String zzade() {
        zzacb();
        return zzacb().zzafe();
    }

    private static String zzas(Map<String, String> map) {
        zzaa.zzy(map);
        Builder builder = new Builder();
        for (Entry entry : map.entrySet()) {
            builder.appendQueryParameter((String) entry.getKey(), (String) entry.getValue());
        }
        String encodedQuery = builder.build().getEncodedQuery();
        return encodedQuery == null ? BuildConfig.FLAVOR : encodedQuery;
    }

    private long zzb(String str, String[] strArr) {
        Cursor cursor = null;
        try {
            cursor = getWritableDatabase().rawQuery(str, strArr);
            if (cursor.moveToFirst()) {
                long j = cursor.getLong(0);
                if (cursor != null) {
                    cursor.close();
                }
                return j;
            }
            throw new SQLiteException("Database returned empty set");
        } catch (SQLiteException e) {
            zzd("Database error", str, e);
            throw e;
        } catch (Throwable th) {
            if (cursor != null) {
                cursor.close();
            }
        }
    }

    private String zzd(zzab com_google_android_gms_analytics_internal_zzab) {
        return com_google_android_gms_analytics_internal_zzab.zzagc() ? zzacb().zzaes() : zzacb().zzaet();
    }

    private static String zze(zzab com_google_android_gms_analytics_internal_zzab) {
        zzaa.zzy(com_google_android_gms_analytics_internal_zzab);
        Builder builder = new Builder();
        for (Entry entry : com_google_android_gms_analytics_internal_zzab.zzmc().entrySet()) {
            String str = (String) entry.getKey();
            if (!("ht".equals(str) || "qt".equals(str) || "AppUID".equals(str))) {
                builder.appendQueryParameter(str, (String) entry.getValue());
            }
        }
        String encodedQuery = builder.build().getEncodedQuery();
        return encodedQuery == null ? BuildConfig.FLAVOR : encodedQuery;
    }

    public void beginTransaction() {
        zzacj();
        getWritableDatabase().beginTransaction();
    }

    public void close() {
        try {
            this.dy.close();
        } catch (SQLiteException e) {
            zze("Sql error closing database", e);
        } catch (IllegalStateException e2) {
            zze("Error closing database", e2);
        }
    }

    public void endTransaction() {
        zzacj();
        getWritableDatabase().endTransaction();
    }

    SQLiteDatabase getWritableDatabase() {
        try {
            return this.dy.getWritableDatabase();
        } catch (SQLiteException e) {
            zzd("Error opening database", e);
            throw e;
        }
    }

    boolean isEmpty() {
        return zzacu() == 0;
    }

    public void setTransactionSuccessful() {
        zzacj();
        getWritableDatabase().setTransactionSuccessful();
    }

    public long zza(long j, String str, String str2) {
        zzaa.zzib(str);
        zzaa.zzib(str2);
        zzacj();
        zzzx();
        return zza("SELECT hits_count FROM properties WHERE app_uid=? AND cid=? AND tid=?", new String[]{String.valueOf(j), str, str2}, 0);
    }

    public void zza(long j, String str) {
        zzaa.zzib(str);
        zzacj();
        zzzx();
        int delete = getWritableDatabase().delete("properties", "app_uid=? AND cid<>?", new String[]{String.valueOf(j), str});
        if (delete > 0) {
            zza("Deleted property records", Integer.valueOf(delete));
        }
    }

    public long zzacu() {
        zzzx();
        zzacj();
        return zzb("SELECT COUNT(*) FROM hits2", null);
    }

    public void zzacz() {
        zzzx();
        zzacj();
        getWritableDatabase().delete("hits2", null, null);
    }

    public void zzada() {
        zzzx();
        zzacj();
        getWritableDatabase().delete("properties", null, null);
    }

    public int zzadb() {
        zzzx();
        zzacj();
        if (!this.dz.zzz(86400000)) {
            return 0;
        }
        this.dz.start();
        zzes("Deleting stale hits (if any)");
        int delete = getWritableDatabase().delete("hits2", "hit_time < ?", new String[]{Long.toString(zzabz().currentTimeMillis() - 2592000000L)});
        zza("Deleted stale hits, count", Integer.valueOf(delete));
        return delete;
    }

    public long zzadc() {
        zzzx();
        zzacj();
        return zza(dx, null, 0);
    }

    public void zzb(zzh com_google_android_gms_analytics_internal_zzh) {
        zzaa.zzy(com_google_android_gms_analytics_internal_zzh);
        zzacj();
        zzzx();
        SQLiteDatabase writableDatabase = getWritableDatabase();
        String zzas = zzas(com_google_android_gms_analytics_internal_zzh.zzmc());
        ContentValues contentValues = new ContentValues();
        contentValues.put("app_uid", Long.valueOf(com_google_android_gms_analytics_internal_zzh.zzacr()));
        contentValues.put("cid", com_google_android_gms_analytics_internal_zzh.zzze());
        contentValues.put("tid", com_google_android_gms_analytics_internal_zzh.zzacs());
        contentValues.put("adid", Integer.valueOf(com_google_android_gms_analytics_internal_zzh.zzact() ? 1 : 0));
        contentValues.put("hits_count", Long.valueOf(com_google_android_gms_analytics_internal_zzh.zzacu()));
        contentValues.put(NativeProtocol.WEB_DIALOG_PARAMS, zzas);
        try {
            if (writableDatabase.insertWithOnConflict("properties", null, contentValues, 5) == -1) {
                zzew("Failed to insert/update a property (got -1)");
            }
        } catch (SQLiteException e) {
            zze("Error storing a property", e);
        }
    }

    public void zzc(zzab com_google_android_gms_analytics_internal_zzab) {
        zzaa.zzy(com_google_android_gms_analytics_internal_zzab);
        zzzx();
        zzacj();
        String zze = zze(com_google_android_gms_analytics_internal_zzab);
        if (zze.length() > Utility.DEFAULT_STREAM_BUFFER_SIZE) {
            zzaca().zza(com_google_android_gms_analytics_internal_zzab, "Hit length exceeds the maximum allowed size");
            return;
        }
        zzadd();
        SQLiteDatabase writableDatabase = getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("hit_string", zze);
        contentValues.put("hit_time", Long.valueOf(com_google_android_gms_analytics_internal_zzab.zzaga()));
        contentValues.put("hit_app_id", Integer.valueOf(com_google_android_gms_analytics_internal_zzab.zzafy()));
        contentValues.put("hit_url", zzd(com_google_android_gms_analytics_internal_zzab));
        try {
            long insert = writableDatabase.insert("hits2", null, contentValues);
            if (insert == -1) {
                zzew("Failed to insert a hit (got -1)");
            } else {
                zzb("Hit saved to database. db-id, hit", Long.valueOf(insert), com_google_android_gms_analytics_internal_zzab);
            }
        } catch (SQLiteException e) {
            zze("Error storing a hit", e);
        }
    }

    Map<String, String> zzex(String str) {
        if (TextUtils.isEmpty(str)) {
            return new HashMap(0);
        }
        try {
            if (!str.startsWith("?")) {
                String str2 = "?";
                String valueOf = String.valueOf(str);
                str = valueOf.length() != 0 ? str2.concat(valueOf) : new String(str2);
            }
            return zzn.zza(new URI(str), C0989C.UTF8_NAME);
        } catch (URISyntaxException e) {
            zze("Error parsing hit parameters", e);
            return new HashMap(0);
        }
    }

    Map<String, String> zzey(String str) {
        if (TextUtils.isEmpty(str)) {
            return new HashMap(0);
        }
        try {
            String str2 = "?";
            String valueOf = String.valueOf(str);
            return zzn.zza(new URI(valueOf.length() != 0 ? str2.concat(valueOf) : new String(str2)), C0989C.UTF8_NAME);
        } catch (URISyntaxException e) {
            zze("Error parsing property parameters", e);
            return new HashMap(0);
        }
    }

    public void zzr(List<Long> list) {
        zzaa.zzy(list);
        zzzx();
        zzacj();
        if (!list.isEmpty()) {
            int i;
            StringBuilder stringBuilder = new StringBuilder("hit_id");
            stringBuilder.append(" in (");
            for (i = 0; i < list.size(); i++) {
                Long l = (Long) list.get(i);
                if (l == null || l.longValue() == 0) {
                    throw new SQLiteException("Invalid hit id");
                }
                if (i > 0) {
                    stringBuilder.append(",");
                }
                stringBuilder.append(l);
            }
            stringBuilder.append(")");
            String stringBuilder2 = stringBuilder.toString();
            try {
                SQLiteDatabase writableDatabase = getWritableDatabase();
                zza("Deleting dispatched hits. count", Integer.valueOf(list.size()));
                i = writableDatabase.delete("hits2", stringBuilder2, null);
                if (i != list.size()) {
                    zzb("Deleted fewer hits then expected", Integer.valueOf(list.size()), Integer.valueOf(i), stringBuilder2);
                }
            } catch (SQLiteException e) {
                zze("Error deleting hits", e);
                throw e;
            }
        }
    }

    public List<Long> zzs(long j) {
        Cursor query;
        Object e;
        Throwable th;
        Cursor cursor = null;
        zzzx();
        zzacj();
        if (j <= 0) {
            return Collections.emptyList();
        }
        SQLiteDatabase writableDatabase = getWritableDatabase();
        List<Long> arrayList = new ArrayList();
        try {
            query = writableDatabase.query("hits2", new String[]{"hit_id"}, null, null, null, null, String.format("%s ASC", new Object[]{"hit_id"}), Long.toString(j));
            try {
                if (query.moveToFirst()) {
                    do {
                        arrayList.add(Long.valueOf(query.getLong(0)));
                    } while (query.moveToNext());
                }
                if (query != null) {
                    query.close();
                }
            } catch (SQLiteException e2) {
                e = e2;
                try {
                    zzd("Error selecting hit ids", e);
                    if (query != null) {
                        query.close();
                    }
                    return arrayList;
                } catch (Throwable th2) {
                    th = th2;
                    cursor = query;
                    if (cursor != null) {
                        cursor.close();
                    }
                    throw th;
                }
            }
        } catch (SQLiteException e3) {
            e = e3;
            query = null;
            zzd("Error selecting hit ids", e);
            if (query != null) {
                query.close();
            }
            return arrayList;
        } catch (Throwable th3) {
            th = th3;
            if (cursor != null) {
                cursor.close();
            }
            throw th;
        }
        return arrayList;
    }

    /* JADX WARNING: inconsistent code. */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public java.util.List<com.google.android.gms.analytics.internal.zzab> zzt(long r14) {
        /*
        r13 = this;
        r0 = 1;
        r1 = 0;
        r9 = 0;
        r2 = 0;
        r2 = (r14 > r2 ? 1 : (r14 == r2 ? 0 : -1));
        if (r2 < 0) goto L_0x008f;
    L_0x0009:
        com.google.android.gms.common.internal.zzaa.zzbt(r0);
        r13.zzzx();
        r13.zzacj();
        r0 = r13.getWritableDatabase();
        r1 = "hits2";
        r2 = 5;
        r2 = new java.lang.String[r2];	 Catch:{ SQLiteException -> 0x0092, all -> 0x00a2 }
        r3 = 0;
        r4 = "hit_id";
        r2[r3] = r4;	 Catch:{ SQLiteException -> 0x0092, all -> 0x00a2 }
        r3 = 1;
        r4 = "hit_time";
        r2[r3] = r4;	 Catch:{ SQLiteException -> 0x0092, all -> 0x00a2 }
        r3 = 2;
        r4 = "hit_string";
        r2[r3] = r4;	 Catch:{ SQLiteException -> 0x0092, all -> 0x00a2 }
        r3 = 3;
        r4 = "hit_url";
        r2[r3] = r4;	 Catch:{ SQLiteException -> 0x0092, all -> 0x00a2 }
        r3 = 4;
        r4 = "hit_app_id";
        r2[r3] = r4;	 Catch:{ SQLiteException -> 0x0092, all -> 0x00a2 }
        r3 = 0;
        r4 = 0;
        r5 = 0;
        r6 = 0;
        r7 = "%s ASC";
        r8 = 1;
        r8 = new java.lang.Object[r8];	 Catch:{ SQLiteException -> 0x0092, all -> 0x00a2 }
        r10 = 0;
        r11 = "hit_id";
        r8[r10] = r11;	 Catch:{ SQLiteException -> 0x0092, all -> 0x00a2 }
        r7 = java.lang.String.format(r7, r8);	 Catch:{ SQLiteException -> 0x0092, all -> 0x00a2 }
        r8 = java.lang.Long.toString(r14);	 Catch:{ SQLiteException -> 0x0092, all -> 0x00a2 }
        r9 = r0.query(r1, r2, r3, r4, r5, r6, r7, r8);	 Catch:{ SQLiteException -> 0x0092, all -> 0x00a2 }
        r10 = new java.util.ArrayList;	 Catch:{ SQLiteException -> 0x00a4, all -> 0x00a2 }
        r10.<init>();	 Catch:{ SQLiteException -> 0x00a4, all -> 0x00a2 }
        r0 = r9.moveToFirst();	 Catch:{ SQLiteException -> 0x00a4, all -> 0x00a2 }
        if (r0 == 0) goto L_0x0089;
    L_0x0059:
        r0 = 0;
        r6 = r9.getLong(r0);	 Catch:{ SQLiteException -> 0x00a4, all -> 0x00a2 }
        r0 = 1;
        r3 = r9.getLong(r0);	 Catch:{ SQLiteException -> 0x00a4, all -> 0x00a2 }
        r0 = 2;
        r0 = r9.getString(r0);	 Catch:{ SQLiteException -> 0x00a4, all -> 0x00a2 }
        r1 = 3;
        r1 = r9.getString(r1);	 Catch:{ SQLiteException -> 0x00a4, all -> 0x00a2 }
        r2 = 4;
        r8 = r9.getInt(r2);	 Catch:{ SQLiteException -> 0x00a4, all -> 0x00a2 }
        r2 = r13.zzex(r0);	 Catch:{ SQLiteException -> 0x00a4, all -> 0x00a2 }
        r5 = com.google.android.gms.analytics.internal.zzao.zzfn(r1);	 Catch:{ SQLiteException -> 0x00a4, all -> 0x00a2 }
        r0 = new com.google.android.gms.analytics.internal.zzab;	 Catch:{ SQLiteException -> 0x00a4, all -> 0x00a2 }
        r1 = r13;
        r0.<init>(r1, r2, r3, r5, r6, r8);	 Catch:{ SQLiteException -> 0x00a4, all -> 0x00a2 }
        r10.add(r0);	 Catch:{ SQLiteException -> 0x00a4, all -> 0x00a2 }
        r0 = r9.moveToNext();	 Catch:{ SQLiteException -> 0x00a4, all -> 0x00a2 }
        if (r0 != 0) goto L_0x0059;
    L_0x0089:
        if (r9 == 0) goto L_0x008e;
    L_0x008b:
        r9.close();
    L_0x008e:
        return r10;
    L_0x008f:
        r0 = r1;
        goto L_0x0009;
    L_0x0092:
        r0 = move-exception;
        r1 = r9;
    L_0x0094:
        r2 = "Error loading hits from the database";
        r13.zze(r2, r0);	 Catch:{ all -> 0x009a }
        throw r0;	 Catch:{ all -> 0x009a }
    L_0x009a:
        r0 = move-exception;
        r9 = r1;
    L_0x009c:
        if (r9 == 0) goto L_0x00a1;
    L_0x009e:
        r9.close();
    L_0x00a1:
        throw r0;
    L_0x00a2:
        r0 = move-exception;
        goto L_0x009c;
    L_0x00a4:
        r0 = move-exception;
        r1 = r9;
        goto L_0x0094;
        */
        throw new UnsupportedOperationException("Method not decompiled: com.google.android.gms.analytics.internal.zzj.zzt(long):java.util.List<com.google.android.gms.analytics.internal.zzab>");
    }

    public void zzu(long j) {
        zzzx();
        zzacj();
        List arrayList = new ArrayList(1);
        arrayList.add(Long.valueOf(j));
        zza("Deleting hit, id", Long.valueOf(j));
        zzr(arrayList);
    }

    /* JADX WARNING: inconsistent code. */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public java.util.List<com.google.android.gms.analytics.internal.zzh> zzv(long r14) {
        /*
        r13 = this;
        r13.zzacj();
        r13.zzzx();
        r0 = r13.getWritableDatabase();
        r9 = 0;
        r1 = 5;
        r2 = new java.lang.String[r1];	 Catch:{ SQLiteException -> 0x00ba, all -> 0x00b8 }
        r1 = 0;
        r3 = "cid";
        r2[r1] = r3;	 Catch:{ SQLiteException -> 0x00ba, all -> 0x00b8 }
        r1 = 1;
        r3 = "tid";
        r2[r1] = r3;	 Catch:{ SQLiteException -> 0x00ba, all -> 0x00b8 }
        r1 = 2;
        r3 = "adid";
        r2[r1] = r3;	 Catch:{ SQLiteException -> 0x00ba, all -> 0x00b8 }
        r1 = 3;
        r3 = "hits_count";
        r2[r1] = r3;	 Catch:{ SQLiteException -> 0x00ba, all -> 0x00b8 }
        r1 = 4;
        r3 = "params";
        r2[r1] = r3;	 Catch:{ SQLiteException -> 0x00ba, all -> 0x00b8 }
        r1 = r13.zzacb();	 Catch:{ SQLiteException -> 0x00ba, all -> 0x00b8 }
        r10 = r1.zzafd();	 Catch:{ SQLiteException -> 0x00ba, all -> 0x00b8 }
        r8 = java.lang.String.valueOf(r10);	 Catch:{ SQLiteException -> 0x00ba, all -> 0x00b8 }
        r3 = "app_uid=?";
        r1 = 1;
        r4 = new java.lang.String[r1];	 Catch:{ SQLiteException -> 0x00ba, all -> 0x00b8 }
        r1 = 0;
        r5 = java.lang.String.valueOf(r14);	 Catch:{ SQLiteException -> 0x00ba, all -> 0x00b8 }
        r4[r1] = r5;	 Catch:{ SQLiteException -> 0x00ba, all -> 0x00b8 }
        r1 = "properties";
        r5 = 0;
        r6 = 0;
        r7 = 0;
        r9 = r0.query(r1, r2, r3, r4, r5, r6, r7, r8);	 Catch:{ SQLiteException -> 0x00ba, all -> 0x00b8 }
        r11 = new java.util.ArrayList;	 Catch:{ SQLiteException -> 0x00a8, all -> 0x00b8 }
        r11.<init>();	 Catch:{ SQLiteException -> 0x00a8, all -> 0x00b8 }
        r0 = r9.moveToFirst();	 Catch:{ SQLiteException -> 0x00a8, all -> 0x00b8 }
        if (r0 == 0) goto L_0x008b;
    L_0x0053:
        r0 = 0;
        r3 = r9.getString(r0);	 Catch:{ SQLiteException -> 0x00a8, all -> 0x00b8 }
        r0 = 1;
        r4 = r9.getString(r0);	 Catch:{ SQLiteException -> 0x00a8, all -> 0x00b8 }
        r0 = 2;
        r0 = r9.getInt(r0);	 Catch:{ SQLiteException -> 0x00a8, all -> 0x00b8 }
        if (r0 == 0) goto L_0x009c;
    L_0x0064:
        r5 = 1;
    L_0x0065:
        r0 = 3;
        r0 = r9.getInt(r0);	 Catch:{ SQLiteException -> 0x00a8, all -> 0x00b8 }
        r6 = (long) r0;	 Catch:{ SQLiteException -> 0x00a8, all -> 0x00b8 }
        r0 = 4;
        r0 = r9.getString(r0);	 Catch:{ SQLiteException -> 0x00a8, all -> 0x00b8 }
        r8 = r13.zzey(r0);	 Catch:{ SQLiteException -> 0x00a8, all -> 0x00b8 }
        r0 = android.text.TextUtils.isEmpty(r3);	 Catch:{ SQLiteException -> 0x00a8, all -> 0x00b8 }
        if (r0 != 0) goto L_0x0080;
    L_0x007a:
        r0 = android.text.TextUtils.isEmpty(r4);	 Catch:{ SQLiteException -> 0x00a8, all -> 0x00b8 }
        if (r0 == 0) goto L_0x009e;
    L_0x0080:
        r0 = "Read property with empty client id or tracker id";
        r13.zzc(r0, r3, r4);	 Catch:{ SQLiteException -> 0x00a8, all -> 0x00b8 }
    L_0x0085:
        r0 = r9.moveToNext();	 Catch:{ SQLiteException -> 0x00a8, all -> 0x00b8 }
        if (r0 != 0) goto L_0x0053;
    L_0x008b:
        r0 = r11.size();	 Catch:{ SQLiteException -> 0x00a8, all -> 0x00b8 }
        if (r0 < r10) goto L_0x0096;
    L_0x0091:
        r0 = "Sending hits to too many properties. Campaign report might be incorrect";
        r13.zzev(r0);	 Catch:{ SQLiteException -> 0x00a8, all -> 0x00b8 }
    L_0x0096:
        if (r9 == 0) goto L_0x009b;
    L_0x0098:
        r9.close();
    L_0x009b:
        return r11;
    L_0x009c:
        r5 = 0;
        goto L_0x0065;
    L_0x009e:
        r0 = new com.google.android.gms.analytics.internal.zzh;	 Catch:{ SQLiteException -> 0x00a8, all -> 0x00b8 }
        r1 = r14;
        r0.<init>(r1, r3, r4, r5, r6, r8);	 Catch:{ SQLiteException -> 0x00a8, all -> 0x00b8 }
        r11.add(r0);	 Catch:{ SQLiteException -> 0x00a8, all -> 0x00b8 }
        goto L_0x0085;
    L_0x00a8:
        r0 = move-exception;
        r1 = r9;
    L_0x00aa:
        r2 = "Error loading hits from the database";
        r13.zze(r2, r0);	 Catch:{ all -> 0x00b0 }
        throw r0;	 Catch:{ all -> 0x00b0 }
    L_0x00b0:
        r0 = move-exception;
        r9 = r1;
    L_0x00b2:
        if (r9 == 0) goto L_0x00b7;
    L_0x00b4:
        r9.close();
    L_0x00b7:
        throw r0;
    L_0x00b8:
        r0 = move-exception;
        goto L_0x00b2;
    L_0x00ba:
        r0 = move-exception;
        r1 = r9;
        goto L_0x00aa;
        */
        throw new UnsupportedOperationException("Method not decompiled: com.google.android.gms.analytics.internal.zzj.zzv(long):java.util.List<com.google.android.gms.analytics.internal.zzh>");
    }

    protected void zzzy() {
    }
}