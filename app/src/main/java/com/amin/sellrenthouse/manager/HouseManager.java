package com.amin.sellrenthouse.manager;

import android.location.Location;
import com.amin.sellrenthouse.constants.HouseType;
import com.amin.sellrenthouse.constants.SellRent;
import com.amin.sellrenthouse.dao.HouseDao;
import com.amin.sellrenthouse.entities.*;
import com.ibm.icu.util.Calendar;
import java.util.List;
import java.util.concurrent.ExecutionException;

public class HouseManager {
    private static HouseManager instance = new HouseManager();
    private HouseDao dao = new HouseDao();

    private HouseManager(){

    }
    public static HouseManager getInstance() {
        return instance;
    }

    public House createHouse(long id, String address, HouseType type, int floorNumber, double houseSize, long price,
                             String description, Location location, long ownerId, SellRent sellRent, Calendar registeredDate, Province province, County county,
                             City city, int age, long rentalCost){
        House house = new House();
        house.setId(id);
        house.setAddress(address);
        house.setType(type);
        house.setFloorNumber(floorNumber);
        house.setPrice(price);
        house.setDescription(description);
        house.setLocation(location);
        house.setOwnerId(ownerId);
        house.setHouseSize(houseSize);
        house.setSellRent(sellRent);
        house.setRegisterDate(registeredDate);
        house.setProvince(province);
        house.setCounty(county);
        house.setCity(city);
        house.setAge(age);
        house.setRentalCost(rentalCost);

        return house;

    }
    public Exception registerHouse(House house) throws ExecutionException, InterruptedException {
       return dao.registerHouse(house);
    }

    public Exception addHouseImage(HouseImage houseImage) throws ExecutionException, InterruptedException {
       return dao.addHouseImage(houseImage);
    }

    public List<House> getMyHouses(long userId) throws ExecutionException, InterruptedException {
        return dao.getMyHouses(userId);

    }

    public Exception editHouse(House house) throws ExecutionException, InterruptedException {
      return dao.editHouse(house);

    }

    public Exception removeHouse(House myHouse) throws ExecutionException, InterruptedException {
      return dao.removeHouse(myHouse);
    }


    public List<House>  search(House house) throws ExecutionException, InterruptedException {
       return dao.search(house);

    }

    public List<HouseImage> getHouseImages(long houseId) throws ExecutionException, InterruptedException {
        return dao.getHouseImages( houseId);
    }

    public Exception removeHouseImage(HouseImage houseImage) throws ExecutionException, InterruptedException {
       return dao.removeHouseImage(houseImage);
    }
}
