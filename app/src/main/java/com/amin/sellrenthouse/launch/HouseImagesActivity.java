package com.amin.sellrenthouse.launch;

import android.graphics.Bitmap;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import com.amin.sellrenthouse.R;
import com.amin.sellrenthouse.controller.HouseController;
import com.amin.sellrenthouse.entities.HouseImage;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class HouseImagesActivity extends AppCompatActivity {
    private int imagePosition = -1;
    private List<HouseImage> houseImageList;
    private ImageView imageView;
    ImageButton nextButton;
    ImageButton previousButton;
    private long houseId;

  private ExecutorService executorService = Executors.newFixedThreadPool(2);

    public void nextImage(View view) {
        if (imagePosition < houseImageList.size()-1){
            imagePosition++;
            Bitmap bitmap =  houseImageList.get(imagePosition).getImage();
            imageView.setImageBitmap(bitmap);
        }
    }
    public void previousImage(View view){
        if (imagePosition > 0){

            Bitmap bitmap =  houseImageList.get(--imagePosition).getImage();
            imageView.setImageBitmap(bitmap);
        }
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
       houseId = this.getIntent().getLongExtra("houseId", -1);
        setContentView(R.layout.activity_house_images);
        previousButton = findViewById(R.id.previousButton);
        nextButton = findViewById(R.id.nextButton);
        imageView = findViewById(R.id.imageView);
        houseImageList = new ArrayList<>();
       executorService.execute(new ImageDownloadTask());
       executorService.shutdown();

    }
    private class ImageDownloadTask implements Runnable{

      public ImageDownloadTask(){

      }
        @Override
        public void run() {

            try {
                houseImageList = HouseController.getInstance().getHouseImages(houseId);
            } catch (ExecutionException e) {
                e.printStackTrace();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
                runOnUiThread(()-> {
                    {
                        findViewById(R.id.progressBarHouseImages).setVisibility(View.GONE);
                        if (imagePosition == -1 ){
                            nextImage(null);
                        }
                        if (houseImageList.size() == 0){
                            imageView.setImageResource(R.drawable.no_image);
                        }
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
