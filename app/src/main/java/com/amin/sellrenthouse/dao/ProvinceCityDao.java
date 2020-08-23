package com.amin.sellrenthouse.dao;

import com.amin.sellrenthouse.utils.SqlHelper;

import java.util.Map;
import java.util.concurrent.ExecutionException;

public class ProvinceCityDao {
    public static Map<String, Integer> getProvinces() throws ExecutionException, InterruptedException {
        String query = "{call select_all_province_procedure()}";
        SqlHelper.GetProvinceCountyCity getProvince = new SqlHelper.GetProvinceCountyCity();
        return getProvince.execute(query).get();
    }

    public static Map<String, Integer> getCounties(int id) throws ExecutionException, InterruptedException {
        String query = "{call select_county_by_provinceid_procedure(?)}";
        SqlHelper.GetProvinceCountyCity getProvince = new SqlHelper.GetProvinceCountyCity();
        return getProvince.execute(query, id).get();
    }

    public static Map<String, Integer> getCities(int id) throws ExecutionException, InterruptedException {
        String query = "{call select_city_by_countyid_procedure(?)}";
        SqlHelper.GetProvinceCountyCity getProvince = new SqlHelper.GetProvinceCountyCity();
        return getProvince.execute(query, id).get();
    }
}
