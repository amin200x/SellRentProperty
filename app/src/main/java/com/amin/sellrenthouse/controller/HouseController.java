package com.amin.sellrenthouse.controller;

import com.amin.sellrenthouse.entities.House;
import com.amin.sellrenthouse.entities.HouseImage;
import com.amin.sellrenthouse.manager.HouseManager;
import java.util.List;
import java.util.concurrent.ExecutionException;

public class HouseController {
    private static HouseController instance = new HouseController();

    private HouseController(){

    }
    public static HouseController getInstance() {
        return instance;
    }

    public Exception registerHouse(House house) throws ExecutionException, InterruptedException {
       return HouseManager.getInstance().registerHouse(house);
    }
    public Exception addHouseImage(HouseImage houseImage) throws ExecutionException, InterruptedException {
       return HouseManager.getInstance().addHouseImage(houseImage);
    }

    public List<House> getMyHouses(long userId) throws ExecutionException, InterruptedException {
       return HouseManager.getInstance().getMyHouses(userId);
    }

    public Exception editHouse(House house) throws ExecutionException, InterruptedException {
      return   HouseManager.getInstance().editHouse(house);
    }

    public Exception removeHouse(House myHouse) throws ExecutionException, InterruptedException {
       return HouseManager.getInstance().removeHouse(myHouse);
    }

    public List<House>  search(House house) throws ExecutionException, InterruptedException {
        return HouseManager.getInstance().search(house);
    }

    public List<HouseImage> getHouseImages(long houseId) throws ExecutionException, InterruptedException {
        return HouseManager.getInstance().getHouseImages( houseId);
    }

    public Exception removeHouseImage(HouseImage houseImage) throws ExecutionException, InterruptedException {
       return HouseManager.getInstance().removeHouseImage(houseImage);
    }
}
