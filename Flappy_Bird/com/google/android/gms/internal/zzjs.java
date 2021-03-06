package com.google.android.gms.internal;

import android.content.SharedPreferences;
import android.text.TextUtils;
import com.google.android.gms.cast.Cast;
import com.google.android.gms.cast.Cast.CastApi;
import com.google.android.gms.cast.Cast.MessageReceivedCallback;
import com.google.android.gms.cast.CastDevice;
import com.google.android.gms.cast.CastStatusCodes;
import com.google.android.gms.cast.RemoteMediaPlayer;
import com.google.android.gms.cast.games.GameManagerClient;
import com.google.android.gms.cast.games.GameManagerClient.GameManagerInstanceResult;
import com.google.android.gms.cast.games.GameManagerClient.GameManagerResult;
import com.google.android.gms.cast.games.GameManagerClient.Listener;
import com.google.android.gms.cast.games.GameManagerState;
import com.google.android.gms.cast.games.PlayerInfo;
import com.google.android.gms.cast.internal.zzf;
import com.google.android.gms.cast.internal.zzl;
import com.google.android.gms.cast.internal.zzo;
import com.google.android.gms.cast.internal.zzp;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.common.api.Result;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.drive.events.CompletionEvent;
import com.google.android.gms.games.GamesStatusCodes;
import com.google.example.games.basegameutils.GameHelper;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import org.json.JSONException;
import org.json.JSONObject;

public class zzjs extends com.google.android.gms.cast.internal.zzc {
    static final String NAMESPACE;
    private static final zzl zzQW;
    private String zzTA;
    private final Map<String, String> zzTn;
    private final List<zzp> zzTo;
    private final String zzTp;
    private final CastApi zzTq;
    private final GoogleApiClient zzTr;
    private zzjt zzTs;
    private boolean zzTt;
    private GameManagerState zzTu;
    private GameManagerState zzTv;
    private String zzTw;
    private JSONObject zzTx;
    private long zzTy;
    private Listener zzTz;
    private final SharedPreferences zztB;

    /* renamed from: com.google.android.gms.internal.zzjs.4 */
    class C04794 implements ResultCallback<Status> {
        final /* synthetic */ zzjs zzTB;
        final /* synthetic */ long zzTG;

        C04794(zzjs com_google_android_gms_internal_zzjs, long j) {
            this.zzTB = com_google_android_gms_internal_zzjs;
            this.zzTG = j;
        }

        public /* synthetic */ void onResult(Result x0) {
            zzm((Status) x0);
        }

        public void zzm(Status status) {
            if (!status.isSuccess()) {
                this.zzTB.zzb(this.zzTG, status.getStatusCode());
            }
        }
    }

    private static final class zzd implements GameManagerInstanceResult {
        private final Status zzOt;
        private final GameManagerClient zzTJ;

        zzd(Status status, GameManagerClient gameManagerClient) {
            this.zzOt = status;
            this.zzTJ = gameManagerClient;
        }

        public GameManagerClient getGameManagerClient() {
            return this.zzTJ;
        }

        public Status getStatus() {
            return this.zzOt;
        }
    }

    private static final class zze implements GameManagerResult {
        private final Status zzOt;
        private final String zzTL;
        private final long zzTM;
        private final JSONObject zzTN;

        zze(Status status, String str, long j, JSONObject jSONObject) {
            this.zzOt = status;
            this.zzTL = str;
            this.zzTM = j;
            this.zzTN = jSONObject;
        }

        public JSONObject getExtraMessageData() {
            return this.zzTN;
        }

        public String getPlayerId() {
            return this.zzTL;
        }

        public long getRequestId() {
            return this.zzTM;
        }

        public Status getStatus() {
            return this.zzOt;
        }
    }

    public abstract class zzb<R extends Result> extends com.google.android.gms.cast.internal.zzb<R> {
        final /* synthetic */ zzjs zzTB;
        protected zzo zzTa;

