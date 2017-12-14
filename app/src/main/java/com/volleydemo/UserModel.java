package com.volleydemo;

import android.app.Activity;

import com.android.volley.VolleyError;
import com.volleydemo.utils.HttpHelper;

import org.json.JSONObject;

/**
 * Created by guojiadong
 * on 2016/12/14.
 */

public class UserModel extends HttpHelper<String> {
    public UserModel(Activity activity) {
        super(activity);
    }

    @Override
    protected void disposeVolleyError(VolleyError error) {

    }

    @Override
    protected void disposeResponse(JSONObject response) {

    }
}
