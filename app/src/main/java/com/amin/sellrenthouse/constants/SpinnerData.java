package com.amin.sellrenthouse.constants;

import com.amin.sellrenthouse.R;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class SpinnerData {
   private final static SpinnerData instance = new SpinnerData();
   private static final String[] items = {"نوع", "آپارتمان", "ویلا", "باغ", "تجاری و اداری", "سوله", "زمین"};
   private static final List<String> spinnerItems = new  ArrayList<>(Arrays.asList(items));
   private static final int[] images = {android.R.drawable.alert_light_frame, R.drawable.apartment, R.drawable.villa, R.drawable.garden,
            R.drawable.shop, R.drawable.warehouse, R.drawable.land};

    private SpinnerData(){}
    public static SpinnerData getInstance(){

        return instance;
    }

    public List<String> getSpinnerItems() {
       /* spinnerItems.clear();
        spinnerItems.add("نوع");
        spinnerItems.add( "آپارتمان");
        spinnerItems.add( "ویلا");
        spinnerItems.add( "باغ");
        spinnerItems.add( "فروشگاه");
        spinnerItems.add( "سوله");
        spinnerItems.add( "زمین");*/
        return spinnerItems;
    }

    public  int[] getImages() {
        return images;
    }

   }
