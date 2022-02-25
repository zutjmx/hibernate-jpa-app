package org.zutjmx.hibernate.app;

import com.github.javafaker.Faker;
import jakarta.persistence.EntityManager;
import org.zutjmx.hibernate.app.entity.Cliente;
import org.zutjmx.hibernate.app.util.JpaUtil;

import java.util.Locale;

public class MiFaker {
    public static void main(String[] args) {
        //Faker faker = new Faker();
        Faker faker = new Faker(new Locale("es-MX"));

        System.out.println(":: Se van a generar datos por medio de javafaker ::");
        System.out.println(":: https://github.com/DiUS/java-faker ::");

        EntityManager entityManager = JpaUtil.getEntityManager();

        try {
            entityManager.getTransaction().begin();

            for (int i = 0; i < 10; i++) {
                String name = faker.name().fullName(); // Miss Samanta Schmidt
                String firstName = faker.name().firstName(); // Emory
                String lastName = faker.name().lastName(); // Barton
                String streetAddress = faker.address().streetAddress(); // 60018 Sawayn Brooks Suite 449
                String formaPago = faker.currency().name();

                System.out.println("name: " + name);
                System.out.println("firstName: " + firstName);
                System.out.println("lastName: " + lastName);
                System.out.println("streetAddress: " + streetAddress);
                System.out.println("formaPago: " + formaPago);
                System.out.println(":::::::::::::::::::::::::::::::::");

                Cliente cliente = new Cliente();
                cliente.setNombre(firstName);
                cliente.setApellido(lastName);
                cliente.setFormaPago(formaPago);

                entityManager.persist(cliente);

            }

            entityManager.getTransaction().commit();
        } catch (Exception e) {
            entityManager.getTransaction().rollback();
            e.printStackTrace();
        } finally {
            entityManager.close();
        }

        System.out.println(":: Se concluye proceso de inserción de datos Faker ::");

    }
}
