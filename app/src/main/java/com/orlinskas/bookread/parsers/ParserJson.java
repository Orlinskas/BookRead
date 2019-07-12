package com.orlinskas.bookread.parsers;

import org.json.JSONObject;
/**
 * @author Orlinskas
 * @version 1
 */
public class ParserJson {
    public String parse (String json) throws Exception {
        JSONObject jsonObject = new JSONObject(json);
        JSONObject jsonObjectResult = jsonObject.getJSONObject("result");
        return jsonObjectResult.getString("translated");
    }
}
