package model.db;

public class SchemaColumn {

    private String name;
    private String code;
    private boolean primaryKey;
    private boolean foreignKey;
    private String referencedTableName;
    private String tableName;

    private Class<?> clazz;

    public SchemaColumn(String tableName, String name, String code,Class<?> clazz, boolean primaryKey, boolean foreignKey) {
        this.tableName = tableName;
        this.name = name;
        this.code = code;
        this.clazz = clazz;
        this.primaryKey = primaryKey;
        this.foreignKey = foreignKey;
    }

    public String getTableName() {
        return tableName;
    }

    public void setTableName(String tableName) {
        this.tableName = tableName;
    }

    public String getReferencedTableName() {
        return referencedTableName;
    }

    public void setReferencedTableName(String referencedTableName) {
        this.referencedTableName = referencedTableName;
    }

    public String getCode() {
        return code;
    }

    public String getName() {
        return name;
    }

    public boolean isPrimaryKey() {
        return primaryKey;
    }

    public boolean isForeignKey() {
        return foreignKey;
    }

    public Class<?> getClazz() {
        return clazz;
    }

    public void setClazz(Class<?> clazz) {
        this.clazz = clazz;
    }

    @Override
    public String toString() {
        return "\nColumn{" +
                "name='" + name + '\'' +
                ", code='" + code + '\'' +
                ", primaryKey=" + primaryKey +
                ", foreignKey=" + foreignKey +
                ", referencedTableName='" + referencedTableName + '\'' +
                '}';
    }
}
