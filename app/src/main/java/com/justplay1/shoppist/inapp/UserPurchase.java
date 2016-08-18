//package com.justplay1.shoppist.inapp;
//
//import android.support.annotation.NonNull;
//
//import org.json.JSONException;
//import org.json.JSONObject;
//import org.solovyev.android.checkout.Purchase;
//
///**
// * Created by Mkhytar on 03.03.2016.
// */
//public class UserPurchase {
//    // the item's product identifier. Every item has a product ID, which you must specify
//    // in the application's product list on the Google Play Developer Console
//    @NonNull
//    public final String sku;
//    // a unique order identifier for the transaction. This identifier corresponds to the
//    // Google Wallet Order ID
//    @NonNull
//    public final String orderId;
//    // the application package from which the purchase originated
//    @NonNull
//    public final String packageName;
//    // the time the product was purchased, in milliseconds since the epoch (Jan 1, 1970)
//    public final long time;
//    // the purchase state of the order
//    @NonNull
//    public final Purchase.State state;
//    // a developer-specified string that contains supplemental information about an order.
//    // You can specify a value for this field when you make a getBuyIntent request
//    @NonNull
//    public final String payload;
//    // a token that uniquely identifies a purchase for a given item and user pair
//    @NonNull
//    public final String token;
//    // Indicates whether the subscription renews automatically. If true, the subscription is active,
//    // and will automatically renew on the next billing date. If false, indicates that the user has
//    // canceled the subscription. The user has access to subscription content until the next billing
//    // date and will lose access at that time unless they re-enable automatic renewal
//    public final boolean autoRenewing;
//    /**
//     * Raw data returned from {@link com.android.vending.billing.IInAppBillingService#getPurchases}
//     */
//    @NonNull
//    public final String data;
//    /**
//     * Signature of {@link #data}
//     */
//    @NonNull
//    public final String signature;
//
//    UserPurchase(@NonNull String sku, @NonNull String orderId, @NonNull String packageName, long time, int state, @NonNull String payload, @NonNull String token, boolean autoRenewing, @NonNull String data, @NonNull String signature) {
//        this.sku = sku;
//        this.orderId = orderId;
//        this.packageName = packageName;
//        this.time = time;
//        this.state = Purchase.State.valueOf(String.valueOf(state));
//        this.payload = payload;
//        this.token = token;
//        this.autoRenewing = autoRenewing;
//        this.signature = signature;
//        this.data = data;
//    }
//
//    @NonNull
//    public static UserPurchase fromJson(@NonNull String data, @NonNull String signature) throws JSONException {
//        final JSONObject json = new JSONObject(data);
//        final String sku = json.getString("productId");
//        final String orderId = json.optString("orderId");
//        final String packageName = json.optString("packageName");
//        final long purchaseTime = json.getLong("purchaseTime");
//        final int purchaseState = json.optInt("purchaseState", 0);
//        final String payload = json.optString("developerPayload");
//        final String token = json.optString("token", json.optString("purchaseToken"));
//        final boolean autoRenewing = json.optBoolean("autoRenewing");
//        return new UserPurchase(sku, orderId, packageName, purchaseTime, purchaseState, payload, token, autoRenewing, data, signature);
//    }
//}
