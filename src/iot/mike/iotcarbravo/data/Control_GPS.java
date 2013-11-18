package iot.mike.iotcarbravo.data;


public class Control_GPS {
    private static final String order = 
        "{\"action\":\"gps\",\"param\":{\"longtitude\":\"on\",\"latitude\":\"on\",\"speed\":\"on\",\"direction\":\"on\",\"height\":\"on\"}";
    public String getOrder() {
        return order;
    }
}
