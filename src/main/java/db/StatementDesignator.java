package db;

import com.google.common.collect.ListMultimap;
import model.db.SchemaColumn;

import java.math.BigDecimal;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Collection;
import java.util.List;

public class StatementDesignator {

    public void designate(List<SchemaColumn> schemaColumns, List<String> values, PreparedStatement statement) throws SQLException {
        if (schemaColumns.size() != values.size()) {
            throw new IllegalArgumentException("First two params must have equal size. Size of given params are " + schemaColumns.size() + " != " + values.size());
        }
        System.out.println("Values ----------->");
        for (int i = 1; i <= schemaColumns.size(); i++) {
            Class clazz = schemaColumns.get(i - 1).getClazz();
            System.out.println("(" + clazz.getName() + ")" + values.get(i - 1));
            if (!clazz.isAssignableFrom(String.class)) {
                if (clazz.isAssignableFrom(BigDecimal.class)) {
                    statement.setBigDecimal(i, BigDecimal.valueOf(Double.valueOf(values.get(i - 1))));
                } else if (clazz.isAssignableFrom(Double.class)) {
                    statement.setDouble(i, Double.valueOf(values.get(i - 1)));
                } else if (clazz.isAssignableFrom(Integer.class)) {
                    statement.setInt(i, Integer.parseInt(values.get(i - 1)));
                }
            } else {
                statement.setString(i, String.valueOf(values.get(i - 1)));
            }
        }
        System.out.println("<----------- Values");
    }

    public void designate(List<SchemaColumn> schemaColumns, Collection<String> values, PreparedStatement statement) throws SQLException {
        if (schemaColumns.size() != values.size()) {
            throw new IllegalArgumentException("First two params must have equal size. Size of given params are " + schemaColumns.size() + " != " + values.size());
        }
        System.out.println("Values ----------->");
        String[] vals = values.toArray(new String[values.size()]);
        for (int i = 1; i <= schemaColumns.size(); i++) {
            Class clazz = schemaColumns.get(i - 1).getClazz();
            System.out.println("(" + clazz.getName() + ")" + vals[i - 1]);
            if (!clazz.isAssignableFrom(String.class)) {
                if (clazz.isAssignableFrom(BigDecimal.class)) {
                    statement.setBigDecimal(i, BigDecimal.valueOf(Double.valueOf(vals[i - 1])));
                } else if (clazz.isAssignableFrom(Double.class)) {
                    statement.setDouble(i, Double.valueOf(vals[i - 1]));
                } else if (clazz.isAssignableFrom(Integer.class)) {
                    statement.setInt(i, Integer.parseInt(vals[i - 1]));
                }
            } else {
                statement.setString(i, String.valueOf(vals[i - 1]));
            }
        }
        System.out.println("<----------- Values");
    }

    @Deprecated
    public PreparedStatement designate(ListMultimap<Class, String> schemaColumns, PreparedStatement statement) throws SQLException {
        return this.designate(schemaColumns.keySet(), schemaColumns.values(), statement);
    }

    @Deprecated
    public PreparedStatement designate(Collection<Class> clazzes, Collection<String> values, PreparedStatement statement) throws SQLException {
        Object[] clazzesArray = clazzes.toArray();
        Object[] valuesArray = values.toArray();

        for (int i = 1; i <= clazzesArray.length; i++) {
            Class clazz = (Class) clazzesArray[i - 1];
            String value = (String) valuesArray[i - 1];
            System.out.println(clazz.getName() + ":" + valuesArray[i - 1]);
            if (!clazz.isAssignableFrom(String.class)) {
                if (clazz.isAssignableFrom(BigDecimal.class)) {
                    statement.setBigDecimal(i, BigDecimal.valueOf(Double.valueOf(value)));
                } else if (clazz.isAssignableFrom(Double.class)) {
                    statement.setDouble(i, Double.valueOf(value));
                } else if (clazz.isAssignableFrom(Integer.class)) {
                    statement.setInt(i, Integer.parseInt(value));
                }
            } else {
                statement.setString(i, String.valueOf(valuesArray[i - 1]));
            }
        }
        return statement;
    }
}
