package me.goodt.vkpht.common.application.util;

import lombok.extern.slf4j.Slf4j;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Параллельный хэш-мап, с ограничением времени жизни элементов
 * */
@Slf4j
public class ExpiredConcurrentHashMap<K, V> extends ConcurrentHashMap<K, V> {

    /** Хранение точки времени поступления в map для каждого элемента */
    private final Map<K, Long> timeMap = new ConcurrentHashMap<>();

    /** Период, в течение которого запись будет считаться "живой" */
    private final long expiryInMillis;

    /** Периодичность срабатывания потока, очищающего map */
    private final long cleanerInMillis;

    /** Формат даты */
    private static final SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss:SSS");

    /** Название кэша, для вывода в логах */
    private final String cacheName;

    /**
     * @param cacheName Название сервиса, используемое в логах
     * @param expiryInMillis Кол-во миллисекунд, через которое объект будет удалён
     * @param cleanerInMillis Промежуток времени, через который будет запускаться cleaner для очистки устаревших значений
     * */
    public ExpiredConcurrentHashMap(String cacheName, long expiryInMillis, long cleanerInMillis) {
        this.cacheName = cacheName;
        this.expiryInMillis = expiryInMillis;
        this.cleanerInMillis = cleanerInMillis;
        log.info("Initialize cache for \"{}\" with expire time \"{}\" sec and cleaner time \"{}\" sec", cacheName, expiryInMillis / 1000, cleanerInMillis / 1000);
        initialize();
    }

    private void initialize() {
        new CleanerThread().start();
    }

    @Override
    public V put(K key, V value) {
        Date date = new Date();
        timeMap.put(key, date.getTime());
        log.info("Inserting element to \"{}\": " + sdf.format(date) + " : " + key + " : " + value, ExpiredConcurrentHashMap.this.cacheName);
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
        if (!containsKey(key))
            return put(key, value);
        else
            return get(key);
    }

    /**
     * Класс-cleaner, удаляющий элементы map с просроченным сроком годности
     * */
    private class CleanerThread extends Thread {
        @Override
        public void run() {
            log.info("Initiating Cleaner Thread for \"{}\"", ExpiredConcurrentHashMap.this.cacheName);
            while (true) {
                cleanMap();
                try {
                    Thread.sleep(cleanerInMillis);
                } catch (InterruptedException ex) {
                    log.error(ex.getMessage());
                }
            }
        }

        /**
         * Метод очищает записи с истекшим сроком годности
         * */
        private void cleanMap() {
            long currentTime = new Date().getTime();
            for (K key : timeMap.keySet()) {
                if (currentTime > (timeMap.get(key) + expiryInMillis)) {
                    V value = remove(key);
                    timeMap.remove(key);
                    log.info("Removing expired element from \"{}\": " + sdf.format(new Date()) + " : " + key + " : " + value, ExpiredConcurrentHashMap.this.cacheName);
                }
            }
        }
    }
}
