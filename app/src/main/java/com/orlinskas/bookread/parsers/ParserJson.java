package com.orlinskas.bookread.parsers;

import org.json.JSONObject;

public class ParserJson {
    public String parse (String json) {
            try {
                JSONObject jsonObject = new JSONObject(json);
                JSONObject jsonObjectResult = jsonObject.getJSONObject("result");
                return jsonObjectResult.getString("translated");
            } catch (Exception e) {
                e.printStackTrace();
            }
        return null;
    }


}
