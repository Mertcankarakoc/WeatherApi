package org.example;

import com.evam.sdk.outputaction.AbstractOutputAction;
import com.evam.sdk.outputaction.IOMParameter;
import com.evam.sdk.outputaction.OutputActionContext;
import com.evam.sdk.outputaction.model.DesignerMetaParameters;
import com.evam.sdk.outputaction.model.ReturnParameter;
import com.evam.sdk.outputaction.model.ReturnType;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.Duration;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;


public class WeatherTestAction extends AbstractOutputAction {

    private static final String API_VALUE = "API_VALUE";
    private static final Logger LOGGER = LoggerFactory.getLogger(WeatherTestAction.class);
    private static final String apiUrl = "http://api.weatherapi.com/v1/current.json";
    private static final String apiKey ="3390d794176f48e680383810240205";
    private static final String CITY_NAME = "CITY_NAME";
    private static final String COUNTRY = "COUNTRY";
    private static final String LON = "LON";
    private static final String LAT = "LAT";
    private static final String TEMPERATURE = "TEMPERATURE";
    private static final String WIND_HOURS = "WIND_HOURS";



    public String cityname_js_val, country_js_str,actorId;
    public double lon_js_val, lat_js_val,temp_c,wind_kph;

    @Override
    public int execute(OutputActionContext outputActionContext) throws Exception {

        Duration timeElapsed = null;
        Instant startAction = Instant.now();
        actorId = outputActionContext.getActorId();
        String scenarioName = outputActionContext.getScenarioName();
        String apiValue = (String) outputActionContext.getParameter("API_VALUE");

        if (apiValue.trim().isEmpty() || "null".equalsIgnoreCase(apiValue)){
            outputActionContext.setMessage("Api value must not be empty or null");
            return -1;
        }

        try {
            StringBuilder responseData = new StringBuilder();
            URIBuilder builder = new URIBuilder(apiUrl);
            builder.setParameter("q", apiValue);
            LOGGER.info("APIValueStatus : RequestBody {}", builder.toString());
            HttpPost httpPost = new HttpPost(builder.build());
            httpPost.setHeader("Accept", "application/json");
            httpPost.setHeader("key", apiKey);
            CloseableHttpClient httpClient = HttpClients.createDefault();

            Instant httpRequestStart = Instant.now();
            CloseableHttpResponse httpResponse = httpClient.execute(httpPost);
            Instant httpRequestStop = Instant.now();

            timeElapsed = Duration.between(httpRequestStart, httpRequestStop);

            LOGGER.info("APIValueStatus : Http Request Time Length {} ", timeElapsed.toMillis());

            Instant httpResponseStart = Instant.now();
            Scanner scanner = new Scanner(httpResponse.getEntity().getContent());
            while (scanner.hasNext()) {
                responseData.append(scanner.nextLine().trim());
            }
            scanner.close();
            Instant httpResponseStop = Instant.now();
            timeElapsed = Duration.between(httpResponseStart, httpResponseStop);

            LOGGER.info("APIValueStatus : Http Response Time Length {} ", timeElapsed.toMillis());

            String statusCode = String.valueOf(httpResponse.getStatusLine().getStatusCode());

            LOGGER.info("APIValueStatus : SCENARIO_NAME {}, ACTOR_ID {}, API_VALUE_RESPONSE {} , BPC_GET_CARD_STATUS_CODE {}", scenarioName, actorId, responseData.toString(), statusCode);

            if (statusCode.equals(ActionProperties.SUCCESS_CODE)) {
                JSONObject jsonObject = new JSONObject(responseData.toString());
                JSONObject location = jsonObject.getJSONObject("location");
                JSONObject location_js = new JSONObject(location.toString());
                //
                JSONObject current = jsonObject.getJSONObject("current");
                JSONObject current_js = new JSONObject(current.toString());
                ///
                cityname_js_val = location_js.getString("name");
                ///
                country_js_str = location_js.getString("country");
                ///
                lon_js_val = location_js.getDouble("lon");
                ///
                lat_js_val = location_js.getDouble("lat");
                ///

                temp_c = current_js.getDouble("temp_c");
                ///
                wind_kph = current_js.getDouble("wind_kph");

            }

            outputActionContext.getReturnMap().put("CITY_NAME", cityname_js_val);
            outputActionContext.getReturnMap().put("COUNTRY", country_js_str);
            outputActionContext.getReturnMap().put("LON", lon_js_val);
            outputActionContext.getReturnMap().put("LAT", lat_js_val);
            outputActionContext.getReturnMap().put("TEMPERATURE", temp_c);
            outputActionContext.getReturnMap().put("WIND_HOURS", wind_kph);

        } catch (Exception e){
            LOGGER.error("GetApiStatus : Error {}", e.toString());
            return -1;
        }
        Instant stopAction = Instant.now();
        timeElapsed = Duration.between(startAction, stopAction);

        LOGGER.info("GetApiStatusOA : Action Time Length {} ", timeElapsed.toMillis());
        return 0;
        }

    @Override
    protected List<IOMParameter> getParameters() {
        ArrayList<IOMParameter> parameters = new ArrayList<>();
        parameters.add(new IOMParameter(API_VALUE,"API Value"));
        return parameters;
    }

    @Override
    public boolean actionInputStringShouldBeEvaluated() {
        return false;
    }

    @Override
    public String getVersion() {
        return null;
    }

    @Override
    public ReturnParameter[] getRetParams(DesignerMetaParameters designerMetaParameters) {
        ReturnParameter actor_Id = new ReturnParameter(actorId, ReturnType.String);
        ReturnParameter city_name = new ReturnParameter(CITY_NAME, ReturnType.String);
        ReturnParameter country_name = new ReturnParameter(COUNTRY, ReturnType.String);
        ReturnParameter _lon = new ReturnParameter(LON, ReturnType.Numeric);
        ReturnParameter _lat = new ReturnParameter(LAT, ReturnType.Numeric);
        ReturnParameter _temp = new ReturnParameter(TEMPERATURE, ReturnType.Numeric);
        ReturnParameter _windh = new ReturnParameter(WIND_HOURS, ReturnType.Numeric);
        ReturnParameter apiValue = new ReturnParameter(API_VALUE,ReturnType.String);
        return new ReturnParameter[]{actor_Id,city_name, country_name, _lon,_lat,_temp,_windh,apiValue};
    }

    @Override
    public boolean isReturnable() {
        return true;
    }
}
