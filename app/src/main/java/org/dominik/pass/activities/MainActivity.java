package org.dominik.pass.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager2.widget.ViewPager2;
import androidx.viewpager2.widget.ViewPager2.OnPageChangeCallback;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.google.android.material.button.MaterialButton;
import com.tbuonomo.viewpagerdotsindicator.WormDotsIndicator;

import org.dominik.pass.R;
import org.dominik.pass.adapters.OnboardAdapter;
import org.dominik.pass.models.OnboardItem;

import java.util.LinkedList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
  private OnboardAdapter onboardAdapter;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_main);

    ViewPager2 viewPager = findViewById(R.id.onboardViewPager);
    WormDotsIndicator indicator = findViewById(R.id.worm_dots_indicator);
    MaterialButton signinBtn = findViewById(R.id.signin_btn);
    MaterialButton signupBtn = findViewById(R.id.signup_btn);

    setOnboardItems();

    viewPager.setAdapter(onboardAdapter);
    indicator.setViewPager2(viewPager);

    viewPager.registerOnPageChangeCallback(new OnPageChangeCallback() {
      @Override
      public void onPageSelected(int position) {
        super.onPageSelected(position);
        if (position == 2) {
          signupBtn.setVisibility(View.VISIBLE);
          signinBtn.setVisibility(View.VISIBLE);
        } else {
          signupBtn.setVisibility(View.INVISIBLE);
          signinBtn.setVisibility(View.INVISIBLE);
        }
      }
    });

    signupBtn.setOnClickListener(v -> {
      Intent intent = new Intent(this, SignupActivity.class);
      startActivity(intent);
    });
  }

  private void setOnboardItems() {
    List<OnboardItem> items = new LinkedList<>();

    OnboardItem item1 = new OnboardItem(
      R.drawable.laptop_img,
      getResources().getString(R.string.onboard_title_first),
      getResources().getString(R.string.onboard_msg_first)
      );

    OnboardItem item2 = new OnboardItem(
      R.drawable.key_img,
      getResources().getString(R.string.onboard_title_second),
      getResources().getString(R.string.onboard_msg_second)
    );

    OnboardItem item3 = new OnboardItem(
      R.drawable.vault_img,
      getResources().getString(R.string.onboard_title_third),
      getResources().getString(R.string.onboard_msg_third)
    );

    items.add(item1);
    items.add(item2);
    items.add(item3);

    onboardAdapter = new OnboardAdapter(items);
  }
}