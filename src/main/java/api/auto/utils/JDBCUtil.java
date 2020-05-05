package api.auto.utils;
import java.sql.*;
import java.util.*;

public class JDBCUtil {
    private static String url;
    private static String user;
    private static String password;

    static {
        try {
            Properties properties = new Properties();
            properties.load(JDBCUtil.class.getResourceAsStream("/jdbc.properties"));
            url = properties.getProperty("jdbc.url");
            user = properties.getProperty("jdbc.user");
            password = properties.getProperty("jdbc.password");

            String driver = properties.getProperty("jdbc.driver");
            Class.forName(driver);
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    private static Connection getConnection(){
        try {
            return DriverManager.getConnection(url,user,password);
        }catch (SQLException e){
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 执行增删改命令
     * @param sql
     * @param parameters
     */
    public static void executeSQL(String sql, String... parameters){
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        try {
            connection = getConnection();
            preparedStatement = connection.prepareStatement(sql);
            for(int i = 0;i<parameters.length;i++){
                preparedStatement.setString(i+1,parameters[i]);
            }
            preparedStatement.execute();

        }catch (SQLException e){
            e.printStackTrace();
        }finally {
            close(connection,preparedStatement);
        }
    }

    /**
     * 查询
     * @param sql
     * @param parameters
     * @return
     */
    public static List<LinkedHashMap<String,String>> query(String sql, String... parameters){
        //需要有序的结果,以便与预期结果进行比对
        List<LinkedHashMap<String,String>> allRecordsByQuery = null;
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        try {
            connection = getConnection();
            preparedStatement = connection.prepareStatement(sql);
            for(int i = 0;i<parameters.length;i++){
                preparedStatement.setString(i+1,parameters[i]);
            }
            resultSet = preparedStatement.executeQuery();
            ResultSetMetaData metaData = resultSet.getMetaData();
            int columnCount = metaData.getColumnCount();
            allRecordsByQuery = new ArrayList<>();
            while (resultSet.next()){
                LinkedHashMap<String,String> recordMap = new LinkedHashMap<>();
                for(int i = 1;i <= columnCount;i++){
                    //别名
                    String columnName = metaData.getColumnLabel(i);
                    String value = resultSet.getString(i);
                    recordMap.put(columnName,value);
                }
                allRecordsByQuery.add(recordMap);
            }

        }catch (SQLException e){
            e.printStackTrace();
        }finally {
            close(connection,preparedStatement,resultSet);
        }
        return allRecordsByQuery;
    }

    private static void close(Connection connection, PreparedStatement preparedStatement, ResultSet resultSet) {
        close(connection,preparedStatement);
        if(resultSet != null){
            try {
                resultSet.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    private static void close(Connection connection, PreparedStatement preparedStatement) {
        if(connection != null){
            try {
                connection.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        if(preparedStatement != null){
            try {
                preparedStatement.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }

    }
}
