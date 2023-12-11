/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pathfile;

/**
 * @author Andrea Paton [write]
 *
 * @author Pablo Esparis [info, mkfile]
 *
 * @author Carlos Martin [delete]
 *
 * @author Sergio Oropesa [help, mkdir, dividirComandos2()]
 *
 * @author Kevin Francisco [dir]
 *
 * @author Alberto Jimenez [cat, top]
 *
 * @author Yeray Déniz (YAS) [ Cuerpo del programa, cd(), dividirComando(),
 * clear(), close(), mostrarCabecera() ]
 *
 */
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;

import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

import java.nio.file.Path;

import java.nio.file.Paths;

import java.util.InputMismatchException;

import java.util.Scanner;

public class PracticaCompleta {

	// SCANNER GLOBAL

	/*
	 * Creamos una instancia del objeto Scanner, lo hacemos de manera global.
	 * Llamamos "elEscaner" a dicha instancia. Capturará desde teclado (System.in)
	 * los datos que el usuario introduzca
	 */
	public static Scanner elEscaner = new Scanner(System.in);

	// La variable donde se va a almacenar el comando que introducirá el usuario
	// será global, para poder modificarla
	// fácilmente desde el metodo cd
	public static String comando;

	// Iniciamos el constructor principal del programa
	public static void main(String[] args) {

		iniciarComandos();

	}

	private static void iniciarComandos() {

		// Declaramos y definimos variables
		// La condición que nos permitirá salir del bucle do while principal
		boolean condicion = false, llaveDir;

		// Aquí irá lo que escriba el usuario después de cd (si escribe algo después
		// de
		// cd)
		String parte2 = "";

		// Ruta actual de directorios del usuario
		Path path = Paths.get(System.getProperty("user.dir"));

		// Mostramos la cabecera del programa al usuario
		mostrarCabecera();

		String dos = ""; // Declaro un String en el que guardar lo que escribira el usuario tras mkdir

		String introducido = "";

		do {
			llaveDir = true;

			System.out.print("\nConsola>");
			comando = elEscaner.nextLine();

			// En caso de que el comando sea cd o alguna de sus variantes (cd.., cd .., cd
			// directorio),
			// éste se dividirá en dos partes. La parte final (parte2) es la que más nos
			// interesa, ya que irá como parámetro en el método cd()
			parte2 = dividirComando(); // Yeray

			// El string llamado comando va como parámetro del switch
			dos = dividirComando2(); // Igualo el String dos a lo devuelto por dividirComando2, ya que quiero que sea
										// mi posicion 1 en el array

			introducido = dividircat();

			switch (comando) {

			case "help":

				help();

				break;

			case "cd":

				path = cd(parte2, path); // Yeray

				break;

			case "mkdir":

				mkDir(path, dos);

				break;

			case "info":

				System.out.println("info");

				break;

			case "cat":

				cat(path, introducido);

				break;

			case "top":

				top(path);

				break;

			case "mkfile":

				System.out.println("mkfile");

				break;

			case "write":

				write(path);

				break;

			case "dir":

				dir();

				break;

			case "delete":

				delete();

				break;

			case "close":

				condicion = close();

				break;

			case "clear":

				clear();

				break;

			default:

				if (llaveDir) {
					System.out.println("comando incorrecto");
				}

			}

		} while (!condicion);

	}

//Yeray  
	/**
	 * Este método divide el comando que comienza por cd.
	 *
	 * @return La parte anexa a cd identifica si está vacía, si es dos puntos (..)
	 *         o si se trata de otra cosa, lo cual se verificará más adelante en
	 *         el método cd().
	 */
	private static String dividirComando() {

		String[] partes = {};
		String parte2 = "";
		if (comando.startsWith("cd ")) {
			partes = comando.split(" ", 2);
			parte2 = partes[1];
			comando = "cd";

		} else if (comando.equals("cd..")) {
			partes = comando.split("cd", 2);
			parte2 = partes[1];
			comando = "cd";
		}

		return parte2;
	}

	/**
	 * Este método comprueba si la variable parte2 (la palabra escrita después de
	 * cd y del espacio en el main) es igual a dos puntos seguidos.
	 *
	 * Si lo es, busca la ruta anterior a la actual. Además, hace una captura de
	 * NullPointerException.
	 *
	 * Si no lo es, sería posible que la palabra ingresada después del espacio
	 * fuera un directorio posterior a la ruta actual. Lo comprueba y lanza el
	 * resultado.
	 *
	 * @param parte2
	 * @param path
	 * @return Path
	 */
	private static Path cd(String parte2, Path path) {

		int contadorCarpetas = path.getNameCount();
		Path rutaActual = path;

		if (parte2.equals("..")) {

			if (contadorCarpetas != 0) { // La cantidad de carpetas va disminuyendo según se va retrocediendo en los
				// directorios
				path = path.getParent();
				System.out.println(path);
				return path; // Devolvemos la ruta
			} else {
				System.out.println(rutaActual); // Esta ruta será igual a la raíz de la ruta original
			}

		} else {

			Path relativa = rutaActual.resolve(parte2);

			File haciaDelante = new File(relativa.toString());

			if (haciaDelante.exists()) {
				System.out.println(haciaDelante.getAbsolutePath()); // Dirección actual
				return haciaDelante.toPath();
			} else {
				System.out.println("El directorio no existe");
				return rutaActual;

			}

		}

		return rutaActual;
	}

