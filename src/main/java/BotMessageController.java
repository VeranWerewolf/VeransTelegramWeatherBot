import org.telegram.telegrambots.meta.api.objects.Message;


public final class BotMessageController {
    Bot thisBot;
    private String textMessage;
    Message message;

    public BotMessageController(Bot bot) {
        helpMessage();
        thisBot = bot;
    }

    public String getTextMessage() {
        return textMessage;
    }

    public void MessageParser(Message message) {

        switch (message.getText()) {
            case "/start":
                startMessage(message);
                break;
            case "/sub":
                sub(message);
                break;
            case "/unsub":
                unsub(message);
                break;
            case "/settings":
                settingsMessage();
                break;
            case "/getweather":
                getWeather();
                break;
            case "/help":
                helpMessage();
                break;
            default:
                defaultMessage(message);
                break;
        }
    }

    protected void stickerParser(Message message, String marker) {
        switch (marker) {
            case ("OK"):
                thisBot.sendSticker(message, "CAADAgADIQIAAtzyqwe0Fc_LsMF1aAI");
                break;
            case ("NF"):
                thisBot.sendSticker(message, "CAADAgADJAIAAtzyqwfF1W5c-h3UagI");
                break;
            case ("WRG"):
                thisBot.sendSticker(message, "CAADAgADDgIAAtzyqwcajUKMi_aq9wI");
                break;
            case ("SUB"):
                thisBot.sendSticker(message, "CAADAgADEgIAAtzyqwe1qmzuvl61UgI");
                break;
            case ("UNSUB"):
                thisBot.sendSticker(message, "CAADAgADCQIAAtzyqwdEux5ezoBTwQI");
                break;
            case ("COLD"):
                thisBot.sendSticker(message, "CAADAgADCwIAAtzyqweLxkDf5AxPFAI");
                break;
            default:
                return;
        }
    }

    protected void startMessage(Message message) {
        StringBuilder start = new StringBuilder();

        start.append("Sup, ");
        //Пытаемся вытащить имя пользователя
        if (message.getFrom().getFirstName() != null && !message.getFrom().getFirstName().equals("")) {
            start.append(message.getFrom().getFirstName());
        } else {
            start.append("Anonymous Stranger");
        }

        start.append("! \nI am WeatherBot and you can just /sub me." +
                " \nManage everything you want in /settings . \nIf you want more info just call for /help.");
        this.textMessage = start.toString();
    }

    protected void settingsMessage() {
        this.textMessage = "To update your location just send it to me.";
    }
    protected void getWeather() {
        Weather weather = new Weather();
        thisBot.sendTextMessage(message, weather.getWeatherByLocatoin(message), weather.getIco());
    }

    protected void locationMessage(Message message) {
        Weather weather = new Weather();
        DBController db = new DBController();
        thisBot.sendTextMessage(message, weather.getWeatherByLocatoin(message), weather.getIco());
        db.changeLocation(message);
        this.textMessage = "Your location has been updated.";
    }

    protected void helpMessage() {
        this.textMessage = "You can: \n/sub - for subscribe.\n/unsub - for unsubscribe.\n" +
                "/settings - to manage your subscription.\nIt is simple ;)";
    }

    protected void defaultMessage(Message message) {
        stickerParser(message, "NF");
        this.textMessage = "No such command found.\nUse /help to see available commands.";
    }

    protected void sub(Message message) {
        DBController db = new DBController();
        String result = db.subcribe(message);
        System.out.println(result);
        switch (result) {
            case "AlrSub":
                stickerParser(message, "OK");
                this.textMessage = "Subscription already online.";
                break;
            case "SubAgn":
                stickerParser(message, "SUB");
                this.textMessage = "Hello again! Subscription online.";
                break;
            case "NewSub":
                stickerParser(message, "SUB");
                this.textMessage = "Subscription online.";
                break;
            case "WRG":
                stickerParser(message, "WRG");
                this.textMessage = "Something went wrong.";
                break;
        }
    }

    protected void unsub(Message message) {
        DBController db = new DBController();
        String result = db.unsubcribe(message);
        System.out.println(result);
        switch (result) {
            case "NotUsr":
                stickerParser(message, "SUBFRST");
                this.textMessage = "For unsubscribe you must subscribe first ;)";
                break;
            case "UnSub":
                stickerParser(message, "UNSUB");
                this.textMessage = "Subscription offline.";
                break;
            case "AlrUnSub":
                stickerParser(message, "COLD");
                this.textMessage = "Subscription already offline. Don't get cold there.";
                break;
            case "WRG":
                stickerParser(message, "UNSUB");
                this.textMessage = "Something went wrong.";
                break;
        }
    }

}
