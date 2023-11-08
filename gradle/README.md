##Librerias usadas
-GSON MATERIAL DESING
Invenio Talet
Invenio Talet es una aplicación Android que permite a los usuarios ingresar y almacenar información sobre libros.
La aplicación también verifica la conectividad a Internet y almacena los datos en una base de datos remota a través de solicitudes HTTP.
##FORMULARIOS TIPO INTERACTIVOS

##RELOJ FECHA Y HORA
##Características clave
##Ingreso de Datos: Los usuarios pueden ingresar detalles de un libro, como el título, el autor, la fecha y el contenido.
##Validación de Campos: Se realizan validaciones en los campos de entrada para garantizar que no estén vacíos.
##Selección de Fecha: Los usuarios pueden seleccionar la fecha de publicación de un libro utilizando un selector de fecha.
##Almacenamiento Remoto: Los datos ingresados se envían a un servidor remoto para su almacenamiento.
###Recuperación de Datos: Los resultados de las solicitudes al servidor s e almacenan localmente para su visualización posterior.
##Conectividad en Tiempo Real: La aplicación verifica la conectividad a Internet y muestra un mensaje de estado en función de la disponibilidad de la red.
##Listado de Libros: Los usuarios pueden acceder a una lista de libros ingresados previamente.

##Componentes Principales
-MainActivity: La actividad principal de la aplicación donde los usuarios ingresan detalles de libros y envían la información al servidor.
-ConnectivityReceiver: Un receptor de difusión que monitorea la conectividad a Internet en tiempo real y muestra el estado de la red.
-Volley: Se utiliza para realizar solicitudes HTTP al servidor para el almacenamiento y recuperación de datos.

##Uso
--Abre la aplicación Invenio Talet en tu dispositivo Android.
--Ingresa los detalles del libro, incluyendo título, autor, fecha y contenido.
--Haz clic en "Enviar" para almacenar la información en el servidor remoto.
--La aplicación verificará la conectividad y mostrará un mensaje de estado.
--Puedes acceder a la lista de libros ingresados previamente haciendo clic en "Lista".
--Invenio Talet es una herramienta útil para llevar un registro de tus libros y asegurarte de que tus datos estén disponibles incluso cuando no tengas acceso a Internet.