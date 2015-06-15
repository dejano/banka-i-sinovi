package gui.standard.form.misc;

import database.DBConnection;
import gui.standard.Column;

import javax.xml.datatype.XMLGregorianCalendar;
import java.math.BigDecimal;
import java.sql.*;
import java.sql.Date;
import java.text.ParseException;
import java.text.SimpleDateFormat;
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
            Object value = column.getValue();

            setValue(statement, columnTypeClass, i++, value);
        }

        statement.execute();
        statement.close();

        DBConnection.getConnection().commit();
    }

    public List<String[]> execute(String query, Collection<String> columnCodes) throws SQLException {
        System.out.println("Executing : \n" + query);

        Statement statement = DBConnection.getConnection().createStatement();
        ResultSet resultSet = statement.executeQuery(query);

        return rowCallback(statement, resultSet, columnCodes);
    }

    public List<String[]> execute(String query, Collection<String> columnCodes,
                                  Map<String, String> columnCodeValues) throws SQLException {
        System.out.println("Executing : \n" + query);

        PreparedStatement statement = DBConnection.getConnection().prepareStatement(query);

        int i = 1;
        for (String columnCode : columnCodeValues.keySet()) {
            String columnTypeClass = columnCodeTypes.get(columnCode);
            String value = columnCodeValues.get(columnCode);
            System.out.println(columnCode + ":" + value);
            if (value.equals("")) {
                statement.setString(i, "%");
            } else {
                statement.setString(i, "%" + value + "%");
            }
            i++;
        }

        ResultSet resultSet = statement.executeQuery();

        return rowCallback(statement, resultSet, columnCodes);
    }

    private List<String[]> rowCallback(Statement statement, ResultSet resultSet, Collection<String> columnCodes)
            throws SQLException {
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

    private void setValue(PreparedStatement statement, String columnTypeClass, int i, Object value)
            throws SQLException {
        switch (columnTypeClass) {
            case "java.lang.String":
                statement.setString(i, (String) value);
                break;
            case "java.math.BigDecimal":
                if (value instanceof String) {
                    try {
                        statement.setInt(i, Integer.parseInt((String) value));
                    } catch (NumberFormatException e) {
                        statement.setDouble(i, Double.parseDouble((String) value));
                    }
                } else if (value instanceof Integer) {
                    statement.setInt(i, (Integer) value);
                } else if (value instanceof Double) {
                    statement.setDouble(i, (Double) value);
                } else if (value instanceof BigDecimal) {
                    statement.setBigDecimal(i, (BigDecimal) value);
                }

                break;
            case "java.lang.Boolean":
                if (value instanceof String) {
                    try {
                        statement.setBoolean(i, Boolean.parseBoolean((String) value));
                    } catch (NumberFormatException e) {
                        int intValue = Integer.parseInt((String) value);
                        statement.setBoolean(i, intValue == 1 ? true : false);
                    }
                } else if (value instanceof Boolean) {
                    statement.setBoolean(i, (Boolean) value);
                } else if (value instanceof Integer) {
                    statement.setBoolean(i, value.equals(1) ? true : false);
                }

                break;
            case "java.sql.Date":
                if (value instanceof String) {
                    SimpleDateFormat dateFormat = new SimpleDateFormat("dd-mm-yyyy");
                    try {
                        java.util.Date date = dateFormat.parse((String) value);
                        Date sqlDate = new Date(date.getTime());
                        statement.setDate(i, sqlDate);
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                } else if (value instanceof java.util.Date) {
                    Date sqlDate = new Date(((java.util.Date) value).getTime());
                    statement.setDate(i, sqlDate);
                } else if (value instanceof Date) {
                    statement.setDate(i, (Date) value);
                } else if (value instanceof XMLGregorianCalendar) {
                    java.util.Date date = ((XMLGregorianCalendar) value).toGregorianCalendar().getTime();
                    Date sqlDate = new Date(date.getTime());
                    statement.setDate(i, sqlDate);
                }

                break;
        }
    }

    public void setFuzzy(boolean fuzzy) {
        this.fuzzy = fuzzy;
    }
}
