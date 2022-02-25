package org.zutjmx.hibernate.app;

import jakarta.persistence.EntityManager;
import org.zutjmx.hibernate.app.entity.Cliente;
import org.zutjmx.hibernate.app.util.JpaUtil;

import javax.swing.*;

public class HibernateCrear {
    public static void main(String[] args) {
        EntityManager entityManager = JpaUtil.getEntityManager();
        try {
            String nombre = JOptionPane.showInputDialog("Escribe el nombre:");
            String apellido = JOptionPane.showInputDialog("Escribe el apellido:");
            String formaPago = JOptionPane.showInputDialog("Escribe la forma de pago:");

            entityManager.getTransaction().begin();

            Cliente cliente = new Cliente();
            cliente.setNombre(nombre);
            cliente.setApellido(apellido);
            cliente.setFormaPago(formaPago);

            entityManager.persist(cliente);

            entityManager.getTransaction().commit();

            cliente = entityManager.find(Cliente.class,cliente.getId());
            System.out.println("Se insertó con éxito el cliente: " + cliente);

        } catch (Exception e) {
            entityManager.getTransaction().rollback();
            e.printStackTrace();
        } finally {
            entityManager.close();
        }

    }
}
