package com.amin.sellrenthouse.entities;

import android.location.Location;
import android.os.Parcel;
import android.os.Parcelable;
import com.amin.sellrenthouse.constants.HouseType;
import com.amin.sellrenthouse.constants.SellRent;
import com.ibm.icu.util.Calendar;
import com.ibm.icu.util.ULocale;
import java.util.Date;
import java.util.concurrent.TimeUnit;


public class House implements Parcelable {
    private long id;
    private  String address;
    private  HouseType type;
    private  int floorNumber;
    private  double houseSize;
    private long price;
    private  String description;
    private  Location location;
    private long ownerId;
    private SellRent sellRent;
    private Calendar registerDate;
    private Province province;
    private County county;
    private City city;
    private int age;
    private long rentalCost;
    private boolean isReported;

    public SellRent getSellRent() {
        return sellRent;
    }

    public void setSellRent(SellRent sellRent) {
        this.sellRent = sellRent;
    }

    public House(){

    }


    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public double getHouseSize() {
        return houseSize;
    }

    public void setHouseSize(double houseSize) {
        this.houseSize = houseSize;
    }

    public HouseType getType() {
        return type;
    }

    public void setType(HouseType type) {
        this.type = type;
    }

    public int getFloorNumber() {
        return floorNumber;
    }

    public void setFloorNumber(int floorNumber) {
        this.floorNumber = floorNumber;
    }

    public long getPrice() {
        return price;
    }

    public void setPrice(long price) {
        this.price = price;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Location getLocation() {
        return location;
    }

    public void setLocation(Location location) {
        this.location = location;
    }

    public long getOwnerId() {
        return ownerId;
    }

    public void setOwnerId(long ownerId) {
        this.ownerId = ownerId;
    }

    public Calendar getRegisterDate() {
        return registerDate;
    }

    public void setRegisterDate(Calendar registerDate) {
        this.registerDate = registerDate;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public Province getProvince() {
        return province;
    }

    public void setProvince(Province province) {
        this.province = province;
    }

    public County getCounty() {
        return county;
    }

    public void setCounty(County county) {
        this.county = county;
    }

    public City getCity() {
        return city;
    }

    public void setCity(City city) {
        this.city = city;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public long getRentalCost() {
        return rentalCost;
    }

    public void setRentalCost(long rentalCost) {
        this.rentalCost = rentalCost;
    }

    public boolean isReported() {
        return isReported;
    }

    public void setReported(boolean reported) {
        isReported = reported;
    }

    @Override
    public String toString() {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("متراژ: ").append(String.format("%.0f", this.houseSize));
        if(this.type == HouseType.APARTMENT)
            stringBuilder.append("   ").append("طبقه: ").append( this.floorNumber);
        if (age > 0) {
            ULocale locale = new ULocale("fa_IR@calendar=persian");
            Date current = Calendar.getInstance(locale).getTime();
            Calendar passed = Calendar.getInstance(locale);
            passed.set(age, 1, 1);
            Date passedDate = passed.getTime();
            long passedYears = TimeUnit.DAYS.convert(current.getTime() - passedDate.getTime(), TimeUnit.MILLISECONDS) / 365;
            stringBuilder.append("   ").append("سن ملک: ").append(passedYears).append(" سال");
           /* DateFormat df = DateFormat.getDateInstance(DateFormat.DATE_FIELD, locale);
            stringBuilder.append("\t\t").append(" تاریخ: ").append(df.format(registerDate));*/
        }
        Date currentDate = Calendar.getInstance().getTime();
        long passedDays =  TimeUnit.DAYS.convert(currentDate.getTime() - registerDate.getTime().getTime(), TimeUnit.MILLISECONDS);
        if (passedDays == 0)     stringBuilder.append(" - امروز");
        else  stringBuilder.append("   ").append(passedDays).append(" روز پیش");

        if (sellRent == SellRent.RENT)
            stringBuilder.append("\n").append("پیش پرداخت: ").append( this.price);
            else
             stringBuilder.append("\n").append("قیمت: ").append( this.price);
        if (rentalCost > 0 && sellRent == SellRent.RENT)
            stringBuilder.append(" ").append("هزینه اجاره: ").append( this.rentalCost);

        stringBuilder.append("\n ").append("آدرس: ")
                .append(province).append(" - ").append(county).append(" - ").append(city);

        return stringBuilder.toString();
    }



    protected House(Parcel in) {
        id = in.readLong();
        address = in.readString();
        type = HouseType.values()[in.readInt()];
        floorNumber = in.readInt();
        houseSize = in.readDouble();
        price = in.readLong();
        description = in.readString();
        location = in.readParcelable(Location.class.getClassLoader());
        ownerId = in.readLong();
        sellRent = SellRent.values()[in.readInt()];
        registerDate = (Calendar) in.readSerializable();
        province = in.readParcelable(Province.class.getClassLoader());
        county = in.readParcelable(County.class.getClassLoader());
        city = in.readParcelable(City.class.getClassLoader());
        age = in.readInt();
        rentalCost = in.readLong();
        isReported = (boolean) in.readValue(boolean.class.getClassLoader());
    }


    public static final Creator<House> CREATOR = new Creator<House>() {
        @Override
        public House createFromParcel(Parcel in) {
            return new House(in);
        }

        @Override
        public House[] newArray(int size) {
            return new House[size];
        }
    };
    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeLong(id);
        dest.writeString(address);
        dest.writeInt(type.ordinal());
        dest.writeInt(floorNumber);
        dest.writeDouble(houseSize);
        dest.writeLong(price);
        dest.writeString(description);
        dest.writeParcelable(location, flags);
        dest.writeLong(ownerId);
        dest.writeInt(sellRent.ordinal());
        dest.writeSerializable(registerDate);
        dest.writeParcelable(province, flags);
        dest.writeParcelable(county, flags);
        dest.writeParcelable(city, flags);
        dest.writeInt(age);
        dest.writeLong(rentalCost);
        dest.writeValue(isReported);

    }

}
