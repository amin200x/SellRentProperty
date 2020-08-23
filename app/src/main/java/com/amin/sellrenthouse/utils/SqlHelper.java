package com.amin.sellrenthouse.utils;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.location.Location;
import android.os.AsyncTask;
import com.amin.sellrenthouse.constants.DbString;
import com.amin.sellrenthouse.constants.HouseType;
import com.amin.sellrenthouse.constants.SellRent;
import com.amin.sellrenthouse.entities.*;
import com.ibm.icu.util.Calendar;

import java.io.*;
import java.sql.*;
import java.util.*;

public class SqlHelper {
    private static final String JDBC_DRIVER = "com.mysql.jdbc.Driver";
    private static final String DATABASE_URL = "jdbc:mysql://"
                        + DbString.DB_URL + "/"
                        + DbString.DB_NAME + "?useInformationSchema=true&useSSL=false&useUnicode=yes&characterEncoding=UTF-8";  // ?useInformationSchema=true, ,

        public static class UserManipulating extends AsyncTask<Object, String, Exception>{

            @Override
            protected Exception doInBackground(Object... querys) {
                Exception exception = null;

                try {
                    Class.forName(JDBC_DRIVER);
                } catch (ClassNotFoundException e) {
                    exception = e;
                }
                String query = querys[0].toString();
                User user = (User) querys[1];
                ;
                try (Connection connection = DriverManager.getConnection(DATABASE_URL, DbString.DB_USER_NAME, DbString.DB_PASSWORD);
                    CallableStatement pstm = connection.prepareCall(query)){

                    if(query.contains("insert")) {
                        pstm.setString("p_username", user.getUserName());
                        pstm.setString("p_password", user.getPassword());
                        pstm.setString("p_firstname", user.getFirstName());
                        pstm.setString("p_lastname", user.getLastName());
                        pstm.setString("p_contactnumber", user.getContactNumber());
                    }else if (query.contains("update")){
                        pstm.setLong("p_id", user.getId());
                        pstm.setString("p_password", user.getPassword());
                        pstm.setString("p_firstname", user.getFirstName());
                        pstm.setString("p_lastname", user.getLastName());
                        pstm.setString("p_contactnumber", user.getContactNumber());
                    }else {
                        pstm.setLong("p_id", user.getId());

                    }
                    pstm.execute();

                } catch (SQLException e) {
                    e.printStackTrace();
                    exception = e;
                }

                return exception;
            }
        }
        public static class GetUser extends AsyncTask<Object, String, List<User>>{

            @Override
            protected List<User> doInBackground(Object... querys) {

                try {
                    Class.forName(JDBC_DRIVER);
                } catch (ClassNotFoundException e) {

                }
                List<User> userList = new ArrayList<>();
                String query = querys[0].toString();
                User objUser = (User) querys[1];

                try(Connection connection = DriverManager.getConnection(DATABASE_URL, DbString.DB_USER_NAME, DbString.DB_PASSWORD);
                    CallableStatement pstm = connection.prepareCall(query)) {
                    if (query.contains("username")) {
                        pstm.setString("p_username", objUser.getUserName());
                        pstm.setString("p_password", objUser.getPassword());
                    }else {
                       pstm.setLong("p_id", objUser.getId());

                    }
                    ResultSet rs = pstm.executeQuery();

                    User user;
                    while (rs.next()){
                        user = new com.amin.sellrenthouse.entities.User();
                        user.setUserName(rs.getString("user_name"));
                        user.setPassword(rs.getString("password"));
                        user.setFirstName(rs.getString("first_name"));
                        user.setLastName(rs.getString("last_name"));
                        user.setContactNumber(rs.getString("contact_number"));
                        user.setId(rs.getLong("id"));
                        userList.add(user);
                    }

                } catch (SQLException e) {
                    e.printStackTrace();

                }

                return userList;
            }
        }
        public static class HouseManipulating extends AsyncTask<Object, String, Exception>{

