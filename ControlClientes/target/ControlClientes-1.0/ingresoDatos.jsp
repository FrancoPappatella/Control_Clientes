<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Ingresar cliente</title>
    </head>
    <body>
        <h1>Ingreso de cliente</h1>
        <form action="${pageContext.request.contextPath}/ServletControlador" method="post"  >
            Nombre: <input type="text" name="nombre">
            <br/><br/>
            Apellido: <input type="text" name="apellido">
            <br/><br/>
            Email: <input type="text" name="email">
            <br/><br/>
            Telefono: <input type="text" name="telefono">
            <br/><br/>
            Saldo: <input type="number" name="saldo">
            <br/><br/><br/><br/>
            
            <input type="submit" value="Enviar">
        </form>
    </body>
</html>
