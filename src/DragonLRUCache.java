public class DragonLRUCache extends LRUCache {
    public DragonLRUCache(int capacity, Logger logger) {
        super(capacity, logger);
    }

    public void removeHead() {
        if (head.getState() == 'M' || head.getState() == 'D') {
            // write back to cache
            logger.incrementIdleTime(100);
        }
        hmap.remove(head.key);
        head.next.prev = null;
        head = head.next;
    }
}
