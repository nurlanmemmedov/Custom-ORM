package management;
import java.lang.reflect.Field;
import annotations.MyColumn;
import storages.DataTypes;
import storages.Entity;

public final class SQLBuilder {

    public static String buildCreateTableRequest(Entity entity) {

        String tableName = entity.tableName();
        String primaryKey = entity.primaryKey();

        StringBuilder SQLRequest = new StringBuilder("CREATE TABLE " + tableName
                + " (" + primaryKey + " serial, ");
        SQLRequest.append(buildEntityFieldLine(entity));
        SQLRequest.append("PRIMARY KEY ").append(" (").append(primaryKey).append(")");
        SQLRequest.append(")");
        return SQLRequest.toString();
    }


    private static String buildEntityFieldLine(Entity entity) {
        StringBuilder fieldLine = new StringBuilder();

        for (int i = 0; i < entity.getFieldsNames().size(); i++) {
            fieldLine.append(entity.getFieldsNames().get(i));
            fieldLine.append(" ").append(DataTypes.getInstance().getDataTypes().get(entity.getFieldTypes().get(i))).append(", ");
        }
        return fieldLine.toString();
    }


    public static String buildFieldValuesLine(Entity entity) {
        StringBuilder valuesLine = new StringBuilder();
        String columnName;
        Object fieldValue;

        for (Field parsedField : entity.getEntityClass().getDeclaredFields()) {
            if (parsedField.isAnnotationPresent(MyColumn.class)) {
                columnName = parsedField.getAnnotation(MyColumn.class).fieldName();

                try {
                    parsedField.setAccessible(true);
                    fieldValue = ((Object) parsedField.get(entity.getEntityObject()));
                    valuesLine.append(columnName + " = '" + fieldValue + "'" + ", ");
                } catch (IllegalArgumentException | IllegalAccessException e) {
                    e.printStackTrace();
                }
            }
        }
        return valuesLine.toString().substring(0, valuesLine.length() - 2);
    }

}