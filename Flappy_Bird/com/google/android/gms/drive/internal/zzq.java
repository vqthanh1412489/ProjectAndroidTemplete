package com.google.android.gms.drive.internal;

import android.os.RemoteException;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.common.api.Releasable;
import com.google.android.gms.common.api.Result;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.drive.CreateFileActivityBuilder;
import com.google.android.gms.drive.Drive;
import com.google.android.gms.drive.DriveApi;
import com.google.android.gms.drive.DriveApi.DriveContentsResult;
import com.google.android.gms.drive.DriveApi.DriveIdResult;
import com.google.android.gms.drive.DriveApi.MetadataBufferResult;
import com.google.android.gms.drive.DriveContents;
import com.google.android.gms.drive.DriveFile;
import com.google.android.gms.drive.DriveFolder;
import com.google.android.gms.drive.DriveId;
import com.google.android.gms.drive.MetadataBuffer;
import com.google.android.gms.drive.OpenFileActivityBuilder;
import com.google.android.gms.drive.query.Query;
import java.util.List;

public class zzq implements DriveApi {

    static class zza implements Releasable, DriveContentsResult {
        private final Status zzOt;
        private final DriveContents zzacW;

        public zza(Status status, DriveContents driveContents) {
            this.zzOt = status;
            this.zzacW = driveContents;
        }

        public DriveContents getDriveContents() {
            return this.zzacW;
        }

        public Status getStatus() {
            return this.zzOt;
        }

        public void release() {
            if (this.zzacW != null) {
                this.zzacW.zzpf();
            }
        }
    }

    private static class zzd implements DriveIdResult {
        private final Status zzOt;
        private final DriveId zzacT;

        public zzd(Status status, DriveId driveId) {
            this.zzOt = status;
            this.zzacT = driveId;
        }

        public DriveId getDriveId() {
            return this.zzacT;
        }

        public Status getStatus() {
            return this.zzOt;
        }
    }

    static class zzf implements MetadataBufferResult {
        private final Status zzOt;
        private final MetadataBuffer zzaeK;
        private final boolean zzaeL;

        public zzf(Status status, MetadataBuffer metadataBuffer, boolean z) {
            this.zzOt = status;
            this.zzaeK = metadataBuffer;
            this.zzaeL = z;
        }

        public MetadataBuffer getMetadataBuffer() {
            return this.zzaeK;
        }

        public Status getStatus() {
            return this.zzOt;
        }

        public void release() {
            if (this.zzaeK != null) {
                this.zzaeK.release();
            }
        }
    }

    static class zzc extends zzd {
        private final com.google.android.gms.common.api.zza.zzb<DriveIdResult> zzOs;

        public zzc(com.google.android.gms.common.api.zza.zzb<DriveIdResult> com_google_android_gms_common_api_zza_zzb_com_google_android_gms_drive_DriveApi_DriveIdResult) {
            this.zzOs = com_google_android_gms_common_api_zza_zzb_com_google_android_gms_drive_DriveApi_DriveIdResult;
        }

        public void zza(OnDriveIdResponse onDriveIdResponse) throws RemoteException {
            this.zzOs.zzm(new zzd(Status.zzXP, onDriveIdResponse.getDriveId()));
        }

        public void zza(OnMetadataResponse onMetadataResponse) throws RemoteException {
            this.zzOs.zzm(new zzd(Status.zzXP, new zzn(onMetadataResponse.zzpS()).getDriveId()));
        }

        public void zzt(Status status) throws RemoteException {
            this.zzOs.zzm(new zzd(status, null));
        }
    }

    private static class zzh extends zzd {
        private final com.google.android.gms.common.api.zza.zzb<DriveContentsResult> zzOs;

        public zzh(com.google.android.gms.common.api.zza.zzb<DriveContentsResult> com_google_android_gms_common_api_zza_zzb_com_google_android_gms_drive_DriveApi_DriveContentsResult) {
            this.zzOs = com_google_android_gms_common_api_zza_zzb_com_google_android_gms_drive_DriveApi_DriveContentsResult;
        }

