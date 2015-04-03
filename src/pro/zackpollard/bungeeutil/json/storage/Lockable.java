package pro.zackpollard.bungeeutil.json.storage;

/**
 * @Author zack
 * @Date 02/04/15.
 */
public abstract class Lockable {

    private transient long lastAccessed = 0;

    public void accessed() {

        lastAccessed = System.currentTimeMillis();
    }

    public long getLastAccessed() {

        return lastAccessed;
    }

    public boolean compareLastAccessedWithNow() {

        return compareLastAccessedWithNow(true);
    }

    public boolean compareLastAccessedWithNow(boolean isBeforeNow) {

<<<<<<< HEAD
        if(lastAccessed < System.currentTimeMillis()) {
=======
        if(lastAccessed == System.currentTimeMillis()) {
>>>>>>> parent of d1b7b9e... Hopefully fixed the locking issues.

            return isBeforeNow;
        } else {

            return !isBeforeNow;
        }
    }
}
