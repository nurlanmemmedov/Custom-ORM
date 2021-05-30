import management.EntityManager;
import models.Person;
import storages.Entity;

import java.lang.reflect.Field;
import java.sql.SQLException;
import java.util.Date;

public class Main {
    public static void main(String[] args) throws SQLException, IllegalAccessException {
        Person model1 = new Person("Nurlan", new Date(), 1, 1);
        Person model2 = new Person("Nurlan1", new Date(), 1, 1);
        Person model3 = new Person("Nurlan2", new Date(), 1, 1);
        Entity entity1 = new Entity(model1);
        Entity entity2 = new Entity(model2);
        Entity entity3 = new Entity(model3);

        EntityManager entityManager = new EntityManager();
        entityManager.create(entity1);
        entityManager.create(entity2);
        entityManager.create(entity3);
        entity1 = new Entity(new Person("Rafet",  new Date(), 1, 1));


        Object object = new EntityManager().find(new Entity(Person.class), 1);
        for(Entity entit : entityManager.getAll(new Entity(Person.class))){
            for (Field field : entit.getEntityObject().getClass().getDeclaredFields()){
                field.setAccessible(true);
                System.out.println(field.get(entit.getEntityObject()));
            }
        }

        entityManager.updateById(new Entity(new Person("Rafet",  new Date(), 1,2)), 13);
    }
}
