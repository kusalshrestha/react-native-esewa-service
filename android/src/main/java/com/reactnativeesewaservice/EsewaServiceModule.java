package com.reactnativeesewaservice;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import androidx.annotation.NonNull;

import com.esewa.android.sdk.payment.ESewaConfiguration;
import com.esewa.android.sdk.payment.ESewaPayment;
import com.esewa.android.sdk.payment.ESewaPaymentActivity;
import com.facebook.react.bridge.ActivityEventListener;
import com.facebook.react.bridge.Arguments;
import com.facebook.react.bridge.BaseActivityEventListener;
import com.facebook.react.bridge.Promise;
import com.facebook.react.bridge.ReactApplicationContext;
import com.facebook.react.bridge.ReactContextBaseJavaModule;
import com.facebook.react.bridge.ReactMethod;
import com.facebook.react.bridge.WritableMap;
import com.facebook.react.module.annotations.ReactModule;

@ReactModule(name = EsewaServiceModule.NAME)
public class EsewaServiceModule extends ReactContextBaseJavaModule {
  public static final String NAME = "Esewa";
  private final ReactApplicationContext reactContext;
  private static final int REQUEST_CODE_PAYMENT = 1;
  private ESewaConfiguration eSewaConfiguration;
  private Promise mEsewaPromise;

  @Override
  @NonNull
  public String getName() {
        return NAME;
    }

  public EsewaServiceModule(ReactApplicationContext reactContext) {
    super(reactContext);
    this.reactContext = reactContext;
    reactContext.addActivityEventListener(mActivityEventListener);
  }

  @ReactMethod
  public void init(String CLIENT_ID, String SECRET_KEY, String ENVIRONMENT) {
    Log.d("Merchant Id", CLIENT_ID);
    Log.d("Merchant key", SECRET_KEY);
    eSewaConfiguration = new ESewaConfiguration()
      .clientId(CLIENT_ID)
      .secretKey(SECRET_KEY)
      .environment(ENVIRONMENT);
  }


  @ReactMethod
  public void makePayment(String amount, String productName, String productID, String callbackURL) {
    Activity currentActivity = getCurrentActivity();

    if (currentActivity == null) {
      return;
    }

    ESewaPayment eSewaPayment = new ESewaPayment(amount, productName, productID, callbackURL);
    Intent intent = new Intent(currentActivity.getBaseContext(), ESewaPaymentActivity.class);
    intent.putExtra(ESewaConfiguration.ESEWA_CONFIGURATION, eSewaConfiguration);
    intent.putExtra(ESewaPayment.ESEWA_PAYMENT, eSewaPayment);
    currentActivity.startActivityForResult(intent, REQUEST_CODE_PAYMENT);
  }

  private final ActivityEventListener mActivityEventListener = new BaseActivityEventListener() {
    @Override
    public void onActivityResult(Activity activity, int requestCode, int resultCode, Intent data) {
      if (requestCode == 1) {
        if (mEsewaPromise != null) {
          if (resultCode == Activity.RESULT_CANCELED) {
            mEsewaPromise.reject("PAYMENT_ERROR", "Payment was unsuccessful");
          } else if (resultCode == Activity.RESULT_OK) {
            String result = data.getStringExtra(ESewaPayment.EXTRA_RESULT_MESSAGE);
            Bundle bundle = data.getExtras();

            WritableMap map = Arguments.createMap();
            map.putString("response", result);

            mEsewaPromise.resolve(map);
          } else if(resultCode == ESewaPayment.RESULT_EXTRAS_INVALID){
            String s = data.getStringExtra(ESewaPayment.EXTRA_RESULT_MESSAGE);
            mEsewaPromise.reject("PAYMENT_ERROR", s);
          }

          mEsewaPromise = null;
        }
      }
    }
  };
}
