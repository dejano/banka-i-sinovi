package gui.standard.form.misc;

import database.DBConnection;
import gui.standard.Column;

import java.sql.*;
import java.util.*;

/**
 * Created by Nikola on 8.6.2015..
 */
public class StatementExecutor {

    private Map<String, String> columnCodeTypes;
    private boolean fuzzy;

    public StatementExecutor(Map<String, String> columnCodeTypes) {
        this.columnCodeTypes = columnCodeTypes;
    }

    public void executeProcedure(String procedureCall, List<Column> columnValues) throws SQLException {
        System.out.println("Executing : \n" + procedureCall);

        CallableStatement statement = DBConnection.getConnection().prepareCall(
                procedureCall);

        int i = 1;
        for (Column column : columnValues) {
            String columnTypeClass = columnCodeTypes.get(column.getName());
            System.out.println(column.getName()+":" + column.getValue());
            String value = column.getValue();
            switch (columnTypeClass) {
                case "java.lang.String":
                    statement.setString(i, value);
                    break;
                case "java.math.BigDecimal":
                    try {
                        statement.setInt(i, Integer.parseInt(value));
                    } catch (NumberFormatException e) {
                        statement.setDouble(i, Double.parseDouble(value));
                    }
                    break;
            }

            i++;
        }

        statement.execute();
        statement.close();

        DBConnection.getConnection().commit();
    }

    public List<String[]> execute(String query, Set<String> columnCodes) throws SQLException {
        System.out.println("Executing : \n" + query);

        Statement statement = DBConnection.getConnection().createStatement();
        ResultSet resultSet = statement.executeQuery(query);

        return rowCallback(statement, resultSet, columnCodes);
    }

    public List<String[]> execute(String query, Set<String> columnCodes, Map<String, String> columnCodeValues) throws SQLException {
        System.out.println("Executing : \n" + query);

        PreparedStatement statement = DBConnection.getConnection().prepareStatement(query);

        int i = 1;
        for (String columnCode : columnCodeValues.keySet()) {
            String columnTypeClass = columnCodeTypes.get(columnCode);
            String value = columnCodeValues.get(columnCode);
            System.out.println(columnCode+":"+value);
            if (value.equals("")) {
                statement.setString(i, "%");
            } else {
                switch (columnTypeClass) {
                    case "java.lang.String":
                        if (fuzzy)
                            value = "%" + value + "%";

                        statement.setString(i, value);
                        break;
                    case "java.math.BigDecimal":
                        try {
                            statement.setInt(i, Integer.parseInt(value));
                        } catch (NumberFormatException e) {
                            statement.setDouble(i, Double.parseDouble(value));
                        }
                        break;
                }
            }
            i++;
        }

        ResultSet resultSet = statement.executeQuery();

        return rowCallback(statement, resultSet, columnCodes);
    }


    private List<String[]> rowCallback(Statement statement, ResultSet resultSet, Set<String> columnCodes) throws SQLException {
        List<String[]> ret = new ArrayList<>();

        String[] rowValues = new String[columnCodes.size()];
        while (resultSet.next()) {
            int count = 0;
            for (String columnName : columnCodes) {
                rowValues[count++] = resultSet.getString(columnName);
            }

            ret.add(rowValues);
            rowValues = new String[columnCodes.size()];
        }

        resultSet.close();
        statement.close();

        DBConnection.getConnection().commit();

        return ret;
    }

    public void setFuzzy(boolean fuzzy) {
        this.fuzzy = fuzzy;
    }
}
