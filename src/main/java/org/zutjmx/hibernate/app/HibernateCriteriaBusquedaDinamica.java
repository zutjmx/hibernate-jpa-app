package org.zutjmx.hibernate.app;

import jakarta.persistence.EntityManager;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import org.zutjmx.hibernate.app.entity.Cliente;
import org.zutjmx.hibernate.app.util.JpaUtil;

import javax.swing.*;
import java.util.ArrayList;
import java.util.List;

public class HibernateCriteriaBusquedaDinamica {
    public static void main(String[] args) {

        String nombre = JOptionPane.showInputDialog("Filtrar por nombre: ");
        //System.out.println("Nombre: "+nombre);

        String apellido = JOptionPane.showInputDialog("Filtrar por apellido: ");
        //System.out.println("Apellido: "+apellido);

        String formaPago = JOptionPane.showInputDialog("Filtrar por forma de pago: ");
        //System.out.println("Forma de pago: "+formaPago);

        EntityManager entityManager = JpaUtil.getEntityManager();

        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        CriteriaQuery<Cliente> criteriaQuery = criteriaBuilder.createQuery(Cliente.class);
        Root<Cliente> from = criteriaQuery.from(Cliente.class);

        List<Predicate> condiciones = new ArrayList<>();

        if (nombre != null && !nombre.isBlank()) {
            condiciones.add(criteriaBuilder.equal(from.get("nombre"),nombre));
        }

        if (apellido != null && !apellido.isBlank()) {
            condiciones.add(criteriaBuilder.equal(from.get("apellido"),apellido));
        }

        if (formaPago != null && !formaPago.isBlank()) {
            condiciones.add(criteriaBuilder.equal(from.get("formaPago"),formaPago));
        }

        criteriaQuery
                .select(from)
                .where(
                        criteriaBuilder.and(
                                condiciones.toArray(new Predicate[condiciones.size()])
                        )
                );

        List<Cliente> clientes = entityManager
                .createQuery(criteriaQuery)
                .getResultList();

        clientes.forEach(System.out::println);

        entityManager.close();
    }
}