            @Override
            protected Exception doInBackground(Object... querys) {
                Exception exception = null;

                try {
                    Class.forName(JDBC_DRIVER);
                } catch (ClassNotFoundException e) {
                    exception = e;
                }
                String query = querys[0].toString();
                House house = (House) querys[1];
                try(Connection connection = DriverManager.getConnection(DATABASE_URL, DbString.DB_USER_NAME, DbString.DB_PASSWORD);
                    CallableStatement pstm = connection.prepareCall(query)) {
                    if(query.contains("insert")) {
                        pstm.setInt("p_type", house.getType().ordinal());
                        pstm.setInt("p_sellrent", house.getSellRent().ordinal());
                        pstm.setDouble("p_size", house.getHouseSize());
                        pstm.setInt("p_age", house.getAge());
                        pstm.setInt("p_floor", house.getFloorNumber());
                        pstm.setLong("p_price", house.getPrice());
                        pstm.setLong("p_rentalcost", house.getRentalCost());
                        pstm.setDouble("p_latitude", house.getLocation().getLatitude());
                        pstm.setDouble("p_longitude", house.getLocation().getLongitude());
                        pstm.setInt("p_provinceid", house.getProvince().getId());
                        pstm.setInt("p_countyid", house.getCounty().getId());
                        pstm.setInt("p_cityid", house.getCity().getId());
                        pstm.setString("p_address", house.getAddress());
                        pstm.setString("p_description", house.getDescription());
                        pstm.setLong("p_ownerid", house.getOwnerId());

                    }else if (query.contains("update")){
                        pstm.setLong("p_id", house.getId());
                        pstm.setInt("p_type", house.getType().ordinal());
                        pstm.setInt("p_sellrent", house.getSellRent().ordinal());
                        pstm.setDouble("p_size", house.getHouseSize());
                        pstm.setInt("p_age", house.getAge());
                        pstm.setInt("p_floor", house.getFloorNumber());
                        pstm.setLong("p_price", house.getPrice());
                        pstm.setLong("p_rentalcost", house.getRentalCost());
                        pstm.setDouble("p_latitude", house.getLocation().getLatitude());
                        pstm.setDouble("p_longitude", house.getLocation().getLongitude());
                        pstm.setInt("p_provinceid", house.getProvince().getId());
                        pstm.setInt("p_countyid", house.getCounty().getId());
                        pstm.setInt("p_cityid", house.getCity().getId());
                        pstm.setString("p_address", house.getAddress());
                        pstm.setString("p_description", house.getDescription());
                        pstm.setLong("p_ownerid", house.getOwnerId());
                    }else if (query.contains("delete")){
                        pstm.setLong("p_id", house.getId());
                    }
                    pstm.execute();

                } catch (SQLException e) {
                    e.printStackTrace();
                    exception = e;
                }

                return exception;
            }
        }
    public static class HouseReporting extends AsyncTask<Object, String, Exception>{

        @Override
        protected Exception doInBackground(Object... querys) {
            Exception exception = null;

            try {
                Class.forName(JDBC_DRIVER);
            } catch (ClassNotFoundException e) {
                exception = e;
            }
            String query = querys[0].toString();
            House house = (House) querys[1];
            try(Connection connection = DriverManager.getConnection(DATABASE_URL, DbString.DB_USER_NAME, DbString.DB_PASSWORD);
                CallableStatement pstm = connection.prepareCall(query)) {
                    pstm.setLong("p_id", house.getId());
                    pstm.setBoolean("isReported", house.isReported());


                pstm.execute();

            } catch (SQLException e) {
                e.printStackTrace();
                exception = e;
            }

            return exception;
        }
    }

    public static class GetMyHouse extends AsyncTask<Object, String, List<House>>{

