package com.amin.sellrenthouse.dao;

import com.amin.sellrenthouse.entities.House;
import com.amin.sellrenthouse.entities.HouseImage;
import com.amin.sellrenthouse.utils.SqlHelper;
import java.util.List;
import java.util.concurrent.ExecutionException;


public class HouseDao {

   public HouseDao(){}

   public Exception addHouseImage(HouseImage houseImage) throws ExecutionException, InterruptedException {
       SqlHelper.HouseImageManipulating insertHouseImage = new SqlHelper.HouseImageManipulating();
       String procedure =  "{call insert_houseimage_procedure(?, ?)}";

       return insertHouseImage.execute(procedure, houseImage).get();
   }
    public Exception registerHouse(House house) throws ExecutionException, InterruptedException {

        SqlHelper.HouseManipulating insertHouse = new SqlHelper.HouseManipulating();
        String procedure =  "{call insert_house_procedure(?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)}";

        return insertHouse.execute(procedure, house).get();
    }

    public List<House> getMyHouses(long userId) throws ExecutionException, InterruptedException {
        SqlHelper.GetMyHouse getMyHouse = new SqlHelper.GetMyHouse();
        String procedure =  "{call select_house_by_ownerid_procedure(?)}";

        return getMyHouse.execute(procedure, userId).get();
    }

    public Exception editHouse(final House house) throws ExecutionException, InterruptedException {

        SqlHelper.HouseManipulating editHouse = new SqlHelper.HouseManipulating();
        String procedure =  "{call update_house_procedure(?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)}";

        return editHouse.execute(procedure, house).get();
    }
    public Exception report(final House house) throws ExecutionException, InterruptedException {

        SqlHelper.HouseReporting reportHouse = new SqlHelper.HouseReporting();
        String procedure =  "{call report_house_procedure(?, ?)}";

        return reportHouse.execute(procedure, house).get();
    }
    public Exception removeHouse(final House house) throws ExecutionException, InterruptedException {
        SqlHelper.HouseManipulating removeHouse = new SqlHelper.HouseManipulating();
        String procedure =  "{call delete_house_procedure(?)}";

        return removeHouse.execute(procedure, house).get();
    }


    public List<House> search(House house) throws ExecutionException, InterruptedException {
        SqlHelper.SearchHouse getHouses = new SqlHelper.SearchHouse();
        String procedure =  "{call select_houses_procedure(?, ?, ?, ?, ?, ?, ?, ?, ?)}";

        return getHouses.execute(procedure, house).get();
    }

    public List<HouseImage> getHouseImages(long houseId) throws ExecutionException, InterruptedException {
        SqlHelper.GetHouseImage getHouseImage = new SqlHelper.GetHouseImage();
        String procedure =  "{call select_houseimage_by_houseid_procedure(?)}";

        return getHouseImage.execute(procedure, houseId).get();

    }

    public Exception removeHouseImage(HouseImage houseImage) throws ExecutionException, InterruptedException {
        SqlHelper.HouseImageManipulating removeHouseimage = new SqlHelper.HouseImageManipulating();
        String procedure =  "{call delete_houseimage_procedure(?)}";

        return removeHouseimage.execute(procedure, houseImage).get();
    }
}
