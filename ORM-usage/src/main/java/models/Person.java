package models;

import annotations.MyColumn;
import annotations.MyEntity;
import annotations.MyId;

import java.util.Date;

@MyEntity(tableName = "people_new", primaryKey = "id")
public class Person {

    @MyId
    private int id;

    @MyColumn(fieldName = "name")
    private String name;

    @MyColumn(fieldName = "birth_date")
    private Date birthDate;

    @MyColumn(fieldName = "age")
    private int age;

    @MyColumn(fieldName = "client_id")
    private int clientId;

    public Person(){

    }

    public Person(String name, Date birthDate, int age, int clientId) {
        this.name = name;
        this.birthDate = birthDate;
        this.age = age;
        this.clientId = clientId;
    }

    public Person(int id) {
        this.id = id;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Date getBirthDate() {
        return birthDate;
    }

    public void setBirthDate(Date birthDate) {
        this.birthDate = birthDate;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public int getClientId() {
        return clientId;
    }

    public void setClientId(int clientId) {
        this.clientId = clientId;
    }
}
