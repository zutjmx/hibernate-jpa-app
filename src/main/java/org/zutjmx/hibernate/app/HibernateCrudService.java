package org.zutjmx.hibernate.app;

import jakarta.persistence.EntityManager;
import org.zutjmx.hibernate.app.entity.Cliente;
import org.zutjmx.hibernate.app.services.ClienteService;
import org.zutjmx.hibernate.app.services.ClienteServiceImpl;
import org.zutjmx.hibernate.app.util.JpaUtil;

import java.util.List;
import java.util.Optional;

public class HibernateCrudService {
    public static void main(String[] args) {
        EntityManager entityManager = JpaUtil.getEntityManager();
        ClienteService clienteService = new ClienteServiceImpl(entityManager);

        System.out.println("<<<<::::>>>> Usando Crud y Service <<<<::::>>>>");
        System.out.println(":::: Listado de clientes ::::");
        List<Cliente> clientes = clienteService.listar();
        clientes.forEach(System.out::println);
        System.out.println("Total de clientes => "+clientes.stream().count());

        System.out.println(":::: Obtener por ID ::::");
        Optional<Cliente> clienteOptional = clienteService.porId(21L);
        /*if (clienteOptional.isPresent()) {
            System.out.println(clienteOptional.get());
        } else {
            System.out.println("No existe el cliente");
        }*/
        /*clienteOptional.ifPresent(cliente -> {
            System.out.println(clienteOptional.get());
        });*/
        clienteOptional.ifPresent(System.out::println);

        System.out.println(":::: Insertar nuevo cliente ::::");
        Cliente cliente = new Cliente();
        cliente.setNombre("Lucy");
        cliente.setApellido("In The Sky");
        cliente.setFormaPago("With Diamonds");
        clienteService.guardar(cliente);
        System.out.println(":::: Cliente "+cliente+" guardado con éxito ::::");

        System.out.println(":::: Listado de clientes de nuevo ::::");
        clienteService.listar().forEach(System.out::println);
        System.out.println("Total de clientes de nuevo => "+clienteService.listar().stream().count());

        System.out.println(":::: Editar cliente existente ::::");
        Long id = cliente.getId();
        clienteOptional = clienteService.porId(id);
        clienteOptional.ifPresent(clienteUpdate -> {
            clienteUpdate.setFormaPago("Shine on you crazy diamond");
            clienteService.guardar(clienteUpdate);
            System.out.println("Cliente " + clienteUpdate + " modificado con éxito.");
            System.out.println(":::: Listado de clientes de nuevo ::::");
            clienteService.listar().forEach(System.out::println);
        });

        System.out.println(":::: Eliminar cliente existente ::::");
        id = cliente.getId();
        clienteOptional = clienteService.porId(id);
        clienteOptional.ifPresent(clienteBorrar -> {
            clienteService.eliminar(clienteBorrar.getId());
            System.out.println("Cliente " + clienteBorrar + " eliminado con éxito.");
            System.out.println(":::: Listado de clientes de nuevo ::::");
            clienteService.listar().forEach(System.out::println);
        });

        entityManager.close();
    }
}
