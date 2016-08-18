package com.justplay1.shoppist.utils;

import android.app.Activity;
import android.content.Context;
import android.content.OperationApplicationException;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.database.Cursor;
import android.os.RemoteException;
import android.util.TypedValue;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.Spinner;

import com.justplay1.shoppist.App;
import com.justplay1.shoppist.R;
import com.justplay1.shoppist.database.ShoppingListContact;
import com.justplay1.shoppist.database.TablesHolder;
import com.justplay1.shoppist.models.Category;
import com.justplay1.shoppist.models.Currency;
import com.justplay1.shoppist.models.Header;
import com.justplay1.shoppist.models.Product;
import com.justplay1.shoppist.models.ShoppingListItem;
import com.justplay1.shoppist.models.Unit;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.MissingResourceException;
import java.util.Random;
import java.util.UUID;

/**
 * Created by Mkhitar on 21.11.2014.
 */
public class ShoppistUtils {

    public static final String MAIN_TAG = "Shoppist";

    private ShoppistUtils() {

    }

    public static boolean isTablet(Context context) {
        return (context.getResources().getConfiguration().screenLayout
                & Configuration.SCREENLAYOUT_SIZE_MASK)
                >= Configuration.SCREENLAYOUT_SIZE_LARGE;
    }

    public static String[] concat(String[] a, String[] b) {
        if (a == null) return b;
        int aLen = a.length;
        int bLen = b.length;
        String[] c = new String[aLen + bLen];
        System.arraycopy(a, 0, c, 0, aLen);
        System.arraycopy(b, 0, c, aLen, bLen);
        return c;
    }

    public static String generateId(String name) {
        return UUID.nameUUIDFromBytes((name + UUID.randomUUID()).getBytes()).toString();
    }

    public static void hideKeyboard(Activity activity, EditText editText) {
        InputMethodManager imm = (InputMethodManager) activity.getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(editText.getWindowToken(), 0);
    }

    public static void showKeyboard(Activity activity, EditText editText) {
        InputMethodManager imm = (InputMethodManager) activity.getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.showSoftInput(editText, 0);
    }

    public static String getFirstCharacter(String name) {
        String s = name.trim();
        if (!s.equals("")) {
            return String.valueOf(s.charAt(0));
        }
        return s;
    }

    public static String getAppVersion(Context context) {
        try {
            return context.getPackageManager().getPackageInfo(
                    context.getPackageName(), 0).versionName;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
            return "unknown";
        }
    }

    public static String buildShareString(Collection<ShoppingListItem> items) {
        StringBuilder textToSend = new StringBuilder();
        for (ShoppingListItem item : items) {
            textToSend.append(item.getName()).append(" ");
            if (item.getPrice() > 0) {
                textToSend.append("(").append(item.getPrice()).append(" ").append(item.getCurrency().getName());
                if (item.getQuantity() == 0) {
                    textToSend.append(")");
                }
            }
            if (item.getQuantity() > 0) {
                if (item.getPrice() == 0) {
                    textToSend.append("(");
                } else {
                    textToSend.append("/");
                }
                textToSend.append(item.getQuantity()).append(" ").append(item.getUnit().getShortName()).append(")");
            }
            textToSend.append("\n");
        }
        return textToSend.toString();
    }

    public static String filterSpace(String text) {
        int lineBreakCount = 0;
        int spaceCount = 0;
        StringBuilder builder = new StringBuilder();
        for (char character : text.toCharArray()) {
            if (character == '\n') {
                lineBreakCount++;
            } else {
                lineBreakCount = 0;
            }

            if (character == ' ') {
                spaceCount++;
            } else {
                spaceCount = 0;
            }

            if (lineBreakCount > 2 || spaceCount > 2) {
                continue;
            }
            builder.append(character);
        }
        return builder.toString();
    }

    public static int getRandomColor(Context context) {
        Random random = new Random();
        int[] color1 = context.getResources().getIntArray(R.array.categories_colors);
        int[] color2 = context.getResources().getIntArray(R.array.color_theme);
        int position = random.nextInt(color1.length + color2.length);
        if (position < color1.length) {
            return color1[position];
        } else {
            return color2[position - color1.length];
        }
    }

