//package com.justplay1.shoppist.inapp;
//
//import android.support.annotation.NonNull;
//import android.support.annotation.Nullable;
//
//import org.solovyev.android.checkout.Sku;
//
//public class SkuUi {
//
//    public final Sku sku;
//
//    @Nullable
//    public final String token;
//
//    private SkuUi(Sku sku, @Nullable String token) {
//        this.sku = sku;
//        this.token = token;
//    }
//
//    @NonNull
//    public static SkuUi create(Sku sku, @Nullable String token) {
//        return new SkuUi(sku, token);
//    }
//
//    @NonNull
//    public static String getTitle(@NonNull Sku sku) {
//        final int i = sku.title.indexOf("(");
//        if (i > 0) {
//            if (sku.title.charAt(i - 1) == ' ') {
//                return sku.title.substring(0, i - 1);
//            } else {
//                return sku.title.substring(0, i);
//            }
//        }
//        return sku.title;
//    }
//
//    public boolean isPurchased() {
//        return token != null;
//    }
//}
