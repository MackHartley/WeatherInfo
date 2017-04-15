package hu.ait.recylerviewdemo;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.Menu;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;

/**
 * Created by Mack on 4/11/2017.
 */

public class Splash extends Activity {
    /*Used tutorial from :https://www.youtube.com/watch?v=XwOuTjUFexE*/

    @Override
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        setContentView(R.layout.splash_screen);

        final ImageView imageView = (ImageView) findViewById(R.id.splashImage);
        final Animation an2 = AnimationUtils.loadAnimation(getBaseContext(), R.anim.cart_move);
        final Animation an1 = AnimationUtils.loadAnimation(getBaseContext(), R.anim.cart_fade_in);

        imageView.startAnimation(an1);
        an1.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                imageView.startAnimation(an2);

            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
        an2.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                finish();
                Intent i = new Intent(Splash.this, MainActivity.class);
                startActivity(i);

            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
    }
}
