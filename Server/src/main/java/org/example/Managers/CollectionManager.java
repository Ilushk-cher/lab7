package org.example.Managers;

import org.example.CollectionModel.HumanBeing;
import org.example.CommandSpace.DatabaseHandler;
import org.example.Connection.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;
import java.util.logging.LogManager;

/**
 * Класс менеджера коллекции, хранящий ее саму и информацию о ней
 */
public class CollectionManager {
    private final Stack<HumanBeing> collection = new Stack<>();
    private LocalDateTime lastInitTime;
    private LocalDateTime lastSaveTime;

    private ReadWriteLock lock = new ReentrantReadWriteLock();
    Lock writeLock = lock.writeLock();
    Lock readLock = lock.readLock();

    static final Logger collectionManagerLogger = LoggerFactory.getLogger(CollectionManager.class);


    public CollectionManager() {
        this.lastInitTime = LocalDateTime.now();
        this.lastSaveTime = null;
        try {
            collection.addAll(DatabaseHandler.getDatabaseManager().loadCollection());
        } catch (NullPointerException e) {
            collectionManagerLogger.error("Какое-то из полей в базе данных равно null");
        }
    }

    public static String formatTime(LocalDateTime localDateTime) {
        if (localDateTime == null) return null;
        if (localDateTime.format(DateTimeFormatter.ofPattern("dd.MM.yyyy"))
                .equals(LocalDateTime.now().format(DateTimeFormatter.ofPattern("dd.MM.yyyy")))) {
            return localDateTime.format(DateTimeFormatter.ofPattern("HH:mm:ss"));
        }
        return localDateTime.format(DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm:ss"));
    }

    public static String formatTime(Date date) {
        LocalDateTime localDateTime = date.toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime();
        return formatTime(localDateTime);
    }

    public Stack<HumanBeing> getCollection() {
        try {
            readLock.lock();
            return collection;
        } finally {
            readLock.unlock();
        }
    }

    public String getLastInitTime() {
        try {
            readLock.lock();
            return formatTime(lastInitTime);
        } finally {
            readLock.unlock();
        }
    }

    public String getLastSaveTime() {
        try {
            readLock.lock();
            return formatTime(lastSaveTime);
        } finally {
            readLock.unlock();
        }
    }

    public String getCollectionType() {
        try {
            readLock.lock();
            return collection.getClass().getName();
        } finally {
            readLock.unlock();
        }
    }

    public int getSize() {
        try {
            readLock.lock();
            return collection.size();
        } finally {
            readLock.unlock();
        }
    }

    public void clear() {
        try {
            writeLock.lock();
            collection.clear();
            lastInitTime = LocalDateTime.now();
            collectionManagerLogger.info("Коллекция очищена");
        } finally {
            writeLock.unlock();
        }
    }

    public HumanBeing getLast() {
        try {
            readLock.lock();
            return collection.peek();
        } finally {
            readLock.unlock();
        }
    }

    public HumanBeing getById(int id) {
        try {
            readLock.lock();
            for (HumanBeing element : collection) {
                if (element.getId() == id) return element;
            }
            return null;
        } finally {
            readLock.unlock();
        }

    }

    public void addElement(HumanBeing humanBeing) {
        try {
            writeLock.lock();
            this.lastSaveTime = LocalDateTime.now();
            collection.push(humanBeing);
            collectionManagerLogger.info("Добавлен новый элемент в коллекцию");
        } finally {
            writeLock.unlock();
        }
    }

    public void addElement(Collection<HumanBeing> collection) {
        try {
            writeLock.lock();
            if (collection == null) return;
            for (HumanBeing humanBeing : collection) {
                addElement(humanBeing);
            }
        } finally {
            writeLock.unlock();
        }
    }

    public void removeLast() throws EmptyStackException {
        try {
            writeLock.lock();
            if (collection.isEmpty()) throw new EmptyStackException();
            collection.pop();
            this.lastSaveTime = LocalDateTime.now();
        } finally {
            writeLock.unlock();
        }
    }

    public void removeElement(HumanBeing humanBeing) {
        try {
            writeLock.lock();
            collection.remove(humanBeing);
            this.lastSaveTime = LocalDateTime.now();
        } finally {
            writeLock.unlock();
        }
    }

    public void removeElements(Collection<HumanBeing> collection) {
        try {
            writeLock.lock();
            this.collection.removeAll(collection);
            this.lastSaveTime = LocalDateTime.now();
        } finally {
            writeLock.unlock();
        }
    }

    public void removeElements(List<Long> deletedId) {
        try {
            writeLock.lock();
            deletedId.forEach(id -> collection.remove(getById(id)));
            this.lastSaveTime = LocalDateTime.now();
        } finally {
            writeLock.unlock();
        }
    }

    public boolean checkExistById(long id) {
        try {
            readLock.lock();
            return collection.stream().anyMatch(o -> o.getId() == id);
        } finally {
            readLock.unlock();
        }
    }

    public HumanBeing getById(long id) {
        try {
            readLock.lock();
            for (HumanBeing humanBeing : collection) {
                if (humanBeing.getId() == id) return humanBeing;
            }
            return null;
        } finally {
            readLock.unlock();
        }
    }

    public void editById(long id, HumanBeing newHumanBeing) {
        try {
            writeLock.lock();
            HumanBeing oldHumanBeing = getById(id);
            String userLogin = oldHumanBeing.getUserLogin();
            removeElement(oldHumanBeing);
            newHumanBeing.setId(id);
            newHumanBeing.setUserLogin(userLogin);
            addElement(newHumanBeing);
            this.lastSaveTime = LocalDateTime.now();
            collectionManagerLogger.info("Изменен объект с id=" + id + ": " + newHumanBeing);
        } finally {
            writeLock.unlock();
        }
    }

    public LocalDateTime getLastSaveTimeInDate() {
        try {
            readLock.lock();
            return lastSaveTime;
        } finally {
            readLock.unlock();
        }
    }

    public void setLastInitTime(LocalDateTime lastInitTime) {
        try {
            writeLock.lock();
            this.lastInitTime = lastInitTime;
        } finally {
            writeLock.unlock();
        }
    }

    public void setLastSaveTime(LocalDateTime lastSaveTime) {
        try {
            writeLock.lock();
            this.lastSaveTime = lastSaveTime;
        } finally {
            writeLock.unlock();
        }
    }

    @Override
    public String toString() {
        try {
            readLock.lock();
            if (collection.isEmpty()) return "Коллекция пуста";
            Long last = getLast().getId();

            StringBuilder collectionInfo = new StringBuilder();
            for (HumanBeing humanBeing : collection) {
                collectionInfo.append(humanBeing);
                if (!humanBeing.getId().equals(last)) collectionInfo.append("\n");
            }
            return collectionInfo.toString();
        } finally {
            readLock.unlock();
        }
    }
}
