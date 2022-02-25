package org.zutjmx.hibernate.app;

import jakarta.persistence.EntityManager;
import org.zutjmx.hibernate.app.entity.Cliente;
import org.zutjmx.hibernate.app.util.JpaUtil;

import javax.swing.*;
import java.awt.*;

public class HibernateEliminar {
    public static void main(String[] args) {
        EntityManager entityManager = JpaUtil.getEntityManager();
        try {
            Long id = Long.valueOf(JOptionPane.showInputDialog("Escribe el ID del cliente a eliminar:"));
            Cliente cliente = entityManager.find(Cliente.class,id);

            if (cliente == null) {
                JOptionPane.showMessageDialog(null,"No existe el cliente con ID: " + id);
                return;
            }

            int resp = JOptionPane.showConfirmDialog(null,"¿Quieres eliminar el cliente "
                    + cliente
                    +"?");

            if (JOptionPane.OK_OPTION == resp){

                entityManager.getTransaction().begin();
                entityManager.remove(cliente);
                entityManager.getTransaction().commit();

                JOptionPane.showMessageDialog(null,"Se elimninó el cliente: " + cliente);
            }
            else{
                JOptionPane.showMessageDialog(null,"No se eliminó el cliente: "+ cliente);
            }

        } catch (Exception e) {
            entityManager.getTransaction().rollback();
            e.printStackTrace();
        } finally {
            entityManager.close();
        }
    }
}
