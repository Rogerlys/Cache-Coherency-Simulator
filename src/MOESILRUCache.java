public class MOESILRUCache extends MESILRUCache{
    public MOESILRUCache(int capacity, Logger logger) {
        super(capacity, logger);
    }

    public void removeHead() {
        if (head.getDirty() && (head.getState() =='O' || head.getState() == 'M' ||head.getState() == 'E')) {
            // write back to cache
            logger.incrementIdleTime(100);
            logger.incrementDataTraffic();
        }
        hmap.remove(head.key);
        head.next.prev = null;
        head = head.next;
    }
}
