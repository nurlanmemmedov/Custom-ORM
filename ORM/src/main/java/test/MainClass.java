package test;
import management.EntityManager;
import storages.Entity;

import java.sql.SQLException;
import java.util.Date;

public class MainClass {
    public static void main(String[] args) throws SQLException, IllegalAccessException {
        TestModel model1 = new TestModel("Nurlan", false, new Date(), 1, 1);
        TestModel model2 = new TestModel("Nurlan1", false, new Date(), 1, 1);
        TestModel model3 = new TestModel("Nurlan2", false, new Date(), 1, 1);
/*
        Entity entity1 = new Entity(model1);
        Entity entity2 = new Entity(model2);
        Entity entity3 = new Entity(model3);*/

        EntityManager entityManager = new EntityManager();
/*        entityManager.createRecordInTable(entity1);
        entityManager.createRecordInTable(entity2);
        entityManager.createRecordInTable(entity3);*/
/*
        entity1 = new Entity(new TestModel("Rafet", false, new Date(), 1, 1));
*/
/*        Object object = new EntityManager().find(new Entity(TestModel.class), 1);
        for(Entity entity1 : entityManager.readAllRecordsOrderedByPK(new Entity(TestModel.class))){
            for (Field field : entity1.getEntityObject().getClass().getDeclaredFields()){
                field.setAccessible(true);
                System.out.println(field.get(entity1.getEntityObject()));
            }
        }*/

        entityManager.updateById(new Entity(new TestModel("Rafet", true, new Date(), 1,2)), 13);
    }
}
