package test;

import annotations.MyColumn;
import annotations.MyEntity;
import annotations.MyId;

import java.util.Date;

@MyEntity(tableName = "test", primaryKey = "id")
public class TestModel {

    @MyId
    private int id;

    @MyColumn(fieldName = "test_field")
    private String field;

    @MyColumn(fieldName = "test_sex")
    private boolean sex;

    @MyColumn(fieldName = "birth_date")
    private Date birthDate;

    @MyColumn(fieldName = "age")
    private int age;

    @MyColumn(fieldName = "client_id")
    private int clientId;

    public TestModel(String field, boolean sex, Date birthDate, int age, int clientId) {
        this.field = field;
        this.sex = sex;
        this.birthDate = birthDate;
        this.age = age;
        this.clientId = clientId;
    }

    public TestModel() {
    }

    public TestModel(String field) {
        this.field = field;
    }

    public TestModel(int id) {
        this.id = id;
    }

    public TestModel(String field, Date birthDate) {
        super();
        this.field = field;
        this.birthDate = birthDate;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((field == null) ? 0 : field.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        TestModel other = (TestModel) obj;
        if (field == null) {
            if (other.field != null)
                return false;
        } else if (!field.equals(other.field))
            return false;
        return true;
    }

    public TestModel(String field, int age){
        this.field = field;
        this.age = age;
    }

    public String getField() {
        return field;
    }

    public void setField(String field) {
        this.field = field;
    }

    public boolean isSex() {
        return sex;
    }

    public void setSex(boolean sex) {
        this.sex = sex;
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
}