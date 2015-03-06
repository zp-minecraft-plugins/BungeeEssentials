package pro.zackpollard.bungeeutil.json.storage;

import pro.zackpollard.bungeeutil.BungeeEssentials;
import pro.zackpollard.bungeeutil.utils.Utils;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.UUID;

public class GSONBan {

    private transient boolean fileChanged = false;
    private transient String timestampFormatted = null;

    private long timestamp;
    private long duration = 0;
    private String reason;
    private UUID bannerUUID;

    public long getTimestamp() {
        return this.timestamp;
    }

    public String getTimestampFormatted() {

        if(this.timestampFormatted == null) {

            SimpleDateFormat format = new SimpleDateFormat(BungeeEssentials.getInstance().getConfigs().getMessages().getTimeStampFormat());
            Date date = new Date(this.timestamp);
            this.timestampFormatted = format.format(date);
        }

        return this.timestampFormatted;
    }

    public long getDuration() {
        return this.duration;
    }

    public long getRemainingTime() {
        return (this.timestamp + this.duration) - System.currentTimeMillis();
    }

    public String getRemainingTimeFormatted() {

        long amountTimeSeconds = this.getRemainingTime();

        String message = BungeeEssentials.getInstance().getConfigs().getMessages().getBanRemainingDurationFormat();

        return Utils.parseStringTimeInfo(message, amountTimeSeconds);
    }

    public String getReason() {
        return this.reason;
    }

    public UUID getBannerUUID() {
        return this.bannerUUID;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
        this.fileChanged = true;
    }

    public void setTimestampNow() {

        this.timestamp = System.currentTimeMillis();
        this.fileChanged = true;
    }

    public void setDuration(long duration) {
        this.duration = duration;
        this.fileChanged = true;
    }

    public void setReason(String reason) {
        this.reason = reason;
        this.fileChanged = true;
    }

    public void setBannerUUID(UUID bannerUUID) {

        this.bannerUUID = bannerUUID;
        this.fileChanged = true;
    }

    public boolean isFileChanged() {
        return this.fileChanged;
    }
}