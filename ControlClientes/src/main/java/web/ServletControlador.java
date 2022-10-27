package web;

import datos.ClienteDAOJDBC;
import dominio.Cliente;
import java.io.IOException;
import java.sql.SQLException;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.*;

@WebServlet("/ServletControlador")
public class ServletControlador extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String accion = request.getParameter("accion");
        try {
            if (accion != null) {
                switch (accion) {
                    case "editar":
                        this.editarCliente(request, response);
                        break;
                    case "eliminar":
                        this.eliminarCliente(request, response);
                        break;
                    default:
                        this.accionDefault(request, response);
                }
            } else {
                this.accionDefault(request, response);
            }
        } catch (SQLException sqlEx) {
            System.out.println(sqlEx.getMessage());
        }
    }

    //Muestra los datos en pantalla y lleva  a la pagina principal
    private void accionDefault(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException, SQLException {
        List<Cliente> clientes = new ClienteDAOJDBC().listar();
        HttpSession sesion = request.getSession();
        sesion.setAttribute("clientes", clientes);
        sesion.setAttribute("totalClientes", clientes.size());
        sesion.setAttribute("saldoTotal", this.calcularSaldoTotal(clientes));
        //request.getRequestDispatcher("clientes.jsp").forward(request, response);
        response.sendRedirect("clientes.jsp");
    }

    private double calcularSaldoTotal(List<Cliente> clientes) {
        double saldoTotal = 0;
        for (Cliente cliente : clientes) {
            saldoTotal += cliente.getSaldo();
        }
        return saldoTotal;
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String accion = request.getParameter("accion");
        try {
            if (accion != null) {
                switch (accion) {
                    case "insertar":
                        this.insertarCliente(request, response);
                        break;
                    case "modificar":
                        this.modificarCliente(request, response);
                        break;
                    default:
                        this.accionDefault(request, response);
                }
            } else {
                this.accionDefault(request, response);
            }
        } catch (SQLException sqlEx) {
            System.out.println(sqlEx.getMessage());
        }

    }

    private void insertarCliente(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        try {
            //Recuperando los valores del formulario editarCliente
            String nombre = request.getParameter("nombre");
            String apellido = request.getParameter("apellido");
            String email = request.getParameter("email");
            String telefono = request.getParameter("telefono");
            double saldo = 0;
            String saldoString = request.getParameter("saldo");
            if (saldoString != null && !"".equals(saldoString)) {
                saldo = Double.parseDouble(saldoString);
            }
            //Creando cliente capa de datos
            ClienteDAOJDBC clienteDAO = new ClienteDAOJDBC();

            //Insertando nuevo cliente y recuperando filas afectadas
            int rows = clienteDAO.insertar(new Cliente(nombre, apellido, email, telefono, saldo));
            System.out.println("rows = " + rows);

            //Redirigiendo a acción default - actualizar info y redireccionar a pagina principal
            this.accionDefault(request, response);
        } catch (SQLException ex) {
            Logger.getLogger(ServletControlador.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private void editarCliente(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException, SQLException {
        int idCliente = Integer.parseInt(request.getParameter("idCliente"));
        ClienteDAOJDBC clienteDAO = new ClienteDAOJDBC();
        Cliente cliente = clienteDAO.encontrar(new Cliente(idCliente));
        request.setAttribute("cliente", cliente);
        String jspEditar = "/WEB-INF/paginas/cliente/editarCliente.jsp";
        request.getRequestDispatcher(jspEditar).forward(request, response);
    }

    private void modificarCliente(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException, SQLException {
        //Recuperando los valores del formulario editarCliente
        int idCliente = Integer.parseInt(request.getParameter("idCliente"));
        String nombre = request.getParameter("nombre");
        String apellido = request.getParameter("apellido");
        String email = request.getParameter("email");
        String telefono = request.getParameter("telefono");
        double saldo = 0;
        String saldoString = request.getParameter("saldo");
        //Validando saldo
        if (saldoString != null && !"".equals(saldoString)) {
            saldo = Double.parseDouble(saldoString);
        }
        //Creando cliente capa de datos
        ClienteDAOJDBC clienteDAO = new ClienteDAOJDBC();
        //Modificando nuevo cliente y recuperando filas afectadas
        int rows = clienteDAO.actualizar(new Cliente(idCliente, nombre, apellido, email, telefono, saldo));
        System.out.println("rows = " + rows);

        //Redirigiendo a acción default - actualizar info y redireccionar a pagina principal
        this.accionDefault(request, response);
    }

    private void eliminarCliente(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException, SQLException {
        //Recuperando los valores del formulario editarCliente
        int idCliente = Integer.parseInt(request.getParameter("idCliente"));
        Cliente clienteEliminar = new Cliente(idCliente);
        //Creando cliente capa de datos
        ClienteDAOJDBC clienteDAO = new ClienteDAOJDBC();
        //Eliminando nuevo cliente y recuperando filas afectadas
        int rows = clienteDAO.eliminar(clienteEliminar);
        System.out.println("rows = " + rows);

        //Redirigiendo a acción default - actualizar info y redireccionar a pagina principal
        this.accionDefault(request, response);
    }
    
}