            @Override
            protected List<House> doInBackground(Object... querys) {
                House house;
                List<House> houseList = new ArrayList<>();
                try {
                    Class.forName(JDBC_DRIVER);
                } catch (ClassNotFoundException e) {
                    e.printStackTrace();
                }
                String query = querys[0].toString();
               long ownerId = Long.valueOf(querys[1].toString());

                try(Connection connection = DriverManager.getConnection(DATABASE_URL, DbString.DB_USER_NAME, DbString.DB_PASSWORD);
                    CallableStatement pstm = connection.prepareCall(query)) {
                      pstm.setLong("p_ownerid",ownerId);

                    ResultSet rs =  pstm.executeQuery();
                   while (rs.next()){
                       house = new House();
                       house.setId(rs.getLong("id"));
                       house.setAge(rs.getInt("age"));
                       house.setPrice(rs.getLong("price"));
                       house.setHouseSize(rs.getInt("size"));
                       house.setDescription(rs.getString("description"));
                       house.setAddress(rs.getString("address"));
                       house.setFloorNumber(rs.getInt("floor"));
                       house.setType(HouseType.values()[rs.getInt("type")]);
                       house.setSellRent(SellRent.values()[rs.getInt("sell_rent")]);
                       house.setRentalCost(rs.getLong("rental_cost"));
                       house.setProvince(new Province(rs.getInt("province_id"), rs.getString("province_name")));
                       house.setCounty(new County(rs.getInt("county_id"), rs.getString("county_name")));
                       house.setCity(new City(rs.getInt("city_id"), rs.getString("city_name")));
                       Location location = new Location("hLoction");
                       location.setLatitude(rs.getDouble("latitude"));
                       location.setLongitude(rs.getDouble("longitude"));
                       house.setLocation(location);
                       house.setOwnerId(ownerId);
                       Calendar calendar = Calendar.getInstance();
                       calendar.setTime(rs.getDate("created_date"));
                       house.setRegisterDate(calendar);
                       house.setReported(rs.getBoolean("isReported"));
                       houseList.add(house);
                   }

                } catch (SQLException e) {
                    e.printStackTrace();


                }

                return houseList ;
            }
        }
        public static class GetProvinceCountyCity extends AsyncTask<Object, String, Map<String, Integer>> {
            Map<String, Integer> map = null;
            @Override
            protected Map<String, Integer> doInBackground(Object... querys) {
                map = new HashMap<>();
                try {
                    Class.forName(JDBC_DRIVER);
                } catch (ClassNotFoundException e) {
                    e.printStackTrace();
                }
                String query = querys[0].toString();
                try(Connection connection = DriverManager.getConnection(DATABASE_URL, DbString.DB_USER_NAME, DbString.DB_PASSWORD);
                    CallableStatement pstm = connection.prepareCall(query)) {
                    if(query.contains("county") || query.contains("city")) {
                        int id = Integer.valueOf(querys[1].toString());
                        pstm.setInt(1 , id);
                    }
                    ResultSet rs = pstm.executeQuery();
                    while (rs.next()){
                        map.put(rs.getString("name"), rs.getInt("id"));
                    }
                } catch (SQLException e) {
                    e.printStackTrace();
                }

                return map;
            }
        }
        public static class HouseImageManipulating extends AsyncTask<Object, String, Exception>{

        @Override
        protected Exception doInBackground(Object... querys) {
            Exception exception = null;

            try {
                Class.forName(JDBC_DRIVER);
            } catch (ClassNotFoundException e) {
                exception = e;
            }
            String query = querys[0].toString();
            HouseImage houseImage = (HouseImage) querys[1];
            try(Connection connection = DriverManager.getConnection(DATABASE_URL, DbString.DB_USER_NAME, DbString.DB_PASSWORD);
                CallableStatement pstm = connection.prepareCall(query)) {
                if(query.contains("insert")) {
                    pstm.setLong("p_houseid", houseImage.getHouseId());
                    ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
                    houseImage.getImage().compress(Bitmap.CompressFormat.JPEG, 100, outputStream);
                    ByteArrayInputStream inputStream = new ByteArrayInputStream(outputStream.toByteArray());
                    pstm.setBinaryStream("p_image", inputStream);

                }else if (query.contains("delete")){
                    pstm.setLong("p_id", houseImage.getId());

                }
                pstm.execute();

            } catch (SQLException e) {
                e.printStackTrace();
                exception = e;
            }

            return exception;
        }
    }
        public static class GetHouseImage extends AsyncTask<Object, String, List<HouseImage>>{

