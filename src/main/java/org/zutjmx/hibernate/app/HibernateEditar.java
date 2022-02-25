package org.zutjmx.hibernate.app;

import jakarta.persistence.EntityManager;
import org.zutjmx.hibernate.app.entity.Cliente;
import org.zutjmx.hibernate.app.util.JpaUtil;

import javax.swing.*;
import java.awt.*;

public class HibernateEditar {
    public static void main(String[] args) {
        EntityManager entityManager = JpaUtil.getEntityManager();

        try {
            Long id = Long.valueOf(JOptionPane.showInputDialog("Escribe el ID del cliente a modificar:"));
            Cliente cliente = entityManager.find(Cliente.class,id);

            String nombre = JOptionPane.showInputDialog("Escribe el nombre:", cliente.getNombre());
            String apellido = JOptionPane.showInputDialog("Escribe el apellido:", cliente.getApellido());
            String formaPago = JOptionPane.showInputDialog("Escribe la forma de pago:", cliente.getFormaPago());

            entityManager.getTransaction().begin();

            cliente.setNombre(nombre);
            cliente.setApellido(apellido);
            cliente.setFormaPago(formaPago);

            entityManager.merge(cliente);

            entityManager.getTransaction().commit();
            System.out.println("Se actualizó con éxito el cliente: " + cliente);

        } catch (HeadlessException e) {
            entityManager.getTransaction().rollback();
            e.printStackTrace();
        } finally {
            entityManager.close();
        }

    }
}