	/**
	 * Este método hace que se impriman tantas líneas en blanco como se definan en
	 * la variable 'lineas'.
	 */
	private static void clear() {

		int lineas = 50;

		for (int i = 0; i < lineas; ++i) {
			System.out.println();
		}

	}

	/**
	 * Este método cierra el objeto Scanner y devuelve true, el cual viajará hasta
	 * la variable booleana "condicion", la cual hará que el programa salga del
	 * bucle y se de por finalizado el programa.
	 *
	 * @return true
	 */
	private static boolean close() {
		// Cerramos la llamada al objeto Scanner (el cual es global)
		elEscaner.close();

		System.out.println("Programa cerrado.");

		return true;
	}

	/**
	 * Muestra la cabecera del programa
	 */
	public static void mostrarCabecera() {

		System.out.print("(c) Grupo Path File Corporation. [Versión 1.0]\n Todos los derechos reservados.\n\n"
				+ "Escribe help si necesitas ayuda\n\n");

	}

	// Fin métodos Yeray
//kevin  
	// Muestra los archivos y directorios de la ruta ingresada
	private static void dir() {


			try {   // Control de errores
				System.out.println("Ingresa una ruta: "); 							// Aqui entra la ruta a donde quiere ir el usuario		
				File ruta = new File(elEscaner.nextLine()); 						// La ruta sera igual al dato ingresado en el scanner

				// *****Muestra archivos o ficheros dentro de una ruta***//
				
				
				if (ruta.exists()) {

					
					String[] nombreArchivos = ruta.list(); 						// Para crear lista de archivos de una carpeta primero lo
																				// vuelve String para poder mostrarlo.

																				// Los archivos de la ruta se guardan en array.
					for (int i = 0; i < nombreArchivos.length; i++) { 			// El bucle recorrera todo el array mostrando lo que haya dentro de la carpeta.

						System.out.println(nombreArchivos[i]);

						File f = new File(ruta.getAbsolutePath(), nombreArchivos[i]);  // Muestra los archivos que estan dentro de un directorio

																						// toma la ruta madre y le agrega el nombre de x archivo guardado en la array
																						// nombreArchivos
						if (f.isDirectory()) { 											// Verifica que se trate de un directorio, en caso de ser asi entrara aqui.

							try {
								String[] archivosSubcarpeta = f.list(); 					// En caso de si ser un directorio convierte los archivos en string para verlos.

								for (int j = 0; j < archivosSubcarpeta.length; j++) { // Este for recorre el array archivosSubcarpeta e imprime por pantalla lo que encuentre.

									System.out.println("       -" + archivosSubcarpeta[j]);
								}
								
							} catch (NullPointerException e) {
								// TODO Auto-generated catch block
								System.out.println("Hemos encontrado un error.");
							}

						}
					}

				}else {

					System.out.println("La ruta ingresada no existe o no es un directorio... \n");


				}

			} catch (InputMismatchException e) { // Control de errores *cierre*

				System.out.println("Has introducido un dato erroneo... \n");


			}
		}

//Alberto

	private static String dividircat() {

		String catarray[] = {};

		String introducido = " ";

		if (comando.startsWith("cat ")) {

			catarray = comando.split(" ", 2);
			introducido = catarray[1];
			comando = "cat";
		}

		return introducido;

	}

	/**
	 *
	 * Metodo para el case cat. Este metodo permite al usuario leer el contenido de
	 * un archivo.
	 *
	 */
	private static void cat(Path path, String introducido) {
		try {
			// System.out.println("Ingresa el nombre del archivo:");
			// String archivo = elEscaner.nextLine();
			Path rutaDada = Paths.get(introducido);

			Path rutaFinal = path.resolve(rutaDada.toString());

			File directorio = new File(rutaFinal.toString());

// Apertura del fichero y creacion de BufferedReader
			if (directorio.exists()) {
// Apertura del fichero y creacion de BufferedReader
				FileReader fr = new FileReader(directorio);
				BufferedReader br = new BufferedReader(fr);
// "C:/Programacion/"
// Lectura del fichero
				String linea;
// lee el contenido de las lineas hasta que el el resultado sea null, es decir que no haya nada mas que leer.
				while ((linea = br.readLine()) != null) {
					System.out.println(linea);// imprime todo.
				}
			} else {
				System.out.println("No se ha encontrado.");
			}
		} catch (IOException e) {
			System.out.println("No se ha encontrado el archivo");
		}
	}