        public void zza(OnContentsResponse onContentsResponse) throws RemoteException {
            this.zzOs.zzm(new zza(Status.zzXP, new zzt(onContentsResponse.zzpJ())));
        }

        public void zzt(Status status) throws RemoteException {
            this.zzOs.zzm(new zza(status, null));
        }
    }

    private static class zzi extends zzd {
        private final com.google.android.gms.common.api.zza.zzb<MetadataBufferResult> zzOs;

        public zzi(com.google.android.gms.common.api.zza.zzb<MetadataBufferResult> com_google_android_gms_common_api_zza_zzb_com_google_android_gms_drive_DriveApi_MetadataBufferResult) {
            this.zzOs = com_google_android_gms_common_api_zza_zzb_com_google_android_gms_drive_DriveApi_MetadataBufferResult;
        }

        public void zza(OnListEntriesResponse onListEntriesResponse) throws RemoteException {
            this.zzOs.zzm(new zzf(Status.zzXP, new MetadataBuffer(onListEntriesResponse.zzpP()), onListEntriesResponse.zzpQ()));
        }

        public void zzt(Status status) throws RemoteException {
            this.zzOs.zzm(new zzf(status, null, false));
        }
    }

    static abstract class zzb extends zzr<DriveContentsResult> {
        zzb(GoogleApiClient googleApiClient) {
            super(googleApiClient);
        }

        public /* synthetic */ Result createFailedResult(Status x0) {
            return zzu(x0);
        }

        public DriveContentsResult zzu(Status status) {
            return new zza(status, null);
        }
    }

    static abstract class zze extends zzr<DriveIdResult> {
        zze(GoogleApiClient googleApiClient) {
            super(googleApiClient);
        }

        public /* synthetic */ Result createFailedResult(Status x0) {
            return zzv(x0);
        }

        public DriveIdResult zzv(Status status) {
            return new zzd(status, null);
        }
    }

    static abstract class zzg extends zzr<MetadataBufferResult> {
        zzg(GoogleApiClient googleApiClient) {
            super(googleApiClient);
        }

        public /* synthetic */ Result createFailedResult(Status x0) {
            return zzw(x0);
        }

        public MetadataBufferResult zzw(Status status) {
            return new zzf(status, null, false);
        }
    }

    /* renamed from: com.google.android.gms.drive.internal.zzq.1 */
    class C09211 extends zzg {
        final /* synthetic */ Query zzaeG;
        final /* synthetic */ zzq zzaeH;

        C09211(zzq com_google_android_gms_drive_internal_zzq, GoogleApiClient googleApiClient, Query query) {
            this.zzaeH = com_google_android_gms_drive_internal_zzq;
            this.zzaeG = query;
            super(googleApiClient);
        }

        protected void zza(zzs com_google_android_gms_drive_internal_zzs) throws RemoteException {
            com_google_android_gms_drive_internal_zzs.zzpB().zza(new QueryRequest(this.zzaeG), new zzi(this));
        }
    }

    /* renamed from: com.google.android.gms.drive.internal.zzq.2 */
    class C09222 extends zzb {
        final /* synthetic */ zzq zzaeH;
        final /* synthetic */ int zzaeI;

        C09222(zzq com_google_android_gms_drive_internal_zzq, GoogleApiClient googleApiClient, int i) {
            this.zzaeH = com_google_android_gms_drive_internal_zzq;
            this.zzaeI = i;
            super(googleApiClient);
        }

        protected void zza(zzs com_google_android_gms_drive_internal_zzs) throws RemoteException {
            com_google_android_gms_drive_internal_zzs.zzpB().zza(new CreateContentsRequest(this.zzaeI), new zzh(this));
        }
    }

    /* renamed from: com.google.android.gms.drive.internal.zzq.3 */
    class C09233 extends zze {
        final /* synthetic */ zzq zzaeH;
        final /* synthetic */ String zzaeJ;

        C09233(zzq com_google_android_gms_drive_internal_zzq, GoogleApiClient googleApiClient, String str) {
            this.zzaeH = com_google_android_gms_drive_internal_zzq;
            this.zzaeJ = str;
            super(googleApiClient);
        }

