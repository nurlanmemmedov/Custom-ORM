package management;
import annotations.MyColumn;
import annotations.MyId;
import connections.MyConnection;
import interfaces.IEntityManager;
import storages.Table;
import utils.Settings;
import storages.Entity;
import java.lang.reflect.Field;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class EntityManager implements AutoCloseable, IEntityManager {

    private static EntityManager instance;
    Connection connection;

    public EntityManager() {
        Settings settings = new Settings();
        // CREATE TABLES FROM XML CLASSES
        for (String className : settings.tableClasses){
            try {
                Table.createTableFromEntity(new Entity(Class.forName(className)));
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }
        }
        connection = (new MyConnection()).getConnection();
    }

    public static EntityManager getInstance() {

        if (instance == null)
            instance = new EntityManager();

        return instance;
    }

    @Override
    public int create(Entity entity) {
        int addedRecordId = -1;
        final String query = "INSERT INTO " + entity.tableName() + " (" + entity.getParsedFieldsLine() + ")"
                + " VALUES (" + entity.getParsedValuesLine() + ");";

        try (final PreparedStatement statement = connection.prepareStatement(query,
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

    @Override
    public void updateById(Entity entity, int value) {
        String preparedData = SQLBuilder.buildFieldValuesLine(entity);
        try {
            PreparedStatement statement = connection.prepareStatement("UPDATE " + entity.tableName()+"  SET " + preparedData + " WHERE "+ entity.primaryKey()+ " =  ?");
            statement.setInt(1, value);
            statement.executeUpdate();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }

    }

    @Override
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

    @Override
    public boolean deleteAll(Entity entity) {
        try {
            PreparedStatement statement = connection.prepareStatement("DELETE FROM " + entity.tableName());
            statement.executeUpdate();
            return true;
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return false;
    }

    @Override
    public List<Entity> getAll(Entity entity) {
        final String query = "SELECT * FROM " + entity.tableName() + " ORDER BY " + entity.primaryKey() + ";";
        List<Entity> entities = new ArrayList<>();
        try (final Statement statement = connection.createStatement();
             final ResultSet resultSet = statement.executeQuery(query)) {
            while (resultSet.next()) {
                Entity en = setFieldsValue(entity, resultSet, entity.primaryKey());
                entities.add(en);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return entities;
    }

    @Override
    public Entity find(Entity entity, int id) {
        String pk = entity.primaryKey();
        Entity localEntity = new Entity(entity);
        try {
            PreparedStatement statement = connection.prepareStatement("SELECT * FROM " + entity.tableName() + " WHERE " + pk + " = ?");
            statement.setInt(1, id);
            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                localEntity = setFieldsValue(entity, resultSet, pk);
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return localEntity;
    }

    public Entity setFieldsValue(Entity entity, ResultSet resultSet, String primaryKey) {
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

        } catch (SQLException throwables) {
            throwables.printStackTrace();
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