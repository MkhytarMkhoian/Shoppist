package com.justplay1.shoppist.di.modules;

import android.content.Context;

import com.justplay1.shoppist.preferences.ShoppistPreferences;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

/**
 * Created by Mkhytar on 14.05.2016.
 */
@Module
public class PreferenceModule {

    @Provides
    @Singleton
    ShoppistPreferences provideShoppistPreferences(Context context) {
        ShoppistPreferences preferences = new ShoppistPreferences(context);
        preferences.initPreferences();
        return preferences;
    }
}
