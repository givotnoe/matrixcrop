package tech.stoptalking.matrixcrop;

import android.graphics.BitmapFactory;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import tech.stoptalking.crop.CropImageView;
import tech.stoptalking.crop.config.ViewConfig;
import tech.stoptalking.crop.drawable.bg.CropCenterBGDrawable;
import tech.stoptalking.crop.drawable.fg.CircleFGDrawable;
import tech.stoptalking.crop.restriction.AroundRestriction;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        CropImageView img = findViewById(R.id.img);

        ViewConfig config =
                ViewConfig
                        .builder()
                        .background(new CropCenterBGDrawable(BitmapFactory.decodeResource(getResources(), R.drawable.profile_background_image)))
                        .moveMode(ViewConfig.BG_MOV_FG_STILL_MODE)
                        .boundMode(ViewConfig.BOUND_FG_MODE)
                        .foreground(new CircleFGDrawable(ContextCompat.getColor(this, R.color.overlay_bg)))
                        .restriction(new AroundRestriction())
                        .build();

        img.setConfig(config);
    }
}
