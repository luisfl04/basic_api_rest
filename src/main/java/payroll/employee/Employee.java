package payroll.employee;

import java.util.Objects;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;


@Entity
public class Employee {

    private @Id @GeneratedValue Long id;
    private String first_name;
    private String last_name;
    private String cargo;

    Employee() {}

    Employee(String first_name, String last_name, String cargo) {
        this.first_name = first_name;
        this.last_name = last_name;
        this.cargo = cargo;
    }

    public Long getId() {
        return id;
    }

    public String getNome() {
        return this.first_name + " " + this.last_name ;
    }

    public String getFirstName() {
        return this.first_name;
    }

    public String getLastName() {
        return this.last_name;
    }

    public String getCargo() {
        return cargo;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setNome(String nome) {
        String[] name_parts = nome.split(" ");
        this.first_name = name_parts[0];
        this.last_name = name_parts[1];
    }

    public void setFirstName(String firstName) {
        this.first_name = firstName;
    }

    public void setLastName(String lastName) {
        this.last_name = lastName;
    }

    public void setCargo(String cargo) {
        this.cargo = cargo;
    }

    @Override
    public boolean equals(Object abstract_object) {
        if(this == abstract_object){
            return true;
        }
        if(! (abstract_object instanceof Employee)){
            return false;
        }

        Employee employee = (Employee) abstract_object;

        return Objects.equals(this.id, employee.id) &&
                Objects.equals(this.first_name, employee.first_name) &&
                Objects.equals(this.last_name, employee.last_name) &&
                Objects.equals(this.cargo, employee.cargo);
    }

    @Override
    public int hashCode(){
        return Objects.hash(this.id, this.first_name, this.last_name, this.cargo);
    }

    @Override
    public String toString(){
        return "Employee -> {id: " + this.id + ", primeiro nome: " + this.first_name + ", sobrenome:" + this.last_name + ", cargo: " + this.cargo + "}";
    }


}
