package com.ericyl.utils.net.support.volley;

import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.HttpHeaderParser;
import com.ericyl.utils.util.ResponseCodeUtils;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.io.Serializable;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.ParameterizedType;


/**
 * <p>
 * requestBody like {"code":int, "message":"String", "result":{T}}
 * code extends java.lang.int, message extends java.lang.String and result implements Serializable.
 */
public class JsonResultRequest<T extends Serializable> extends Request<T> {
    private final Response.Listener<T> listener;
    private String requestBody;


    protected static final String PROTOCOL_CHARSET = "utf-8";

    /** Content type for request. */
    private static final String PROTOCOL_CONTENT_TYPE =
            String.format("application/json; charset=%s", PROTOCOL_CHARSET);

    public JsonResultRequest(int method, String url, String requestBody, Response.Listener<T> listener,
                             Response.ErrorListener errorListener) {
        super(method, url, errorListener);
        this.listener = listener;
        this.requestBody = requestBody;
    }

    public JsonResultRequest(String url, String requestBody, Response.Listener<T> listener, Response.ErrorListener errorListener) {
        this(Method.DEPRECATED_GET_OR_POST, url, requestBody, listener, errorListener);
    }

    @Override
    protected Response<T> parseNetworkResponse(NetworkResponse response) {
        String parsed;
        try {
            parsed = new String(response.data, HttpHeaderParser.parseCharset(response.headers));
        } catch (UnsupportedEncodingException e) {
            parsed = new String(response.data);
        }
        JsonElement jsonElement = new JsonParser().parse(parsed);
        int code = jsonElement.getAsJsonObject().get(ResponseCodeUtils.CODE).getAsInt();
        JsonObject obj = jsonElement.getAsJsonObject().get(ResponseCodeUtils.RESULT).getAsJsonObject();
        Gson gson = new GsonBuilder().setDateFormat("yyyy-MM-dd HH:mm:ss").create();
        T result = gson.fromJson(obj, (Class<T>) ((ParameterizedType) getClass().getGenericSuperclass()).getActualTypeArguments()[0]);
        if (code != ResponseCodeUtils.SUCCESS) {
            String message = jsonElement.getAsJsonObject().get(ResponseCodeUtils.MESSAGE).getAsString();
            JsonObject errorObject = new JsonObject();
            errorObject.addProperty("code", code);
            errorObject.addProperty("message", message);
            return Response.error(new VolleyError(errorObject.toString()));
        } else
            return Response.success(result, HttpHeaderParser.parseCacheHeaders(response));
    }

    @Override
    protected void deliverResponse(T response) {
        listener.onResponse(response);
    }

    @Override
    public String getBodyContentType() {
        return PROTOCOL_CONTENT_TYPE;
    }

    @Override
    public byte[] getBody() {
        try {
            return requestBody == null ? null : requestBody.getBytes(PROTOCOL_CHARSET);
        } catch (UnsupportedEncodingException uee) {
            VolleyLog.wtf("Unsupported Encoding while trying to get the bytes of %s using %s",
                    requestBody, PROTOCOL_CHARSET);
            return null;
        }
    }

}
