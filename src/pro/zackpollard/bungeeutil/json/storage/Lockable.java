package pro.zackpollard.bungeeutil.json.storage;

/**
 * @Author zack
 * @Date 02/04/15.
 */
public abstract class Lockable {

    private transient long lastAccessed = 0;

    public void accessed() {

        lastAccessed = System.currentTimeMillis() + 60000;
    }

    public long getLastAccessed() {

        return lastAccessed;
    }

    public boolean compareLastAccessedWithNow() {

        return compareLastAccessedWithNow(true);
    }

    public boolean compareLastAccessedWithNow(boolean isBeforeNow) {

        if(lastAccessed + 120000L < System.currentTimeMillis()) {

            return isBeforeNow;
        } else {

            return !isBeforeNow;
        }
    }
}