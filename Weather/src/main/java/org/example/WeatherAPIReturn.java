package org.example;

import com.evam.sdk.outputaction.AbstractOutputAction;
import com.evam.sdk.outputaction.IOMParameter;
import com.evam.sdk.outputaction.OutputActionContext;
import com.evam.sdk.outputaction.model.DesignerMetaParameters;
import com.evam.sdk.outputaction.model.ReturnParameter;
import com.evam.sdk.outputaction.model.ReturnType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;

public class WeatherAPIReturn extends AbstractOutputAction {

    private static final Logger LOGGER = LoggerFactory.getLogger(WeatherAPIReturn.class);

    @Override
    public int execute(OutputActionContext outputActionContext) throws Exception {
        String actorId = outputActionContext.getActorId();
        String scenarioName = outputActionContext.getScenarioName();

        String city_name = (String) outputActionContext.getParameter("CITY_NAME");
        String country_name = (String) outputActionContext.getParameter("COUNTRY");
        double longditude = Double.parseDouble((String) outputActionContext.getParameter("LON"));
        double latitude = Double.parseDouble((String) outputActionContext.getParameter("LAT"));
        double temp_c = Double.parseDouble((String) outputActionContext.getParameter("TEMPERATURE"));
        double wind_kph = Double.parseDouble((String) outputActionContext.getParameter("WIND_HOURS"));
        double feelslike_temp = Double.parseDouble((String) outputActionContext.getParameter("FEELSLIKE_TEMP"));

        LOGGER.info("VALUE STATUS : CITY_NAME {}, COUNTRY_NAME {}, LON {}, LAT {}, TEMPERATURE {}, WIND/HOURS {}, FEELSLIKE_TEMP {}",city_name,country_name,longditude,latitude,temp_c,wind_kph,feelslike_temp );

        return 0;

    }

    @Override
    protected List<IOMParameter> getParameters() {
        ArrayList<IOMParameter> allValueList = new ArrayList<>();
        allValueList.add(new IOMParameter("CITY_NAME", "this is city name"));
        allValueList.add(new IOMParameter("COUNTRY" , "this is country name"));
        allValueList.add(new IOMParameter("LON","this is lon"));
        allValueList.add(new IOMParameter("LAT" ,"this is lat"));
        allValueList.add(new IOMParameter("TEMPERATURE" , "this is temperature"));
        allValueList.add(new IOMParameter("WIND_HOURS" , "this is WIND_HOURS"));
        allValueList.add(new IOMParameter("FEELSLIKE_TEMP" , "this is FEELSLIKE_TEMP"));
        return allValueList;
    }

    @Override
    public boolean actionInputStringShouldBeEvaluated() {
        return false;
    }

    @Override
    public String getVersion() {
        return null;
    }

    //elde edilen verileri saklamak için kullanırız...
    @Override
    public ReturnParameter[] getRetParams(DesignerMetaParameters designerMetaParameters) {
        ReturnParameter actorId = new ReturnParameter("actor_id", ReturnType.String);
        ReturnParameter city_name = new ReturnParameter("CITY_NAME", ReturnType.String);
        ReturnParameter country_name = new ReturnParameter("COUNTRY", ReturnType.String);
        ReturnParameter _lon = new ReturnParameter("LON", ReturnType.Numeric);
        ReturnParameter _lat = new ReturnParameter("LAT", ReturnType.Numeric);
        ReturnParameter temp_c = new ReturnParameter("TEMPERATURE", ReturnType.Numeric);
        ReturnParameter wind_kph = new ReturnParameter("WIND_HOURS", ReturnType.Numeric);
        ReturnParameter feelslike_c = new ReturnParameter("FEELSLIKE_TEMP", ReturnType.Numeric);
        return new ReturnParameter[]{city_name, country_name, _lon,_lat,temp_c,wind_kph,feelslike_c};
    }

    @Override
    public boolean isReturnable() {
        return true;
    }
}