        public zzb(zzjs com_google_android_gms_internal_zzjs) {
            this.zzTB = com_google_android_gms_internal_zzjs;
            super(com_google_android_gms_internal_zzjs.zzTr);
        }

        public abstract void execute();

        protected void zza(com.google.android.gms.cast.internal.zze com_google_android_gms_cast_internal_zze) {
            execute();
        }

        public zzo zzlA() {
            return this.zzTa;
        }
    }

    public abstract class zza extends zzb<GameManagerResult> {
        final /* synthetic */ zzjs zzTB;

        /* renamed from: com.google.android.gms.internal.zzjs.zza.1 */
        class C04801 implements zzo {
            final /* synthetic */ zzjs zzTH;
            final /* synthetic */ zza zzTI;

            C04801(zza com_google_android_gms_internal_zzjs_zza, zzjs com_google_android_gms_internal_zzjs) {
                this.zzTI = com_google_android_gms_internal_zzjs_zza;
                this.zzTH = com_google_android_gms_internal_zzjs;
            }

            public void zza(long j, int i, Object obj) {
                if (obj == null) {
                    try {
                        this.zzTI.setResult(new zze(new Status(i, null, null), null, j, null));
                        return;
                    } catch (ClassCastException e) {
                        this.zzTI.setResult(this.zzTI.zzo(new Status(13)));
                        return;
                    }
                }
                zzju com_google_android_gms_internal_zzju = (zzju) obj;
                String playerId = com_google_android_gms_internal_zzju.getPlayerId();
                if (i == 0 && playerId != null) {
                    this.zzTI.zzTB.zzTA = playerId;
                }
                this.zzTI.setResult(new zze(new Status(i, com_google_android_gms_internal_zzju.zzlD(), null), playerId, com_google_android_gms_internal_zzju.getRequestId(), com_google_android_gms_internal_zzju.getExtraMessageData()));
            }

            public void zzy(long j) {
                this.zzTI.setResult(this.zzTI.zzo(new Status(RemoteMediaPlayer.STATUS_REPLACED)));
            }
        }

        public zza(zzjs com_google_android_gms_internal_zzjs) {
            this.zzTB = com_google_android_gms_internal_zzjs;
            super(com_google_android_gms_internal_zzjs);
            this.zzTa = new C04801(this, com_google_android_gms_internal_zzjs);
        }

        public /* synthetic */ Result createFailedResult(Status x0) {
            return zzo(x0);
        }

        public GameManagerResult zzo(Status status) {
            return new zze(status, null, -1, null);
        }
    }

    public abstract class zzc extends zzb<GameManagerInstanceResult> {
        final /* synthetic */ zzjs zzTB;
        private GameManagerClient zzTJ;

        /* renamed from: com.google.android.gms.internal.zzjs.zzc.1 */
        class C04811 implements zzo {
            final /* synthetic */ zzjs zzTH;
            final /* synthetic */ zzc zzTK;

            C04811(zzc com_google_android_gms_internal_zzjs_zzc, zzjs com_google_android_gms_internal_zzjs) {
                this.zzTK = com_google_android_gms_internal_zzjs_zzc;
                this.zzTH = com_google_android_gms_internal_zzjs;
            }

            public void zza(long j, int i, Object obj) {
                if (obj == null) {
                    try {
                        this.zzTK.setResult(new zzd(new Status(i, null, null), this.zzTK.zzTJ));
                        return;
                    } catch (ClassCastException e) {
                        this.zzTK.setResult(this.zzTK.zzp(new Status(13)));
                        return;
                    }
                }
                zzju com_google_android_gms_internal_zzju = (zzju) obj;
                zzjt zzlH = com_google_android_gms_internal_zzju.zzlH();
                if (zzlH == null || zzf.zza("1.0.0", zzlH.getVersion())) {
                    this.zzTK.setResult(new zzd(new Status(i, com_google_android_gms_internal_zzju.zzlD(), null), this.zzTK.zzTJ));
                    return;
                }
                this.zzTK.setResult(this.zzTK.zzp(new Status(GameManagerClient.STATUS_INCORRECT_VERSION, String.format(Locale.ROOT, "Incorrect Game Manager SDK version. Receiver: %s Sender: %s", new Object[]{zzlH.getVersion(), "1.0.0"}))));
                this.zzTK.zzTB.zzTs = null;
            }

