package com.amin.sellrenthouse.controller;

import com.amin.sellrenthouse.manager.ProvinceCityManager;

import java.util.Map;
import java.util.concurrent.ExecutionException;

public class ProvinceCityController {
    public static Map<String, Integer> getProvinces() throws ExecutionException, InterruptedException {
        return ProvinceCityManager.getProvinces();
    }

    public static Map<String, Integer> getCounties(int id) throws ExecutionException, InterruptedException {
        return ProvinceCityManager.getCounties(id);
    }

    public static Map<String, Integer> getCities(int id) throws ExecutionException, InterruptedException {
        return ProvinceCityManager.getCities(id);
    }
}
