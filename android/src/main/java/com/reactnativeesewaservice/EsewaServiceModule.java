package com.reactnativeesewaservice;

import android.app.Activity;
import android.content.Intent;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.esewa.android.sdk.payment.ESewaConfiguration;
import com.esewa.android.sdk.payment.ESewaPayment;
import com.esewa.android.sdk.payment.ESewaPaymentActivity;
import com.facebook.react.bridge.ActivityEventListener;
import com.facebook.react.bridge.BaseActivityEventListener;
import com.facebook.react.bridge.Promise;
import com.facebook.react.bridge.ReactApplicationContext;
import com.facebook.react.bridge.ReactContextBaseJavaModule;
import com.facebook.react.bridge.ReactMethod;
import com.facebook.react.module.annotations.ReactModule;

import static android.content.ContentValues.TAG;

@ReactModule(name = EsewaServiceModule.NAME)
public class EsewaServiceModule extends ReactContextBaseJavaModule {
  public static final String NAME = "Esewa";
  private final ReactApplicationContext reactContext;
  private static final int REQUEST_CODE_PAYMENT = 1;
  private ESewaConfiguration eSewaConfiguration;
  private Promise esewaPromise;

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
  public void makePayment(String amount, String productName, String productID, String callbackURL, Promise promise) {
    esewaPromise = promise;
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

      try {
        if (requestCode == REQUEST_CODE_PAYMENT) {
          Activity currentActivity = getCurrentActivity();

          if (resultCode == Activity.RESULT_OK) {
            if (data == null) {
              throw new Exception();
            }
            String message = data.getStringExtra(ESewaPayment.EXTRA_RESULT_MESSAGE);
            Log.i(TAG, "Proof of Payment " + message);
            Toast.makeText(currentActivity, "SUCCESSFUL PAYMENT", Toast.LENGTH_SHORT).show();
            esewaPromise.resolve(resultCode);
          }

          else if (resultCode == Activity.RESULT_CANCELED) {
            Toast.makeText(currentActivity, "Canceled By User", Toast.LENGTH_SHORT).show();
            esewaPromise.resolve(resultCode);
          }

          else if (resultCode == ESewaPayment.RESULT_EXTRAS_INVALID) {
            if (data == null) {
              throw new Exception();
            }
            String message = data.getStringExtra(ESewaPayment.EXTRA_RESULT_MESSAGE);
            Log.i(TAG, "Proof of Payment " + message);
            esewaPromise.resolve(resultCode);
          }

          esewaPromise.reject("Unkonwn Esewa Error", new Exception());
        }
      } catch(Exception e) {
        esewaPromise.reject("Esewa Error", e);
      }
    }
  };
}