            public void zzy(long j) {
                this.zzTK.setResult(this.zzTK.zzp(new Status(RemoteMediaPlayer.STATUS_REPLACED)));
            }
        }

        public zzc(zzjs com_google_android_gms_internal_zzjs, GameManagerClient gameManagerClient) {
            this.zzTB = com_google_android_gms_internal_zzjs;
            super(com_google_android_gms_internal_zzjs);
            this.zzTJ = gameManagerClient;
            this.zzTa = new C04811(this, com_google_android_gms_internal_zzjs);
        }

        public /* synthetic */ Result createFailedResult(Status x0) {
            return zzp(x0);
        }

        public GameManagerInstanceResult zzp(Status status) {
            return new zzd(status, null);
        }
    }

    /* renamed from: com.google.android.gms.internal.zzjs.1 */
    class C10791 extends zzc {
        final /* synthetic */ zzjs zzTB;

        /* renamed from: com.google.android.gms.internal.zzjs.1.1 */
        class C04781 implements MessageReceivedCallback {
            final /* synthetic */ C10791 zzTC;

            C04781(C10791 c10791) {
                this.zzTC = c10791;
            }

            public void onMessageReceived(CastDevice castDevice, String namespace, String message) {
                this.zzTC.zzTB.zzbB(message);
            }
        }

        C10791(zzjs com_google_android_gms_internal_zzjs, GameManagerClient gameManagerClient) {
            this.zzTB = com_google_android_gms_internal_zzjs;
            super(com_google_android_gms_internal_zzjs, gameManagerClient);
        }

        public void execute() {
            try {
                this.zzTB.zzTq.setMessageReceivedCallbacks(this.zzTB.zzTr, this.zzTB.getNamespace(), new C04781(this));
                this.zzTB.zzly();
                this.zzTB.zzlx();
                this.zzTB.zza(null, 1100, null, zzlA());
            } catch (IOException e) {
                zzlA().zza(-1, 8, null);
            } catch (IllegalStateException e2) {
                zzlA().zza(-1, 8, null);
            }
        }
    }

    /* renamed from: com.google.android.gms.internal.zzjs.2 */
    class C10802 extends zza {
        final /* synthetic */ zzjs zzTB;
        final /* synthetic */ int zzTD;
        final /* synthetic */ String zzTE;
        final /* synthetic */ JSONObject zzTF;

        C10802(zzjs com_google_android_gms_internal_zzjs, int i, String str, JSONObject jSONObject) {
            this.zzTB = com_google_android_gms_internal_zzjs;
            this.zzTD = i;
            this.zzTE = str;
            this.zzTF = jSONObject;
            super(com_google_android_gms_internal_zzjs);
        }

        public void execute() {
            int zzaJ = zzjv.zzaJ(this.zzTD);
            if (zzaJ == 0) {
                zzlA().zza(-1, GamesStatusCodes.STATUS_REQUEST_UPDATE_TOTAL_FAILURE, null);
                zzjs.zzQW.zzf("sendPlayerRequest for unsupported playerState: %d", Integer.valueOf(this.zzTD));
                return;
            }
            this.zzTB.zza(this.zzTE, zzaJ, this.zzTF, zzlA());
        }
    }

    /* renamed from: com.google.android.gms.internal.zzjs.3 */
    class C10813 extends zza {
        final /* synthetic */ zzjs zzTB;
        final /* synthetic */ String zzTE;
        final /* synthetic */ JSONObject zzTF;

        C10813(zzjs com_google_android_gms_internal_zzjs, String str, JSONObject jSONObject) {
            this.zzTB = com_google_android_gms_internal_zzjs;
            this.zzTE = str;
            this.zzTF = jSONObject;
            super(com_google_android_gms_internal_zzjs);
        }

