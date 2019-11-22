import java.util.ArrayList;
import java.util.TimerTask;

public class TimeWrapper extends TimerTask {
    Bot bot;

    @Override
    public void run() {
        DBController db = new DBController();
        ArrayList<String[]> users = db.timeWrapsAll();

        for (int i = 0; i < users.size(); i++) {
            String[] user = users.get(i);
            if (i % 50 == 0) {
                try {
                    Thread.sleep(60000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            Weather weather = new Weather();
            bot.sendWrapper(user[0], weather.getWeatherByWrapper(user[1], user[2]), weather.getIco());
        }
    }


    public TimeWrapper(Bot incBot) {
        this.bot = incBot;
    }
}