	/**
	 *
	 * Metodo para el case top. Este metodo permite al usuario leer el numero de
	 * lineas que desee del contenido de un archivo.
	 *
	 */
	private static void top(Path path) {
		try {
			System.out.println("Ingresa una ruta con el nombre del archivo: ");
			String archivo = elEscaner.nextLine();// Pedimos la ruta absoluta con el archivo
			Path rutaDada = Paths.get(archivo);// guardamos la ruta con el archivo
			File archivoEscogido = new File(rutaDada.toString());// Instanciamos un objeto de la clase File con la ruta
																	// dada
			if (archivoEscogido.isAbsolute() && archivoEscogido.exists()) {
// Apertura del fichero y creacion de BufferedReader
				FileReader fr = new FileReader(archivo);
				BufferedReader br = new BufferedReader(fr);
// Lectura del fichero
				System.out.println("Introduce numero de lineas que quieres ver:");
				int tamano = elEscaner.nextInt();// el usuario introduce el numero de filas que quiere ver.
				String salto = elEscaner.nextLine();
				for (int i = 0; i < tamano; i++) { // esto representa el número de filas que habrá.
					String linea = br.readLine(); // el programa lee las lineas
// creamos un string para imprimir las lineas.
					System.out.println(linea);// imprime el contenido de las lineas seleccionadas
				}
			} else {
				System.out.println("No se ha encontrado.");
			}
		} catch (IOException e) {
			System.out.println("No se ha encontrado el archivo.");
		}
	}

// Sergio 
	/**
	 * Este metodo consta de una variable String que contiene todos los comandos de
	 * la terminal y la informacion de cada uno de ellos. El String se imprime por
	 * pantalla para informar al usuario.
	 *
	 */
	private static void help() {

		String help = "Bienvenido al comando de ayuda de esta terminal, a continuación se muestra un listado "
				+ "con los comandos que puede utilizar\ny la función que realiza cada uno:\n\n"
				+ " - help: este comando le muestra la pantalla en la que se encuentra actualmente.\n"
				+ " - cd: este comando le muestra el directorio en el que se encuentra en ese momento.\n"
				+ " - cd .. : este comando le permite acceder al directorio padre del actual.\n"
				+ " - cd 'nombreDirectorio' : este comando le permite acceder a un directorio dentro del actual.\n"
				+ " - cd 'rutaAbsoluta' : este comando le permite acceder a la ruta absoluta del sistema.\n"
				+ " - mkdir 'nombreDirectorio' : este comando le permite crear un directorio en la ruta actual.\n"
				+ " - info 'nombre' : este comando le mostrará la información del elemento.\n"
				+ " - cat 'nombreFichero' : este comando le mostrará el contenido de un fichero.\n"
				+ " - top 'numeroLineas' 'nombreFichero' : este comando le mostrará las líneas que especifique en un fichero.\n"
				+ " - mkfile 'nombreFichero' 'texto' : este comando le permite crear un fichero con el nombre especificado y el contenido escrito.\n"
				+ " - write 'nombreFichro' 'texto' : este comando le permite añadir texto al final del fichero seleccionado.\n"
				+ " - dir: este comando le permite listar los archivos o directorios de la ruta actual.\n"
				+ " - dir 'nombreDirectorio' : este comando le permite listar los archivos o directorios dentro del directorio especificado.\n"
				+ " - dir 'rutaAbsoluta' : lista los archivos o directorios dentro de la ruta especificada.\n"
				+ " - delete 'nombreFichero' : este comando le permite borrar el fichero seleccionado, si es un directorio borra el contenido del mismo y el directorio.\n"
				+ " - clear: este comando le permite vaciar la terminal.\n"
				+ " - close: este comando le permite cerrar la terminal.\n\n"
				+ "Esperamos haberle ayudado. Muchas gracias.";

		System.out.println(help);

	}

	/**
	 * Este metodo divide el comando mkdir en un array por espacios de 2 posiciones
	 * siendo la primera mkdir y la segunda lo q introduzca el usuario
	 *
	 * @return dos
	 */
	public static String dividirComando2() {

		String[] division = {}; // Declaramos un array llamado division

		String dos = ""; // Declaramos un String vacio que será el valor que nos da el usuario

		if (comando.startsWith("mkdir ")) { // Si el comando comienza con mkdir, entonces

			division = comando.split(" ", 2); // Dividimos el array por espacios y le damos dos posiciones
			dos = division[1]; // Lo introducido por el usuario ocupara la posicion 1 del array
			comando = "mkdir"; // El valor del comando es mkdir, que será la posicion 0 de nuestro array

		}

		return dos;
	}