        public void execute() {
            this.zzTB.zza(this.zzTE, 6, this.zzTF, zzlA());
        }
    }

    static {
        NAMESPACE = zzf.zzbE("com.google.cast.games");
        zzQW = new zzl("GameManagerChannel");
    }

    public zzjs(GoogleApiClient googleApiClient, String str, CastApi castApi) throws IllegalArgumentException, IllegalStateException {
        super(NAMESPACE, "CastGameManagerChannel", null);
        this.zzTn = new ConcurrentHashMap();
        this.zzTt = false;
        this.zzTy = 0;
        if (TextUtils.isEmpty(str)) {
            throw new IllegalArgumentException("castSessionId cannot be null.");
        } else if (googleApiClient != null && googleApiClient.isConnected() && googleApiClient.hasConnectedApi(Cast.API)) {
            this.zzTo = new ArrayList();
            this.zzTp = str;
            this.zzTq = castApi;
            this.zzTr = googleApiClient;
            this.zztB = r0.getApplicationContext().getSharedPreferences(String.format(Locale.ROOT, "%s.%s", new Object[]{googleApiClient.getContext().getApplicationContext().getPackageName(), "game_manager_channel_data"}), 0);
            this.zzTv = null;
            this.zzTu = new zzjw(0, 0, "", null, new ArrayList(), "", -1);
        } else {
            throw new IllegalArgumentException("googleApiClient needs to be connected and contain the Cast.API API.");
        }
    }

    private JSONObject zza(long j, String str, int i, JSONObject jSONObject) {
        try {
            JSONObject jSONObject2 = new JSONObject();
            jSONObject2.put("requestId", j);
            jSONObject2.put("type", i);
            jSONObject2.put("extraMessageData", jSONObject);
            jSONObject2.put("playerId", str);
            jSONObject2.put("playerToken", zzbA(str));
            return jSONObject2;
        } catch (JSONException e) {
            zzQW.zzf("JSONException when trying to create a message: %s", e.getMessage());
            return null;
        }
    }

    private synchronized void zza(zzju com_google_android_gms_internal_zzju) {
        Object obj = 1;
        synchronized (this) {
            if (com_google_android_gms_internal_zzju.zzlC() != 1) {
                obj = null;
            }
            this.zzTv = this.zzTu;
            if (!(obj == null || com_google_android_gms_internal_zzju.zzlH() == null)) {
                this.zzTs = com_google_android_gms_internal_zzju.zzlH();
            }
            if (isInitialized()) {
                Collection arrayList = new ArrayList();
                for (zzjy com_google_android_gms_internal_zzjy : com_google_android_gms_internal_zzju.zzlE()) {
                    String playerId = com_google_android_gms_internal_zzjy.getPlayerId();
                    arrayList.add(new zzjx(playerId, com_google_android_gms_internal_zzjy.getPlayerState(), com_google_android_gms_internal_zzjy.getPlayerData(), this.zzTn.containsKey(playerId)));
                }
                this.zzTu = new zzjw(com_google_android_gms_internal_zzju.getLobbyState(), com_google_android_gms_internal_zzju.getGameplayState(), com_google_android_gms_internal_zzju.zzlF(), com_google_android_gms_internal_zzju.getGameData(), arrayList, this.zzTs.zzlB(), this.zzTs.getMaxPlayers());
                PlayerInfo player = this.zzTu.getPlayer(com_google_android_gms_internal_zzju.getPlayerId());
                if (player != null && player.isControllable() && com_google_android_gms_internal_zzju.zzlC() == 2) {
                    this.zzTw = com_google_android_gms_internal_zzju.getPlayerId();
                    this.zzTx = com_google_android_gms_internal_zzju.getExtraMessageData();
                }
            }
        }
    }

