import org.json.JSONArray;
import org.json.JSONObject;

public class WeatherModel {
    private String name;
    private Double temp;
    private Double humidity;
    private String icon;
    private String main;

    public WeatherModel() {
    }

    public WeatherModel(String result) {
        this(new JSONObject(result));
    }

    public WeatherModel(JSONObject incomingJSON) {
        this.setName(incomingJSON.getString("name"));

        JSONObject main = incomingJSON.getJSONObject("main");
        this.setTemp(main.getDouble("temp"));
        this.setHumidity(main.getDouble("humidity"));

        JSONArray getWeatherArray = incomingJSON.getJSONArray("weather");
        for (int i = 0; i < getWeatherArray.length(); i++) {
            JSONObject object = getWeatherArray.getJSONObject(i);
            this.setIcon((String) object.get("icon"));
            this.setMain((String) object.get("main"));
        }
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Double getTemp() {
        return temp;
    }

    public void setTemp(Double temp) {
        this.temp = temp;
    }

    public Double getHumidity() {
        return humidity;
    }

    public void setHumidity(Double humidity) {
        this.humidity = humidity;
    }

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    public String getMain() {
        return main;
    }

    public void setMain(String main) {
        this.main = main;
    }
}