	/**
	 * Este método pide al usuario la ruta en la que quiere crear el directorio y
	 * comprueba si existe o no, en caso de que no exista, lo crea. En caso de que
	 * el directorio ya exista se mostrará un mensaje explicando que ya existe el
	 * directorio introducido. Recibe como parametros la ruta en la que nos
	 * encontramos y el nombre del directorio que quiere crear el usuario
	 *
	 */
	private static void mkDir(Path path, String dos) {

		Path rutaDada = Paths.get(dos); // Cojo la ruta dada (directorio) y lo convierto en una ruta

		File nuevoDirectorio = new File(rutaDada.toString());// Instanciamos un objeto de la clase File con la ruta dada

		if (nuevoDirectorio.isAbsolute()) { // Si la ruta es absoluta...

			if (!nuevoDirectorio.exists()) {// Si el directorio no existe, entonces lo creamos

				boolean crear = nuevoDirectorio.mkdir();

				if (crear) {
					System.out.println("El directorio se ha creado:\n " + nuevoDirectorio);
				}
			} else if (nuevoDirectorio.exists()) { // Si el directorio existe, lo comunicamos

				System.out.println("El directorio introducido ya existe");
			}
		} else if (!nuevoDirectorio.isAbsolute()) { // Si la ruta al directorio no es absoluta...

			Path rutaFinal = path.resolve(rutaDada.toString()); // ... entonces tengo que hacer la ruta fina cogiendo la
																// ruta de parametro y haciendo el resolve en la
																// rutaDada de esa ruta

			nuevoDirectorio = new File(rutaFinal.toString());

			if (!nuevoDirectorio.exists()) { // Si el directorio no existe, lo creamos

				boolean crear2 = nuevoDirectorio.mkdir();

				if (crear2) {
					System.out.println("El directorio se ha creado:\n" + nuevoDirectorio);
				}

			} else if (nuevoDirectorio.exists()) {// Si el directorio existe lo comunicamos

				System.out.println("El directorio introducido ya existe");
			}

		}

	}

	// Andrea

	/**
	 * Función que recoge la variable path que hace referencia a la ruta ya
	 * determinada previamente en el terminal Con la función write pedimos al
	 * usuario que indique un fichero Crearemos un directorio con la ruta al fichero
	 * Si el directorio existe procedemos a iniciar nuestra función pidiendo al
	 * usuario que inserte el texto
	 **/
	private static void write(Path path) {

		System.out.println("Ingresa un fichero"); // Le pedimos al usuario que ingrese el nombre del ficher
		String ficheroDado = elEscaner.nextLine();
		File fichero = new File(ficheroDado); // Instanciamos un fichero nuevo con el fichero que nos ha ingresado el
												// usuario

		Path rutaFinal = path.resolve(fichero.toString()); // Establecemos la ruta al fichero dado

		File directorio = new File(rutaFinal.toString()); // Creamos un directorio con la ruta hacia el fichero

		if (directorio.exists()) { // Si el directorio existe, entonces pedimos el texto que se quiere añadir

			try {
				System.out.println("Escribe el texto que quieres añadir.");
				String texto = elEscaner.nextLine();
				BufferedWriter bw = new BufferedWriter(new FileWriter(directorio, true)); // Comprobamos que se puede
																							// escribir
				bw.write("\n" + texto); // // Momento en el que se establece el texto dado por el usuario en el archivo.
				bw.close();
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		}
	}

	// Carlos
	/**
	 * Esta funcion pide un directorio o archivo concreto y comprueba si existe. En
	 * caso de existir el archivo , lo borraria e imprime un mensaje de confirmacion
	 * 
	 * Si es un directorio entra en otra funcion recursiva que borra cada fichero
	 * dentro del directorio y el directorio en si Si el directorio o archivo no
	 * existe imprimir� un mensaje de error.
	 * 
	 **/

	private static void delete() {

		System.out.println("Ingresa una ruta: ");
		String nombreRuta = elEscaner.nextLine();

		File ruta = new File(nombreRuta);

		boolean estatus = ruta.delete(); // Por alguna razon se inicializa en false
		if (!estatus) {
			if (ruta.isDirectory()) {
				borrar(ruta);
				System.out.println("Se ha eliminado el directorio");
			} else
				System.out.println("Error, este archivo no existe");

		} else {

			System.out.println("Se ha eliminado el archivo");
		}

	}

	public static void borrar(File file) {
		if (file.isDirectory()) {
			File[] entries = file.listFiles();
			if (entries != null) {
				for (File entry : entries) {
					borrar(entry);
				}
			}
		}
		if (!file.delete()) {
			file.delete();

		}

	}

}