    private void zza(String str, int i, JSONObject jSONObject, zzo com_google_android_gms_cast_internal_zzo) {
        long j = 1 + this.zzTy;
        this.zzTy = j;
        JSONObject zza = zza(j, str, i, jSONObject);
        if (zza == null) {
            com_google_android_gms_cast_internal_zzo.zza(-1, GamesStatusCodes.STATUS_REQUEST_UPDATE_TOTAL_FAILURE, null);
            zzQW.zzf("Not sending request because it was invalid.", new Object[0]);
            return;
        }
        zzp com_google_android_gms_cast_internal_zzp = new zzp(30000);
        com_google_android_gms_cast_internal_zzp.zza(j, com_google_android_gms_cast_internal_zzo);
        this.zzTo.add(com_google_android_gms_cast_internal_zzp);
        zzQ(true);
        this.zzTq.sendMessage(this.zzTr, getNamespace(), zza.toString()).setResultCallback(new C04794(this, j));
    }

    private int zzaI(int i) {
        switch (i) {
            case GameHelper.CLIENT_NONE /*0*/:
                return 0;
            case CompletionEvent.STATUS_FAILURE /*1*/:
                return GamesStatusCodes.STATUS_REQUEST_UPDATE_TOTAL_FAILURE;
            case CompletionEvent.STATUS_CONFLICT /*2*/:
                return CastStatusCodes.NOT_ALLOWED;
            case CompletionEvent.STATUS_CANCELED /*3*/:
                return GameManagerClient.STATUS_INCORRECT_VERSION;
            case GameHelper.CLIENT_APPSTATE /*4*/:
                return GameManagerClient.STATUS_TOO_MANY_PLAYERS;
            default:
                zzQW.zzf("Unknown GameManager protocol status code: " + i, new Object[0]);
                return 13;
        }
    }

    private void zzb(long j, int i, Object obj) {
        Iterator it = this.zzTo.iterator();
        while (it.hasNext()) {
            if (((zzp) it.next()).zzc(j, i, obj)) {
                it.remove();
            }
        }
    }

    private synchronized void zzlv() throws IllegalStateException {
        if (!isInitialized()) {
            throw new IllegalStateException("Attempted to perform an operation on the GameManagerChannel before it is initialized.");
        } else if (isDisposed()) {
            throw new IllegalStateException("Attempted to perform an operation on the GameManagerChannel after it has been disposed.");
        }
    }

    private void zzlw() {
        if (this.zzTz != null) {
            if (!(this.zzTv == null || this.zzTu.equals(this.zzTv))) {
                this.zzTz.onStateChanged(this.zzTu, this.zzTv);
            }
            if (!(this.zzTx == null || this.zzTw == null)) {
                this.zzTz.onGameMessageReceived(this.zzTw, this.zzTx);
            }
        }
        this.zzTv = null;
        this.zzTw = null;
        this.zzTx = null;
    }

    private synchronized void zzlx() {
        try {
            JSONObject jSONObject = new JSONObject();
            jSONObject.put("castSessionId", this.zzTp);
            jSONObject.put("playerTokenMap", new JSONObject(this.zzTn));
            this.zztB.edit().putString("save_data", jSONObject.toString()).commit();
        } catch (JSONException e) {
            zzQW.zzf("Error while saving data: %s", e.getMessage());
        }
    }

    private synchronized void zzly() {
        String string = this.zztB.getString("save_data", null);
        if (string != null) {
            try {
                JSONObject jSONObject = new JSONObject(string);
                if (this.zzTp.equals(jSONObject.getString("castSessionId"))) {
                    jSONObject = jSONObject.getJSONObject("playerTokenMap");
                    Iterator keys = jSONObject.keys();
                    while (keys.hasNext()) {
                        string = (String) keys.next();
                        this.zzTn.put(string, jSONObject.getString(string));
                    }
                    this.zzTy = 0;
                }
            } catch (JSONException e) {
                zzQW.zzf("Error while loading data: %s", e.getMessage());
            }
        }
    }

