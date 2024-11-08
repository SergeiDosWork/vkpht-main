package me.goodt.vkpht.common.application.util;

import lombok.extern.slf4j.Slf4j;

import java.util.Date;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/** Параллельный хэш-мап, с ограничением времени жизни элементов */
@Slf4j
public class ExpiredMap<K, V> extends ConcurrentHashMap<K, V> {

    /** Хранение точки времени поступления в map для каждого элемента */
    private final Map<K, Long> timeMap = new ConcurrentHashMap<>();

    /** Период, в течение которого запись будет считаться "живой" */
    private final long expiryInMillis;

    /** Периодичность срабатывания потока, очищающего map */
    private final long cleanEveryMillis;

    /** Название кэша, для вывода в логах */
    private final String cacheName;

    /**
     * @param cacheName
     *            Название сервиса, используемое в логах
     * @param expiryInMillis
     *            Кол-во миллисекунд, через которое объект будет удалён
     * @param cleanEveryMillis
     *            Промежуток времени, через который будет запускаться cleaner для очистки устаревших значений
     */
    public ExpiredMap(String cacheName, long expiryInMillis, long cleanEveryMillis) {
        this.cacheName = cacheName;
        this.expiryInMillis = expiryInMillis;
        this.cleanEveryMillis = cleanEveryMillis;
        log.debug("Initialize cache for \"{}\" with expire time {} sec and cleaner time {} sec", cacheName, expiryInMillis / 1000, cleanEveryMillis / 1000);
        initialize();
    }

    private void initialize() {
        new CleanerThread().start();
    }

    @Override
    public V put(K key, V value) {
        Date date = new Date();
        timeMap.put(key, date.getTime());
        log.debug("Inserting element to \"{}\": {} : {} : {}", this.cacheName, date, key, value);
        return super.put(key, value);
    }

    @Override
    public void putAll(Map<? extends K, ? extends V> m) {
        for (K key : m.keySet()) {
            put(key, m.get(key));
        }
    }

    @Override
    public V putIfAbsent(K key, V value) {
        if (!containsKey(key)) {
            return put(key, value);
        } else {
            return get(key);
        }
    }

    /**
     * Класс-cleaner, удаляющий элементы map с просроченным сроком годности
     */
    private class CleanerThread extends Thread {
        @Override
        public void run() {
            log.debug("Initiating Cleaner Thread for \"{}\"", ExpiredMap.this.cacheName);
            while (true) {
                cleanMap();
                try {
                    Thread.sleep(cleanEveryMillis);
                } catch (InterruptedException ex) {
                    log.error(ex.getMessage());
                }
            }
        }

        /**
         * Метод очищает записи с истекшим сроком годности
         */
        private void cleanMap() {
            long currentTime = new Date().getTime();
            for (K key : timeMap.keySet()) {
                if (currentTime > (timeMap.get(key) + expiryInMillis)) {
                    V value = remove(key);
                    timeMap.remove(key);
                    log.debug("Removing expired element from \"{}\": {} : {} : {}", ExpiredMap.this.cacheName, new Date(), key, value);
                }
            }
        }
    }
}
