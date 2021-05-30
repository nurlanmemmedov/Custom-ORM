package management;
import annotations.MyColumn;
import annotations.MyId;
import connections.MyConnection;
import utils.Settings;
import storages.Entity;
import java.lang.reflect.Field;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class EntityManager implements AutoCloseable {

    private static EntityManager instance;
    Connection connection;

    public EntityManager() {
        Settings settings = new Settings();
        for (String className : settings.tableClasses){

        }
        connection = (new MyConnection()).getConnection();
    }

    public static EntityManager getInstance() {

        if (instance == null)
            instance = new EntityManager();

        return instance;
    }

    public int createRecordInTable(Entity entity) {
        int addedRecordId = -1;
        final String TABLE_NAME = entity.tableName().toLowerCase();
        final String QUERY_CREATE_ON_TABLE = "INSERT INTO " + TABLE_NAME + " (" + entity.getParsedFieldsLine() + ")"
                + " VALUES (" + entity.getParsedValuesLine() + ");";

        try (final PreparedStatement statement = connection.prepareStatement(QUERY_CREATE_ON_TABLE,
                Statement.RETURN_GENERATED_KEYS)) {
            statement.executeUpdate();
            try (ResultSet generatedKeys = statement.getGeneratedKeys()) {
                if (generatedKeys.next()) {

                    addedRecordId = generatedKeys.getInt(1);
                } else {
                    throw new IllegalStateException("Could not return PK of added client!");
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return addedRecordId;
    }

    public void updateById(Entity entity, int value) {
        String preparedData = SQLBuilder.buildFieldValuesLine(entity);
        try {
            PreparedStatement statement = connection.prepareStatement("UPDATE " + entity.tableName()+"  SET " + preparedData + " WHERE "+ entity.primaryKey()+ " =  ?");
            System.out.println(statement);
            statement.setInt(1, value);
            statement.executeUpdate();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }

    }

    public boolean deleteById(Entity entity, int value) {
        try {
            PreparedStatement statement = connection.prepareStatement("DELETE FROM " + entity.tableName()+"  WHERE "+ entity.primaryKey()+ " =  ?");
            statement.setInt(1, value);
            statement.executeUpdate();
            return true;
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return false;
    }


    public boolean deleteAllRecordsInTable(Entity entity) {
        try {
            PreparedStatement statement = connection.prepareStatement("DELETE FROM " + entity.tableName());
            statement.executeUpdate();
            return true;
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return false;
    }

    public List<Entity> readAllRecordsOrderedByPK(Entity entity) {

        final String TABLE_NAME = entity.tableName();
        String PK_NAME = entity.primaryKey();

        final String QUERY_READ_FROM_TABLE = "SELECT * FROM " + TABLE_NAME + " ORDER BY " + PK_NAME + ";";

        List<Entity> entities = new ArrayList<>();

        try (final Statement statement = connection.createStatement();
             final ResultSet resultSet = statement.executeQuery(QUERY_READ_FROM_TABLE)) {
            while (resultSet.next()) {
                Entity en = setFieldsValue(entity, resultSet, PK_NAME);
                entities.add(en);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return entities;
    }

    public Entity find(Entity entity, int id) {
        final String TABLE_NAME = entity.tableName();
        String PK_NAME = entity.primaryKey();
        Entity localEntity = new Entity(entity);

        String QUERY_SELECT_BY_ID = "SELECT * FROM " + TABLE_NAME + " WHERE " + PK_NAME + " = " + id;
        try (final Statement statement = connection.createStatement();
             final ResultSet resultSet = statement.executeQuery(QUERY_SELECT_BY_ID)) {
            while (resultSet.next()) {
                localEntity = setFieldsValue(entity, resultSet, PK_NAME);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return localEntity;
    }

    public Entity setFieldsValue(Entity entity, ResultSet resultSet, String primaryKey) throws SQLException {
        Entity localEntity = new Entity(entity.getEntityClass());
        try {
            for (Field parsedField : entity.getEntityClass().getDeclaredFields()) {
                parsedField.setAccessible(true);
                if (parsedField.isAnnotationPresent(MyId.class)) {
                    parsedField.set(localEntity.getEntityObject(), resultSet.getInt(primaryKey));
                } else if (parsedField.isAnnotationPresent(MyColumn.class)) {
                    final String COLUMN_NAME = parsedField.getAnnotation(MyColumn.class).fieldName();
                    parsedField.set(localEntity.getEntityObject(), resultSet.getObject(COLUMN_NAME));
                }
            }
        } catch (IllegalArgumentException | IllegalAccessException e) {
            e.printStackTrace();

        }
        return localEntity;
    }



    @Override
    public void close() throws Exception {
        try {
            connection.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

}