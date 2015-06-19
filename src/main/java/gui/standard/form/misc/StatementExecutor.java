package gui.standard.form.misc;

import database.DBConnection;
import gui.standard.ColumnValue;

import javax.xml.datatype.XMLGregorianCalendar;
import java.math.BigDecimal;
import java.sql.*;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;

import static gui.standard.form.misc.QueryBuilder.Query.WhereTypesEnum.LIKE;
import static java.sql.Types.VARCHAR;

/**
 * Created by Nikola on 8.6.2015..
 */
public class StatementExecutor {

    private Map<String, String> columnCodeTypes;

    public StatementExecutor() {
    }

    public StatementExecutor(Map<String, String> columnCodeTypes) {
        this.columnCodeTypes = columnCodeTypes;
    }

    public void executeProcedure(String procedureCall, List<ColumnValue> columnValueValues) throws SQLException {
        System.out.println("Executing : \n" + procedureCall);

        CallableStatement statement = DBConnection.getConnection().prepareCall(procedureCall);

        int i = 1;
        for (ColumnValue columnValue : columnValueValues) {
            String columnTypeClass = columnCodeTypes.get(columnValue.getCode());
            Object value = columnValue.getValue();

            setValue(statement, columnTypeClass, i++, value, false);
        }

        statement.execute();
        statement.close();

        DBConnection.getConnection().commit();
    }

    public List<String[]> execute(String query, Collection<String> resultColumnCodes) throws SQLException {
        System.out.println("Executing : \n" + query);

        Statement statement = DBConnection.getConnection().createStatement();
        ResultSet resultSet = statement.executeQuery(query);

        return rowCallback(statement, resultSet, resultColumnCodes);
    }

    public List<String[]> execute(QueryBuilder.Query query, List<ColumnValue> columnValues,
                                  Collection<String> resultColumnCodes) throws SQLException {
        System.out.println("Executing : \n" + query.getQuery());

        PreparedStatement statement = DBConnection.getConnection().prepareStatement(query.getQuery());

        int i = 1;
        for (ColumnValue columnValue : columnValues) {
            String columnTypeClass = columnCodeTypes.get(columnValue.getCode());
            Object value = columnValue.getValue();
            setValue(statement, columnTypeClass, i, value, query.getWheres().get(i++ - 1) == LIKE);
        }

        ResultSet resultSet = statement.executeQuery();

        return rowCallback(statement, resultSet, resultColumnCodes);
    }

    private List<String[]> rowCallback(Statement statement, ResultSet resultSet,
                                       Collection<String> resultColumnCodes)
            throws SQLException {
        List<String[]> ret = new ArrayList<>();

        String[] rowValues = new String[resultColumnCodes.size()];
        while (resultSet.next()) {
            int count = 0;
            for (String columnName : resultColumnCodes) {
                rowValues[count++] = resultSet.getString(columnName);
            }

            ret.add(rowValues);
            rowValues = new String[resultColumnCodes.size()];
        }

        resultSet.close();
        statement.close();

        DBConnection.getConnection().commit();

        return ret;
    }

    private void setValue(PreparedStatement statement, String columnTypeClass, int i, Object value, boolean like)
            throws SQLException {
        if (like) {
            if (value == null || value.equals("")) {
                statement.setString(i, "%");
            } else {
                statement.setString(i, "%" + value + "%");
            }
        } else {
            if (value == null || value.equals("")) {
                statement.setNull(i, VARCHAR);
            } else {
                switch (columnTypeClass) {
                    case "java.lang.String":
                        statement.setString(i, (String) value);
                        break;
                    case "java.math.BigInteger":
                    case "java.math.BigDecimal":
                        if (value instanceof String) {
                            ((String)value).trim();
                            try {
                                statement.setInt(i, Integer.parseInt((String) value));
                            } catch (Exception e) {
                                try {
                                    statement.setDouble(i, Double.parseDouble((String) value));
                                } catch (Exception e1) {
                                        statement.setBigDecimal(i, new BigDecimal((String) value));
                                }
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
                            boolean boolValue = false;

                            switch ((String) value) {
                                case "true":
                                case "1":
                                    boolValue = true;
                                    break;
                                case "false":
                                case "0":
                                    boolValue = false;
                                    break;
                            }

                            statement.setBoolean(i, boolValue);
                        } else if (value instanceof Boolean) {
                            statement.setBoolean(i, (Boolean) value);
                        } else if (value instanceof Integer) {
                            statement.setBoolean(i, value.equals(1) ? true : false);
                        }

                        break;
                    case "java.sql.Date":
                        if (value instanceof String) {
                            SimpleDateFormat dateFormat = new SimpleDateFormat("dd.mm.yyyy.");
                            try {
                                java.util.Date date = dateFormat.parse((String) value);
                                Date sqlDate = new Date(date.getTime());
                                statement.setDate(i, sqlDate);
                            } catch (Exception e) {
                                try {
                                    dateFormat = new SimpleDateFormat("yyyy-");
                                    java.util.Date date = dateFormat.parse((String) value);
                                    Date sqlDate = new Date(date.getTime());
                                    statement.setDate(i, sqlDate);
                                } catch (ParseException e1) {
                                    e1.printStackTrace();
                                }
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
        }
    }
}
