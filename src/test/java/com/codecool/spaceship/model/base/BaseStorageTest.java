package com.codecool.spaceship.model.base;

import com.codecool.spaceship.model.Resource;
import com.codecool.spaceship.model.base.BaseStorage;
import com.codecool.spaceship.model.exception.StorageException;
import com.codecool.spaceship.model.exception.UpgradeNotAvailableException;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class BaseStorageTest {

    @Test
    void getCurrentCapacityTestLevel1() {
        BaseStorage baseStorage = new BaseStorage();
        int expected = 20;

        assertEquals(expected, baseStorage.getCurrentCapacity());
    }

    @Test
    void getCurrentCapacityTestLevel3() {
        BaseStorage baseStorage = new BaseStorage();
        baseStorage.upgrade();
        baseStorage.upgrade();
        int expected = 100;
        assertEquals(expected, baseStorage.getCurrentCapacity());
    }

    @Test
    void addResourceSuccessOne() {
        BaseStorage baseStorage = new BaseStorage();
        try {
            baseStorage.addResource(Resource.METAL, 5);
        } catch (StorageException ignored) {
        }

        Map<Resource, Integer> expected = new HashMap<>() {{
            put(Resource.METAL, 5);
        }};
        assertEquals(expected, baseStorage.getStoredItems());
    }

    @Test
    void addResourceSuccessMultiple() {
        BaseStorage baseStorage = new BaseStorage();
        try {
            baseStorage.addResource(Resource.METAL, 5);
            baseStorage.addResource(Resource.METAL, 10);
            baseStorage.addResource(Resource.SILICONE, 2);
        } catch (StorageException ignored) {
        }

        Map<Resource, Integer> expected = new HashMap<>() {{
            put(Resource.METAL, 15);
            put(Resource.SILICONE, 2);
        }};
        assertEquals(expected, baseStorage.getStoredItems());
    }

    @Test
    void addResourceError() {
        BaseStorage baseStorage = new BaseStorage();
        assertThrows(StorageException.class, () -> baseStorage.addResource(Resource.METAL, 25));
    }

    @Test
    void hasResourceTrue() {
        BaseStorage baseStorage = new BaseStorage();
        try {
            baseStorage.addResource(Resource.METAL, 5);
        } catch (StorageException ignored) {
        }
        assertTrue(baseStorage.hasResource(Resource.METAL, 5));
    }

    @Test
    void hasResourceFalse() {
        BaseStorage baseStorage = new BaseStorage();
        assertFalse(baseStorage.hasResource(Resource.METAL, 5));
    }

    @Test
    void removeResourceSuccess() {
        BaseStorage baseStorage = new BaseStorage();
        try {
            baseStorage.addResource(Resource.METAL, 5);
            baseStorage.removeResource(Resource.METAL, 4);
        } catch (StorageException ignored) {
        }
        Map<Resource, Integer> expected = new HashMap<>() {{
            put(Resource.METAL, 1);
        }};
        assertEquals(expected, baseStorage.getStoredItems());
    }

    @Test
    void removeResourceError() {
        BaseStorage baseStorage = new BaseStorage();
        assertThrows(StorageException.class, () -> baseStorage.removeResource(Resource.METAL, 1));
    }

    @Test
    void getUpgradeCostSuccess() {
        BaseStorage baseStorage = new BaseStorage();
        Map<Resource, Integer> actual = null;
        try {
            actual = baseStorage.getUpgradeCost();
        } catch (UpgradeNotAvailableException ignored) {
        }

        Map<Resource, Integer> expected = new HashMap<>() {{
            put(Resource.METAL, 5);
        }};
        assertEquals(expected, actual);
    }

    @Test
    void getUpgradeCostError() {
        BaseStorage baseStorage = new BaseStorage();
        baseStorage.upgrade();
        baseStorage.upgrade();
        baseStorage.upgrade();
        baseStorage.upgrade();
        assertThrows(UpgradeNotAvailableException.class, baseStorage::getUpgradeCost);
    }

    @Test
    void upgradeSuccess() {
        BaseStorage baseStorage = new BaseStorage();
        baseStorage.upgrade();
        int expected = 2;
        assertEquals(expected,baseStorage.getCurrentLevel());
    }

    @Test
    void upgradeMaxLevelCannotBeExceeded() {
        BaseStorage baseStorage = new BaseStorage();
        baseStorage.upgrade();
        baseStorage.upgrade();
        baseStorage.upgrade();
        baseStorage.upgrade();
        baseStorage.upgrade();
        baseStorage.upgrade();
        baseStorage.upgrade();
        baseStorage.upgrade();
        int expected = 5;
        assertEquals(expected,baseStorage.getCurrentLevel());
    }
}
