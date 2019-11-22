import org.joda.time.DateTime;
import org.telegram.telegrambots.meta.api.objects.Message;

import java.sql.*;
import java.util.ArrayList;

public class DBController {
    Connection connection = null;
    String dbName = "Subscribers.db";

    public void connect() {
        if (connection != null) {
            connection = null;
        }
        try {
            Class.forName("org.sqlite.JDBC");
            connection = DriverManager.getConnection("jdbc:sqlite:" + dbName);
        } catch (ClassNotFoundException | SQLException e) {
            System.err.println(e.getClass().getName() + ": " + e.getMessage());
        }
    }

    public boolean isSubExists(Message message) {
        try {
            connect();
            ResultSet isSubscribed;
            PreparedStatement ifStatement = connection.prepareStatement
                    ("SELECT COUNT(*) as count FROM Subscribers WHERE AccName = (?) and ChatID = (?);");
            ifStatement.setString(1, message.getFrom().getUserName());
            ifStatement.setString(2, message.getChatId().toString());
            isSubscribed = ifStatement.executeQuery();
            isSubscribed.next();
            if (isSubscribed.getInt("count") == 0) {
                connection.close();
                return false;
            }
            connection.close();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return true;
    }

    public boolean isSubSubscribed(Message message) {
        try {
            connect();
            ResultSet isSubscribed;
            PreparedStatement ifStatement = connection.prepareStatement
                    ("SELECT COUNT(*) as count FROM Subscribers WHERE AccName = (?) and ChatID = (?)and isSubscribed = 1;");
            ifStatement.setString(1, message.getFrom().getUserName());
            ifStatement.setString(2, message.getChatId().toString());
            isSubscribed = ifStatement.executeQuery();
            isSubscribed.next();
            if (isSubscribed.getInt("count") > 0) {
                connection.close();
                return true;
            }
            connection.close();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return false;
    }

    public String subcribe(Message message) {
        boolean isSubExists = isSubExists(message);
        System.out.println("isSubExists " + isSubExists);
        if (!isSubExists) {
            try {
                connect();
                PreparedStatement preparedStatement = connection.prepareStatement
                        ("INSERT INTO Subscribers(ChatID,AccName,FirstName,LastName,isSubscribed,FirstSubDate,LastSubDate) " +
                                "VALUES(?,?,?,?,TRUE,DATETIME('now'),DATETIME('now'))");
                preparedStatement.setString(1, message.getChatId().toString());
                preparedStatement.setString(2, message.getFrom().getUserName());
                preparedStatement.setString(3, message.getFrom().getFirstName());
                preparedStatement.setString(4, message.getFrom().getLastName());
                preparedStatement.executeUpdate();
                connection.close();
                return "NewSub"; //new Sub
            } catch (SQLException e) {
                System.out.println(e.getMessage());
            }
        }

        boolean isSubSubscribed = isSubSubscribed(message);
        System.out.println("isSubSubscribed " + isSubSubscribed);
        if (isSubExists && isSubSubscribed) {
            return "AlrSub"; //already subed
        }else if (isSubExists && !isSubSubscribed) {
            try {
                connect();
                PreparedStatement preparedStatement = connection.prepareStatement
                        ("UPDATE Subscribers SET  LastSubDate = DATETIME('now'), isSubscribed = TRUE, UnSubDate = null" +
                                " WHERE AccName = (?) and ChatID = (?);");
                preparedStatement.setString(1, message.getFrom().getUserName());
                preparedStatement.setString(2, message.getChatId().toString());
                preparedStatement.executeUpdate();
                connection.close();
                return "SubAgn"; //subed again
            } catch (SQLException e) {
                System.out.println(e.getMessage());
            }
        }
        return "WRG";
    }

    public String unsubcribe(Message message) {
        boolean isSubExists = isSubExists(message);
        System.out.println("isSubExists " + isSubExists);
        if (!isSubExists) {
            return "NotUsr";
        }

        boolean isSubSubscribed = isSubSubscribed(message);

        System.out.println("isSubSubscribed " + isSubSubscribed);
        if (isSubExists && !isSubSubscribed) {
            return "AlrUnSub"; //already unsubed
        }
        if (isSubExists && isSubSubscribed) {
            try {
                connect();
                PreparedStatement preparedStatement = connection.prepareStatement
                        ("UPDATE Subscribers SET  UnSubDate = DATETIME('now'), isSubscribed = FALSE" +
                                " WHERE AccName = (?) and ChatID = (?);");
                preparedStatement.setString(1, message.getFrom().getUserName());
                preparedStatement.setString(2, message.getChatId().toString());
                preparedStatement.executeUpdate();
                connection.close();
                System.out.println("SA unsubcribe connection.close");
                return "UnSub"; //subed again
            } catch (SQLException e) {
                System.out.println(e.getMessage());
            }
        }
        return "WRG";
    }

    public void changeLocation(Message message) {
        boolean isSubExists = isSubExists(message);
        System.out.println("isSubExists " + isSubExists);
        if (!isSubExists) {
            return;
        }
        try {
            connect();
            PreparedStatement preparedStatement = connection.prepareStatement
                    ("UPDATE Subscribers SET  LocationLAT = (?), LocationLON = (?)" +
                            " WHERE AccName = (?) and ChatID = (?);");
            preparedStatement.setString(1, message.getLocation().getLatitude().toString());
            preparedStatement.setString(2, message.getLocation().getLongitude().toString());
            preparedStatement.setString(3, message.getFrom().getUserName());
            preparedStatement.setString(4, message.getChatId().toString());
            preparedStatement.executeUpdate();
            connection.close();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    public ArrayList <String[]> timeWrapsAll() {
        ArrayList <String[]> out = new ArrayList<>();
        try {
            connect();
            ResultSet resultSet;
            PreparedStatement ifStatement = connection.prepareStatement
                    ("SELECT ChatID as id, LocationLAT as lat, LocationLON as lon FROM Subscribers;");
            resultSet = ifStatement.executeQuery();

            while (resultSet.next()) {
                String[] resultString = new String[3];
                resultString[0] = resultSet.getString(1);
                resultString[1] = resultSet.getString(2);
                resultString[2] = resultSet.getString(3);
                out.add(resultString);
            }
            connection.close();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return out;
    }


    public void startConf() {
        Statement stmt;
        connect();
        if (!this.tableExists("Subscribers")) {
            // Table doesn't exist.
            try {
                stmt = connection.createStatement();
                String sql = "CREATE TABLE Subscribers " +
                        "(ID              INTEGER PRIMARY KEY AUTOINCREMENT   NOT NULL," +
                        " ChatID          INTEGER           NOT NULL," +
                        " AccName         NVARCHAR(100)     NOT NULL, " +
                        " FirstName       NVARCHAR(100)     NOT NULL, " +
                        " LastName        NVARCHAR(100)     NOT NULL, " +
                        " isSubscribed    BOOLEAN           NOT NULL, " +
                        " FirstSubDate    DATETIME, " +
                        " LastSubDate     DATETIME          NOT NULL, " +
                        " UnSubDate       DATETIME, " +
                        " LocationLAT     DOUBLE, " +
                        " LocationLON     DOUBLE, " +
                        " LocationCity    NVARCHAR(100), " +
                        " MessageTime     DATETIME)";
                stmt.executeUpdate(sql);
                stmt.close();
                connection.close();
            } catch (Exception e) {
                System.err.println(e.getClass().getName() + ": " + e.getMessage());
                System.exit(0);
            }
            System.out.println("Table created successfully.");
        } else {
            System.out.println("Table Exists!");
        }
    }

    public boolean tableExists(String tableName) {
        connect();
        try {
            DatabaseMetaData md = connection.getMetaData();
            ResultSet rs = md.getTables(null, null, tableName, null);
            rs.last();
            return rs.getRow() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }
}


