public class DragonLRUCache extends LRUCache {
    public DragonLRUCache(int capacity, Logger logger) {
        super(capacity, logger);
    }

    // todo custom removeHead method
    public void removeHead() {
        if (head.getState() == 'M' || head.getState() == 'S') {
            // write back to cache
            logger.incrementIdleTime(100);
        }
        if(head.getState() == 'S' || head.getState() == 'D') {

        }
        hmap.remove(head.key);
        head.next.prev = null;
        head = head.next;
    }
}