    public static int dpToPx(float dp, Resources resources) {
        float px = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, resources.getDisplayMetrics());
        return (int) px;
    }

    public static String getParseMessageFromException(Context context, String messageFromException) {
        String message;
        if ((messageFromException.contains(ShoppingListContact.Categories.NAME))
                || (messageFromException.contains(ShoppingListContact.Products.NAME))) {

            message = context.getResources().getString(R.string.this_name_already_used);
        } else if (messageFromException.contains(ShoppingListContact.Categories.COLOR)) {
            message = context.getResources().getString(R.string.this_color_already_used);
        } else {
            message = messageFromException;
        }
        return message;
    }

    public static <T> void removeHeaders(List<T> listWithHeaders) {
        if (listWithHeaders == null) return;

        List<T> list = new ArrayList<>(listWithHeaders);
        for (T item : list) {
            if (item instanceof Header) {
                listWithHeaders.remove(item);
            }
        }
    }

    public static void setKeepScreenOn(Window window, boolean on) {
        if (on) {
            window.addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        } else {
            window.clearFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        }
    }

    public static String toStringItemIds(List<String> ids) {
        StringBuilder builder = new StringBuilder();
        for (int i = 0; i < ids.size(); i++) {
            builder.append(ids.get(i));
            if (i < ids.size() - 1) {
                builder.append(", ");
            }
        }
        return builder.toString();
    }

    public static Currency getLocaleCurrency(List<Currency> currencies) {
        String locale = "USA";
        String id = "1";

        try {
            locale = Locale.getDefault().getISO3Country();
        } catch (MissingResourceException ignored) {

        }
        switch (locale) {
            case "USA":
                id = "1";
                break;
            case "UKR":
                id = "3";
                break;
            case "RUS":
                id = "4";
                break;
            case "CAN":
                id = "1";
                break;
            case "AUS":
                id = "1";
                break;
            case "FRA":
                id = "2";
                break;
            case "GBR":
                id = "5";
                break;
            case "DEU":
                id = "2";
                break;
            case "ESP":
                id = "2";
                break;
        }
        for (Currency currency : currencies) {
            if (currency.getId().equals(id)) {
                return currency;
            }
        }
        return currencies.get(0);
    }

    public static double roundDouble(double value, int scale) {
        BigDecimal smallNumber = new BigDecimal(value);
        BigDecimal decimal = smallNumber.setScale(scale, RoundingMode.HALF_UP);
        return decimal.doubleValue();
    }

    public static void setSpinnerItem(Spinner spinner, String nameColumn, String currentId) {
        int spinnerCount = spinner.getCount();
        for (int i = 0; i < spinnerCount; i++) {
            Cursor value = (Cursor) spinner.getItemAtPosition(i);
            String id = value.getString(value.getColumnIndex(nameColumn));
            if (id.equals(currentId)) {
                spinner.setSelection(i);
                break;
            }
        }
    }

    public static Map<String, Product> getStandardProductList() {
        String[] products = App.get().getTablesHolder().getContext().getResources().getStringArray(R.array.products);
        Map<String, Product> newProducts = new HashMap<>(products.length);

        for (String item : products) {
            Product product = new Product();
            String[] productsName = item.split(" ! ");

            Category category = new Category();
            category.setId(productsName[1]);
            product.setCategory(category);
            Unit unit = new Unit();
            unit.setId(Unit.NO_UNIT_ID);
            product.setUnit(unit);
            product.setTimestamp(0);
            product.setCreateByUser(false);
            product.setName(productsName[0]);
            product.setId(UUID.nameUUIDFromBytes((productsName[0]).getBytes()).toString());

            newProducts.put(product.getId(), product);
        }
        return newProducts;
    }

    public static Map<String, Category> getStandardCategories() {
        String[] categories = App.get().getTablesHolder().getContext().getResources().getStringArray(R.array.categories);
        int[] categoriesColors = App.get().getTablesHolder().getContext().getResources().getIntArray(R.array.categories_colors);
        String[] categoriesName;

        Map<String, Category> categoryList = new HashMap<>(categories.length);
        for (int i = 0; i < categories.length; i++) {
            categoriesName = categories[i].split(" ! ");
            Category category = new Category();
            category.setDirty(false);
            category.setId(categoriesName[1]);
            category.setName(categoriesName[0]);
            category.setColor(categoriesColors[i]);
            category.setEnable(true);
            category.setTimestamp(0);
            categoryList.put(category.getId(), category);
        }
        return categoryList;
    }

    public static Map<String, Unit> getStandardUnits() {
        String[] units = App.get().getTablesHolder().getContext().getResources().getStringArray(R.array.units);
        Map<String, Unit> unitList = new HashMap<>(units.length);
        for (String item : units) {
            String[] unit = item.split("/");
            String[] unitId = unit[1].split(" ! ");
            Unit unit1 = new Unit();
            unit1.setDirty(false);
            unit1.setTimestamp(0);
            unit1.setName(unit[0]);
            unit1.setShortName(unitId[0]);
            unit1.setId(unitId[1]);
            unitList.put(unit1.getId(), unit1);
        }
        return unitList;
    }

    public static Map<String, Currency> getStandardCurrencies() {
        String[] currency = App.get().getTablesHolder().getContext().getResources().getStringArray(R.array.currency);
        Map<String, Currency> currencyList = new HashMap<>(currency.length);
        for (String c : currency) {
            String[] currencyId = c.split(" ! ");
            Currency currency1 = new Currency();
            currency1.setDirty(false);
            currency1.setName(currencyId[0]);
            currency1.setId(currencyId[1]);
            currency1.setTimestamp(0);
            currencyList.put(currency1.getId(), currency1);
        }
        return currencyList;
    }

    public static void restoreData() throws RemoteException, OperationApplicationException {
        TablesHolder tablesHolder = App.get().getTablesHolder();
        App.get().getModelHolder().clear();

        Collection<Category> categoryList = ShoppistUtils.getStandardCategories().values();
        tablesHolder.getCategoriesTable().put(categoryList);

        Collection<Unit> unitList = ShoppistUtils.getStandardUnits().values();
        tablesHolder.getUnitTable().put(unitList);

        Collection<Currency> currencyList = ShoppistUtils.getStandardCurrencies().values();
        tablesHolder.getCurrenciesTable().put(currencyList);

        Collection<Product> newProducts = ShoppistUtils.getStandardProductList().values();
        tablesHolder.getProductsTable().put(newProducts);
    }
}
