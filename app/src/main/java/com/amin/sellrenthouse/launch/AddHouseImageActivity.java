package com.amin.sellrenthouse.launch;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.*;
import com.amin.sellrenthouse.R;
import com.amin.sellrenthouse.controller.HouseController;
import com.amin.sellrenthouse.entities.House;
import com.amin.sellrenthouse.entities.HouseImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class AddHouseImageActivity extends AppCompatActivity  {
    ImageButton addImageBtn;
    Button saveImagesBtn;
    ImageButton nextButton;
    ImageButton previousButton;
   private ImageView imageView;
   private House myHouse;
   private List<Uploader> imageUploaderList = new ArrayList<>();
   private List<HouseImage> houseImages;
   private ExecutorService executorService = Executors.newFixedThreadPool(2);
   private int imagePosition = -1;
   private  int imageCount;
   private ExecutorService uploadExecutor = Executors.newFixedThreadPool(2);
    private  static Exception exception ;

    public void nextImage(View view) {

        if (imagePosition < houseImages.size()-1){
            imagePosition++;
            Bitmap bitmap =  houseImages.get(imagePosition).getImage();
            imageView.setImageBitmap(bitmap);
        }

    }
    public void previousImage(View view){

        if (imagePosition > 0){
            Bitmap bitmap =  houseImages.get(--imagePosition).getImage();
            imageView.setImageBitmap(bitmap);
        }
    }

 private static class Uploader implements Runnable {
     private HouseImage houseImage;

     public Uploader(HouseImage houseImage) {
         this.houseImage = houseImage;
     }

     @Override
     public void run() {

         try {
             exception = HouseController.getInstance().addHouseImage(houseImage);

         } catch (ExecutionException | InterruptedException e) {
             if (exception == null){
                 exception = e;
             }else {
                 exception.addSuppressed(e);
             }

             }

         }

     }

    private  void showMessage(String msg) {
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
    }



    public void saveImages(View view){
        if (imageUploaderList.size() > 0) {
            for (int i = 0; i < imageUploaderList.size(); i++) {
                Uploader uploader = imageUploaderList.get(i);
                uploadExecutor.execute(uploader);

            }
            if (exception == null){
                showMessage("تصاویر با موفقیت ثبت شد.");
            }else {
                showMessage("خطا در ثبت!!");
                return;
                }
            imageUploaderList.clear();
        }else {
            Toast.makeText(this, "تصویر جدیدی برای ذخیره وجود ندارد!", Toast.LENGTH_SHORT).show();
        }
     }

    public void addImage(View view){

        if (imageCount < 2) {
            Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
            startActivityForResult(intent, 1);
        }else {
            Toast.makeText(this, "دو تصویر مجازید اضافه کنید!", Toast.LENGTH_LONG).show();
        }

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if( resultCode == RESULT_OK && data != null){
            Uri selectedImage = data.getData();
            Uploader uploader;
            try {
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), selectedImage);
                ByteArrayOutputStream stream = new ByteArrayOutputStream();

                bitmap = Bitmap.createScaledBitmap(bitmap, bitmap.getWidth() /4, bitmap.getHeight() / 4, true);
                bitmap.compress(Bitmap.CompressFormat.JPEG, 30, stream);
                ByteArrayInputStream inputStream = new ByteArrayInputStream(stream.toByteArray());
                bitmap = BitmapFactory.decodeStream(inputStream);
                HouseImage houseImage = new HouseImage();
                houseImage.setHouseId(myHouse.getId());
                houseImage.setImage(bitmap);
                uploader = new Uploader(houseImage);
                imageUploaderList.add(uploader);
                houseImages.add(houseImage);
                nextImage(null);
                imageCount++;

            }catch (Exception e){
                e.printStackTrace();
                Toast.makeText(this, "اشکال در اضافه کردن تصویر!!", Toast.LENGTH_LONG).show();

            }

        }

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_house_image);
        addImageBtn = findViewById(R.id.addImageButton);
        saveImagesBtn = findViewById(R.id.saveImagesButton);
        previousButton = findViewById(R.id.previousButton);
        nextButton = findViewById(R.id.nextButton);
        imageView = findViewById(R.id.imageView);
        imageUploaderList.clear();
        houseImages = new ArrayList<>();
        findViewById(R.id.progressBarAddImage).setVisibility(View.VISIBLE);
        addImageBtn.setEnabled(false);
      executorService.execute(new ImageDownloadTask());
	  executorService.shutdown();

        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                System.out.println("Size: " + houseImages.size() + " position: " + imagePosition);
                if (houseImages.size() > 0){
                    final HouseImage houseImage =  houseImages.get(imagePosition);
                    new AlertDialog.Builder(AddHouseImageActivity.this)
                            .setMessage("مایل به حذف تصویر انتخاب شده هستید؟")
                            .setPositiveButton("حذف تصویر انتخابی", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    houseImages.remove(imagePosition);
                                    imageView.setImageBitmap(null);
                                    if (imagePosition == houseImages.size()) {
                                        previousImage(null);
                                    }else if (imagePosition < houseImages.size()){
                                        imagePosition -= 1;
                                        nextImage(null);
                                    }

                                        if (houseImage.getId() >  0){
                                            try {
                                                Exception exception = HouseController.getInstance().removeHouseImage(houseImage);

                                                if (exception == null){
                                                    showMessage("تصویر حذف شد!");
                                                }else {
                                                    showMessage("خطا در شبکه!");
                                                }
                                            } catch (ExecutionException | InterruptedException  e) {
                                                e.printStackTrace();
                                                showMessage("خطا در شبکه!");
                                            }
                                        }else {
                                            for (int i = 0; i < imageUploaderList.size(); i++) {
                                                if (imageUploaderList.get(i).houseImage == houseImage)
                                                    imageUploaderList.remove(i);
                                            }
                                        }
                                        houseImages.remove(houseImage);
                                        imageCount--;

                                }
                            })
                            .setNegativeButton("لغو", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    return;
                                }
                            }).show();


                }

                }


        });

    }
    private class ImageDownloadTask implements Runnable{

        public ImageDownloadTask( ){

        }
        @Override
        public void run() {

                myHouse = getIntent().getParcelableExtra("house");
                try {
                    houseImages = HouseController.getInstance().getHouseImages(myHouse.getId());
                } catch (ExecutionException e) {
                    e.printStackTrace();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                imageCount = houseImages.size();

                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if (imagePosition == -1 ){
                            nextImage(null);
                        }
                        addImageBtn.setEnabled(true);
                        findViewById(R.id.progressBarAddImage).setVisibility(View.GONE);
                    }
                });




        }
    }
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK){
            this.finish();
        }
        return super.onKeyDown(keyCode, event);
    }
}
