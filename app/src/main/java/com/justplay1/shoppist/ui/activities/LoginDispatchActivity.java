/*
 *  Copyright (c) 2014, Parse, LLC. All rights reserved.
 *
 *  You are hereby granted a non-exclusive, worldwide, royalty-free license to use,
 *  copy, modify, and distribute this software in source code or binary form for use
 *  in connection with the web services and APIs provided by Parse.
 *
 *  As with any software that integrates with the Parse platform, your use of
 *  this software is subject to the Parse Terms of Service
 *  [https://www.parse.com/about/terms]. This copyright notice shall be
 *  included in all copies or substantial portions of the software.
 *
 *  THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 *  IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS
 *  FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR
 *  COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER
 *  IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN
 *  CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 *
 */

package com.justplay1.shoppist.ui.activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import com.justplay1.shoppist.R;
import com.parse.Parse;
import com.parse.ParseUser;

public abstract class LoginDispatchActivity extends Activity {

    protected abstract Class<?> getTargetClass();

    private static final int LOGIN_REQUEST = 0;
    private static final int TARGET_REQUEST = 1;

    private static final String LOG_TAG = "ParseLoginDispatch";

    @Override
    final protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        runDispatch();
    }

    @Override
    final protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        setResult(resultCode);
        if (requestCode == LOGIN_REQUEST && resultCode == RESULT_OK) {
            runDispatch();
        } else {
            finish();
        }
    }

    private void runDispatch() {
        if (ParseUser.getCurrentUser() != null) {
            debugLog(getString(R.string.com_parse_ui_login_dispatch_user_logged_in) + getTargetClass());
            startActivityForResult(new Intent(this, getTargetClass()), TARGET_REQUEST);
        } else {
            debugLog(getString(R.string.com_parse_ui_login_dispatch_user_not_logged_in));
            startActivityForResult(new Intent(this, LoginActivity.class), LOGIN_REQUEST);
        }
    }

    private void debugLog(String message) {
        if (Parse.getLogLevel() <= Parse.LOG_LEVEL_DEBUG &&
                Log.isLoggable(LOG_TAG, Log.DEBUG)) {
            Log.d(LOG_TAG, message);
        }
    }
}
