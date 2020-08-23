package com.amin.sellrenthouse.constants;


import com.amin.sellrenthouse.R;

public enum HouseType   {
    APARTMENT ("آپارتمان", 1) { @Override  public int getImage() { return R.drawable.apartment;  } },
    VILLA ("ویلا", 2) { @Override  public int getImage() { return R.drawable.villa;  } },
    GARDEN ("باغ", 3) { @Override  public int getImage() { return R.drawable.garden; } },
    SHOP ("تجاری اداری", 4) { @Override  public int getImage() { return R.drawable.shop;  } },
    WAREHOUSE ("سوله", 5) { @Override  public int getImage() { return R.drawable.warehouse;  } },
    LAND ("زمین", 6) { @Override  public int getImage() { return R.drawable.land;  } };

    private final String name;
    private final int index;
    HouseType(String name, int index){
        this.name = name;
        this.index = index;
    }
    public abstract int getImage();

    public int getIndex() {
        return index;
    }

    public String getName() {
        return name;
    }
}