        protected void zza(zzs com_google_android_gms_drive_internal_zzs) throws RemoteException {
            com_google_android_gms_drive_internal_zzs.zzpB().zza(new GetMetadataRequest(DriveId.zzcs(this.zzaeJ), false), new zzc(this));
        }
    }

    /* renamed from: com.google.android.gms.drive.internal.zzq.4 */
    class C09244 extends com.google.android.gms.drive.internal.zzr.zza {
        final /* synthetic */ zzq zzaeH;

        C09244(zzq com_google_android_gms_drive_internal_zzq, GoogleApiClient googleApiClient) {
            this.zzaeH = com_google_android_gms_drive_internal_zzq;
            super(googleApiClient);
        }

        protected void zza(zzs com_google_android_gms_drive_internal_zzs) throws RemoteException {
            com_google_android_gms_drive_internal_zzs.zzpB().zza(new zzbq(this));
        }
    }

    static class zzj extends com.google.android.gms.drive.internal.zzr.zza {
        zzj(GoogleApiClient googleApiClient, Status status) {
            super(googleApiClient);
            setResult(status);
        }

        protected void zza(zzs com_google_android_gms_drive_internal_zzs) {
        }
    }

    public PendingResult<Status> cancelPendingActions(GoogleApiClient apiClient, List<String> trackingTags) {
        return ((zzs) apiClient.zza(Drive.zzNX)).cancelPendingActions(apiClient, trackingTags);
    }

    public PendingResult<DriveIdResult> fetchDriveId(GoogleApiClient apiClient, String resourceId) {
        return apiClient.zza(new C09233(this, apiClient, resourceId));
    }

    public DriveFolder getAppFolder(GoogleApiClient apiClient) {
        if (apiClient.isConnected()) {
            DriveId zzpD = ((zzs) apiClient.zza(Drive.zzNX)).zzpD();
            return zzpD != null ? new zzw(zzpD) : null;
        } else {
            throw new IllegalStateException("Client must be connected");
        }
    }

    public DriveFile getFile(GoogleApiClient apiClient, DriveId driveId) {
        if (driveId == null) {
            throw new IllegalArgumentException("Id must be provided.");
        } else if (apiClient.isConnected()) {
            return new zzu(driveId);
        } else {
            throw new IllegalStateException("Client must be connected");
        }
    }

    public DriveFolder getFolder(GoogleApiClient apiClient, DriveId driveId) {
        if (driveId == null) {
            throw new IllegalArgumentException("Id must be provided.");
        } else if (apiClient.isConnected()) {
            return new zzw(driveId);
        } else {
            throw new IllegalStateException("Client must be connected");
        }
    }

    public DriveFolder getRootFolder(GoogleApiClient apiClient) {
        if (apiClient.isConnected()) {
            return new zzw(((zzs) apiClient.zza(Drive.zzNX)).zzpC());
        }
        throw new IllegalStateException("Client must be connected");
    }

    public CreateFileActivityBuilder newCreateFileActivityBuilder() {
        return new CreateFileActivityBuilder();
    }

    public PendingResult<DriveContentsResult> newDriveContents(GoogleApiClient apiClient) {
        return zza(apiClient, DriveFile.MODE_WRITE_ONLY);
    }

    public OpenFileActivityBuilder newOpenFileActivityBuilder() {
        return new OpenFileActivityBuilder();
    }

    public PendingResult<MetadataBufferResult> query(GoogleApiClient apiClient, Query query) {
        if (query != null) {
            return apiClient.zza(new C09211(this, apiClient, query));
        }
        throw new IllegalArgumentException("Query must be provided.");
    }

    public PendingResult<Status> requestSync(GoogleApiClient apiClient) {
        return apiClient.zzb(new C09244(this, apiClient));
    }

    public PendingResult<DriveContentsResult> zza(GoogleApiClient googleApiClient, int i) {
        return googleApiClient.zza(new C09222(this, googleApiClient, i));
    }
}
