class MESILRUCache extends LRUCache {
    public MESILRUCache(int capacity, Logger logger) {
        super(capacity, logger);
    }

    public void removeHead() {
        if (head.getDirty()) {
            // write back to cache
            logger.incrementIdleTime(100);
        }
        hmap.remove(head.key);
        head.next.prev = null;
        head = head.next;
    }
}