    public synchronized void dispose() throws IllegalStateException {
        if (!this.zzTt) {
            this.zzTu = null;
            this.zzTv = null;
            this.zzTw = null;
            this.zzTx = null;
            this.zzTt = true;
            try {
                this.zzTq.removeMessageReceivedCallbacks(this.zzTr, getNamespace());
            } catch (IOException e) {
                zzQW.zzf("Exception while detaching game manager channel.", e);
            }
        }
    }

    public synchronized GameManagerState getCurrentState() throws IllegalStateException {
        zzlv();
        return this.zzTu;
    }

    public synchronized String getLastUsedPlayerId() throws IllegalStateException {
        zzlv();
        return this.zzTA;
    }

    public synchronized boolean isDisposed() {
        return this.zzTt;
    }

    public synchronized boolean isInitialized() {
        return this.zzTs != null;
    }

    public synchronized void sendGameMessage(String playerId, JSONObject extraMessageData) throws IllegalStateException {
        zzlv();
        long j = 1 + this.zzTy;
        this.zzTy = j;
        JSONObject zza = zza(j, playerId, 7, extraMessageData);
        if (zza != null) {
            this.zzTq.sendMessage(this.zzTr, getNamespace(), zza.toString());
        }
    }

    public synchronized PendingResult<GameManagerResult> sendGameRequest(String playerId, JSONObject extraMessageData) throws IllegalStateException {
        zzlv();
        return this.zzTr.zzb(new C10813(this, playerId, extraMessageData));
    }

    public synchronized void setListener(Listener listener) {
        this.zzTz = listener;
    }

    public synchronized PendingResult<GameManagerInstanceResult> zza(GameManagerClient gameManagerClient) throws IllegalArgumentException {
        if (gameManagerClient == null) {
            throw new IllegalArgumentException("gameManagerClient can't be null.");
        }
        return this.zzTr.zzb(new C10791(this, gameManagerClient));
    }

    public synchronized PendingResult<GameManagerResult> zza(String str, int i, JSONObject jSONObject) throws IllegalStateException {
        zzlv();
        return this.zzTr.zzb(new C10802(this, i, str, jSONObject));
    }

    public void zzb(long j, int i) {
        zzb(j, i, null);
    }

    public synchronized String zzbA(String str) throws IllegalStateException {
        return str == null ? null : (String) this.zzTn.get(str);
    }

    public final void zzbB(String str) {
        zzQW.zzb("message received: %s", str);
        try {
            zzju zzh = zzju.zzh(new JSONObject(str));
            if (zzh == null) {
                zzQW.zzf("Could not parse game manager message from string: %s", str);
            } else if ((isInitialized() || zzh.zzlH() != null) && !isDisposed()) {
                int i = zzh.zzlC() == 1 ? 1 : 0;
                if (!(i == 0 || TextUtils.isEmpty(zzh.zzlG()))) {
                    this.zzTn.put(zzh.getPlayerId(), zzh.zzlG());
                    zzlx();
                }
                if (zzh.getStatusCode() == 0) {
                    zza(zzh);
                } else {
                    zzQW.zzf("Not updating from game message because the message contains error code: %d", Integer.valueOf(zzh.getStatusCode()));
                }
                int zzaI = zzaI(zzh.getStatusCode());
                if (i != 0) {
                    zzb(zzh.getRequestId(), zzaI, zzh);
                }
                if (isInitialized() && zzaI == 0) {
                    zzlw();
                }
            }
        } catch (JSONException e) {
            zzQW.zzf("Message is malformed (%s); ignoring: %s", e.getMessage(), str);
        }
    }

    protected boolean zzz(long j) {
        boolean z;
        Iterator it = this.zzTo.iterator();
        while (it.hasNext()) {
            if (((zzp) it.next()).zzd(j, 15)) {
                it.remove();
            }
        }
        synchronized (zzp.zzVr) {
            for (zzp zzme : this.zzTo) {
                if (zzme.zzme()) {
                    z = true;
                    break;
                }
            }
            z = false;
        }
        return z;
    }
}
