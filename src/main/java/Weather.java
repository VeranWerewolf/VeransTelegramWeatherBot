import org.telegram.telegrambots.meta.api.objects.Message;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Scanner;

public class Weather {
    private String ico;
    public String getIco() {
        return ico;
    }

    public String getWeatherByLocatoin(Message message) {
        String result = "";
        URL url = null;
        try {
            url = new URL("https://api.openweathermap.org/data/2.5/weather?lat=" + message.getLocation().getLatitude()
                    +  "&lon=" +message.getLocation().getLongitude()  + "&units=metric&appid=" + getOWMtoken());
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        System.out.println(url); //URL test
        Scanner incomingStream = null;
        try {
            incomingStream = new Scanner((InputStream) url.getContent());
        } catch (IOException e) {
            e.printStackTrace();
        }
        while (incomingStream.hasNext()) {
            result += incomingStream.nextLine();
        }
        //System.out.println(result); //result test

        WeatherModel weatherModel = new WeatherModel(result);
        ico = "https://openweathermap.org/img/w/" + weatherModel.getIcon()+".png";
        return "City:" + weatherModel.getName()+ "\nTemp: " + weatherModel.getTemp() +" C\nHumidity: "+ weatherModel.getHumidity()+"%\nMain: "
                + weatherModel.getMain();
    }

    public String getWeatherByWrapper(String lat, String lon) {
        String result = "";
        URL url = null;
        try {
            url = new URL("https://api.openweathermap.org/data/2.5/weather?lat=" + lat
                    +  "&lon=" + lon  + "&units=metric&appid=" + getOWMtoken());
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        System.out.println(url); //URL test
        Scanner incomingStream = null;
        try {
            incomingStream = new Scanner((InputStream) url.getContent());
        } catch (IOException e) {
            e.printStackTrace();
        }
        while (incomingStream.hasNext()) {
            result += incomingStream.nextLine();
        }
        //System.out.println(result); //result test

        WeatherModel weatherModel = new WeatherModel(result);
        ico = "https://openweathermap.org/img/w/" + weatherModel.getIcon()+".png";
        return "City:" + weatherModel.getName()+ "\nTemp: " + weatherModel.getTemp() +" C\nHumidity: "+ weatherModel.getHumidity()+"%\nMain: "
                + weatherModel.getMain();
    }

    public static String getWeatherByCity(Message message) throws IOException {
    String result = "";
        URL url = new URL("https://api.openweathermap.org/data/2.5/weather?q=" + message + "&units=metric&appid=" + getOWMtoken());
        //System.out.println(url); //URL test
        Scanner incomingStream = new Scanner((InputStream) url.getContent());
        while (incomingStream.hasNext()) {
            result += incomingStream.nextLine();
        }
        //System.out.println(result); //result test

        WeatherModel weatherModel = new WeatherModel(result);

        return "City:" + weatherModel.getName()+ "\nTemp: " + weatherModel.getTemp() +" C\nHumidity: "+ weatherModel.getHumidity()+"%\nMain: "
                + weatherModel.getMain()+"https://openweathermap.org/img/w/"+ weatherModel.getIcon()+".png";
    }

    private static String getOWMtoken() {
        return "OWMTokenHere";
    }
}