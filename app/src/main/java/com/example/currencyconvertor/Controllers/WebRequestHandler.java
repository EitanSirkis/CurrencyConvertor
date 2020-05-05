package com.example.currencyconvertor.Controllers;

/* class is using Volly package in order to get currency information over the net. Volley can be replaced with
any other object that support http requests  */

import android.content.Context;
import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import org.json.JSONObject;

import java.lang.ref.WeakReference;
import java.util.HashMap;
import java.util.Map;


public class WebRequestHandler
{

    public static void getJsonObject(WeakReference<Context> context,final ResponseHandler handler, String url, final HashMap<String,String> headers, final HashMap<String,String> params)
    {
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, url, (JSONObject) null, new Response.Listener<JSONObject>()
        {
            @Override
            public void onResponse(JSONObject response)
            {
                handler.onWebResponseFinished(response);
            }
        }, new Response.ErrorListener()
        {
            @Override
            public void onErrorResponse(VolleyError error)
            {
                handler.onWebResponseFinished(null);
            }
        })
        {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError
            {
                if (headers != null)
                {
                    return headers;
                }

                else
                {
                    return super.getHeaders();
                }

            }

            @Override
            public Map<String, String> getParams() throws AuthFailureError
            {
                if (params != null)
                {
                    return params;
                }
                else
                    {
                    return super.getParams();
                }

            }
        };
        jsonObjectRequest.setShouldCache(false);
        RequestQueue requestQueue = Volley.newRequestQueue(context.get());
        requestQueue.add(jsonObjectRequest);
    }

    // clients of 'getJsonObject' method must implement this method
    public interface ResponseHandler
    {
        public void onWebResponseFinished (JSONObject jsonObjectResponse);
    }
}
