package com.amin.sellrenthouse.manager;

import com.amin.sellrenthouse.dao.ProvinceCityDao;

import java.util.Map;
import java.util.concurrent.ExecutionException;

public class ProvinceCityManager {
    public static Map<String, Integer> getProvinces() throws ExecutionException, InterruptedException {
        return ProvinceCityDao.getProvinces();
    }

    public static Map<String, Integer> getCounties(int id) throws ExecutionException, InterruptedException {
        return ProvinceCityDao.getCounties(id);
    }

    public static Map<String, Integer> getCities(int id) throws ExecutionException, InterruptedException {
        return ProvinceCityDao.getCities(id);
    }
}