        @Override
        protected List<HouseImage> doInBackground(Object... querys) {
            String query = querys[0].toString();
            long houseId =  Long.valueOf(querys[1].toString());
            List<HouseImage> houseImageList = new ArrayList<>();


            try {
                Class.forName(JDBC_DRIVER);
            } catch (ClassNotFoundException e) {

            }

            try(Connection connection = DriverManager.getConnection(DATABASE_URL, DbString.DB_USER_NAME, DbString.DB_PASSWORD);
                CallableStatement pstm = connection.prepareCall(query)) {
                pstm.setLong("p_houseid", houseId);
                ResultSet rs = pstm.executeQuery();
                HouseImage  houseImage;
                while (rs.next()){
                    houseImage = new HouseImage();
                    houseImage.setId(rs.getLong("id"));
                    houseImage.setImage(BitmapFactory.decodeStream(rs.getBinaryStream("image")));
                    houseImage.setHouseId(houseId);
                    houseImageList.add(houseImage);
                }

            } catch (SQLException e) {
                e.printStackTrace();
            }

            return houseImageList;
        }
    }
        public static class SearchHouse extends AsyncTask<Object, String, List<House>>{

        @Override
        protected List<House> doInBackground(Object... querys) {
            House house;
            List<House> houseList = new ArrayList<>(Collections.emptyList());
            try {
                Class.forName(JDBC_DRIVER);
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }


            String query = querys[0].toString();
            House objHouse = (House) querys[1];
            try(Connection connection = DriverManager.getConnection(DATABASE_URL, DbString.DB_USER_NAME, DbString.DB_PASSWORD);
                CallableStatement pstm = connection.prepareCall(query)) {
                pstm.setInt("p_provinceid", objHouse.getProvince().getId());
                pstm.setInt("p_countyid",objHouse.getCounty().getId());
                pstm.setInt("p_cityid",objHouse.getCity().getId());
               if (objHouse.getType() != null)
                    pstm.setInt("p_type",objHouse.getType().ordinal());
               else
                   pstm.setInt("p_type",-1);
               if (objHouse.getSellRent() != null)
                     pstm.setInt("p_sellrent",objHouse.getSellRent().ordinal());
               else
                   pstm.setInt("p_sellrent", -1);
                pstm.setDouble("p_size",objHouse.getHouseSize());
                pstm.setInt("p_age",objHouse.getAge());
                pstm.setLong("p_price",objHouse.getPrice());
                pstm.setLong("p_rentalcost",objHouse.getRentalCost());

                ResultSet rs =  pstm.executeQuery();
                while (rs.next()){
                    house = new House();
                    house.setId(rs.getLong("id"));
                    house.setAge(rs.getInt("age"));
                    house.setPrice(rs.getLong("price"));
                    house.setHouseSize(rs.getInt("size"));
                    house.setDescription(rs.getString("description"));
                    house.setAddress(rs.getString("address"));
                    house.setFloorNumber(rs.getInt("floor"));
                    house.setType(HouseType.values()[rs.getInt("type")]);
                    house.setSellRent(SellRent.values()[rs.getInt("sell_rent")]);
                    house.setRentalCost(rs.getLong("rental_cost"));
                    house.setProvince(new Province(rs.getInt("province_id"), rs.getString("province_name")));
                    house.setCounty(new County(rs.getInt("county_id"), rs.getString("county_name")));
                    house.setCity(new City(rs.getInt("city_id"), rs.getString("city_name")));
                    Location location = new Location("hLoction");
                    location.setLatitude(rs.getDouble("latitude"));
                    location.setLongitude(rs.getDouble("longitude"));
                    house.setLocation(location);
                    house.setOwnerId(rs.getLong("owner_id"));
                    Calendar calendar = Calendar.getInstance();
                    calendar.setTime(rs.getDate("created_date"));
                    house.setRegisterDate(calendar);
                    houseList.add(house);
                }

            } catch (SQLException e) {
                e.printStackTrace();

            }

            return houseList ;
        }
    }

}
