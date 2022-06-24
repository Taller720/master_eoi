package servlet;

import DTO.BookDetalle;
import DTO.LibrosLeidos;
import dao.BooksDao;

import javax.naming.NamingException;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.ExecutionException;

public class Libros extends HttpServlet {
    int accesses = 0;
    private  List<BookDetalle> listacarrito;
    public void doGet(HttpServletRequest request, HttpServletResponse response)
            throws IOException, ServletException {
        String action = request.getServletPath();
        System.out.println("accion:" + action );
        if (request.getParameter("opt") == null || request.getParameter("opt") == ""){
            //Esta será la accion ejecutada por defecto !!!!!!!!!!!!!!!!!

            //Comprobamos que la sesion coincide y en caso afirmativo recumepamos el usuario
            if ( utils.token.ValidarSesion(request)) {
                //Quiero leer de usuariosdo mediante el método MostrarLibrosLeidos la lista de libros
                List<DTO.BookDetalle> lista = null;

                BooksDao booksDao = new BooksDao();
                try {
                    lista = booksDao.MostrarLibros();

                } catch (SQLException e) {
                    e.printStackTrace();
                }
                //Preparar la salida para invocar al jsp
                request.setAttribute("listaLibros", lista);
                RequestDispatcher requestDispatcher = request.getRequestDispatcher("/jsp/mostrarlibrosvender.jsp");
                requestDispatcher.forward(request, response);
            }
            else {
                RequestDispatcher requestDispatcher = request.getRequestDispatcher("/jsp/login.jsp");
                requestDispatcher.forward(request, response);
            }
        } else {
            String option = request.getParameter("opt");
            if(Objects.equals(option, "comprar")) {
                try {
                    System.out.println("option1 :" + option);
                    AnadirElemAlCarrito(request, response);
                } catch (SQLException e) {
                    throw new RuntimeException(e);
                }
            }
            else {
                System.out.println("option2 :" + option);
            }
        }
    }
    public void AnadirElemAlCarrito(HttpServletRequest request, HttpServletResponse response) throws SQLException, ServletException, IOException {
        //Leer la sesion
        HttpSession session = request.getSession();
        //Existe algun carrito?? : listacarrito
        List<BookDetalle> listacarrito = new ArrayList<>();
        BooksDao booksDao = new BooksDao();
        //Buscamos en la sesion
        if  (session.getAttribute ("listacarrito") == null){
            //Creamos la lista
            //Añadimos el primer elemento
            System.out.println("Añadiendo el primer elemento");
            int id = Integer.parseInt(request.getParameter("id_libro"));
            listacarrito.add(booksDao.Detalle_libro(id));
            session.setAttribute("listacarrito",listacarrito);
            System.out.println(listacarrito.size());
        } else{
            //Leer la lista de la sesion
            System.out.println("leyendo lista elemento");
            listacarrito = (List<BookDetalle>) session.getAttribute ("listacarrito");
            String contenido_carrito = "";
            for(int indice = 0;indice<listacarrito.size();indice++)
            {

                contenido_carrito += "<tr> ";
                contenido_carrito +=  "<td>" + listacarrito.get(indice).getBook_id() + "</td> ";
                contenido_carrito +=  "<td>" + listacarrito.get(indice).getBook_title() + "</td> ";
                contenido_carrito += "<td>" +  listacarrito.get(indice).getPrecio() + "</td>";
                contenido_carrito += "</tr>";
            }
            session.setAttribute("strlistacarrito",contenido_carrito);
            System.out.println(listacarrito.size());
            //Añadirle el libro
            int id = Integer.parseInt(request.getParameter("id_libro"));
            listacarrito.add(booksDao.Detalle_libro(id));

            System.out.println(listacarrito.size());
            //Devolver la lista ampliada a la sesion
            session.setAttribute("listacarrito",listacarrito);

        }
        //Volvemos a mostrar los libros
        List<DTO.BookDetalle> lista = null;
        try {
            lista = booksDao.MostrarLibros();

        } catch (SQLException e) {
            e.printStackTrace();
        }
        //Preparar la salida para invocar al jsp
        request.setAttribute("listaLibros", lista);
        RequestDispatcher requestDispatcher = request.getRequestDispatcher("/jsp/mostrarlibrosvender.jsp");
        requestDispatcher.forward(request, response);
    }
}