import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Scanner;
import org.json.JSONArray;
import org.json.JSONObject;

/**
 * Clase principal para gestionar la biblioteca con persistencia en archivo JSON
 */
public class BibliotecaJSON {

    // Estructura de datos para almacenar los libros en memoria
    private static ArrayList<Libro> biblioteca = new ArrayList<>();
    private static final String ARCHIVO_JSON = "biblioteca.json";

    public static void main(String[] args) {
        cargarDatos(); // Cargar los datos desde el archivo JSON al iniciar
        Scanner scanner = new Scanner(System.in);
        int opcion;

        do {
            mostrarMenu();
            opcion = scanner.nextInt();
            scanner.nextLine(); // Consumir el salto de línea

            switch (opcion) {
                case 1:
                    registrarLibro(scanner);
                    break;
                case 2:
                    actualizarLibro(scanner);
                    break;
                case 3:
                    eliminarLibro(scanner);
                    break;
                case 4:
                    buscarLibro(scanner);
                    break;
                case 5:
                    ordenarLibros(scanner);
                    break;
                case 6:
                    mostrarTodosLosLibros(); // Nueva opción para mostrar todos los libros
                    break;
                case 7:
                    guardarDatos(); // Guardar los datos en JSON antes de salir
                    System.out.println("Saliendo...");
                    break;
                default:
                    System.out.println("Opción inválida. Intente nuevamente.");
            }
        } while (opcion != 7);

        scanner.close();
    }

    /**
     * Muestra el menú de opciones en consola
     */
    private static void mostrarMenu() {
        System.out.println("\n--- Biblioteca ---");
        System.out.println("1. Registrar nuevo libro");
        System.out.println("2. Actualizar libro");
        System.out.println("3. Eliminar libro");
        System.out.println("4. Buscar libro");
        System.out.println("5. Ordenar libros");
        System.out.println("6. Mostrar todos los libros"); // Nueva opción en el menú
        System.out.println("7. Salir");
        System.out.print("Seleccione una opción: ");
    }

    /**
     * Cargar los datos desde el archivo JSON a la memoria
     */
    private static void cargarDatos() {
        try {
            byte[] fileContent = Files.readAllBytes(Paths.get(ARCHIVO_JSON));
            JSONArray jsonArray = new JSONArray(new String(fileContent));

            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject jsonObject = jsonArray.getJSONObject(i);
                Libro libro = new Libro(
                        jsonObject.getString("codigo"),
                        jsonObject.getString("nombre"),
                        jsonObject.getString("autor"),
                        jsonObject.getString("materia"),
                        jsonObject.getInt("numPaginas")
                );
                biblioteca.add(libro);
            }
            System.out.println("Datos cargados correctamente.");
        } catch (IOException e) {
            System.out.println("No se encontraron datos previos o hubo un error al cargar los datos.");
        }
    }

    /**
     * Guarda los datos en el archivo JSON
     */
    private static void guardarDatos() {
        JSONArray jsonArray = new JSONArray();
        for (Libro libro : biblioteca) {
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("codigo", libro.codigo);
            jsonObject.put("nombre", libro.nombre);
            jsonObject.put("autor", libro.autor);
            jsonObject.put("materia", libro.materia);
            jsonObject.put("numPaginas", libro.numPaginas);
            jsonArray.put(jsonObject);
        }

        try (FileWriter file = new FileWriter(ARCHIVO_JSON)) {
            file.write(jsonArray.toString(4)); // Formato con sangría de 4 espacios
            System.out.println("Datos guardados correctamente en " + ARCHIVO_JSON);
        } catch (IOException e) {
            System.out.println("Error al guardar los datos: " + e.getMessage());
        }
    }

    /**
     * Registra un nuevo libro en la biblioteca
     */
    private static void registrarLibro(Scanner scanner) {
        System.out.print("Ingrese el código del libro: ");
        String codigo = scanner.nextLine();
        System.out.print("Ingrese el nombre del libro: ");
        String nombre = scanner.nextLine();
        System.out.print("Ingrese el autor del libro: ");
        String autor = scanner.nextLine();
        System.out.print("Ingrese la materia del libro: ");
        String materia = scanner.nextLine();
        System.out.print("Ingrese el número de páginas: ");
        int numPaginas = scanner.nextInt();

        Libro libro = new Libro(codigo, nombre, autor, materia, numPaginas);
        biblioteca.add(libro);
        System.out.println("Libro registrado exitosamente.");
    }

    /**
     * Actualiza la información de un libro existente
     */
    private static void actualizarLibro(Scanner scanner) {
        System.out.print("Ingrese el código del libro a actualizar: ");
        String codigo = scanner.nextLine();
        for (Libro libro : biblioteca) {
            if (libro.codigo.equals(codigo)) {
                System.out.print("Ingrese el nuevo nombre del libro: ");
                libro.nombre = scanner.nextLine();
                System.out.print("Ingrese el nuevo autor del libro: ");
                libro.autor = scanner.nextLine();
                System.out.print("Ingrese la nueva materia del libro: ");
                libro.materia = scanner.nextLine();
                System.out.print("Ingrese el nuevo número de páginas: ");
                libro.numPaginas = scanner.nextInt();
                scanner.nextLine();
                System.out.println("Libro actualizado exitosamente.");
                return;
            }
        }
        System.out.println("Libro no encontrado.");
    }

    /**
     * Elimina un libro de la biblioteca
     */
    private static void eliminarLibro(Scanner scanner) {
        System.out.print("Ingrese el código del libro a eliminar: ");
        String codigo = scanner.nextLine();
        biblioteca.removeIf(libro -> libro.codigo.equals(codigo));
        System.out.println("Libro eliminado exitosamente.");
    }

    /**
     * Busca un libro en la biblioteca
     */
    private static void buscarLibro(Scanner scanner) {
        System.out.print("Ingrese el código del libro a buscar: ");
        String codigo = scanner.nextLine();
        for (Libro libro : biblioteca) {
            if (libro.codigo.equals(codigo)) {
                System.out.println("Libro encontrado: " + libro);
                return;
            }
        }
        System.out.println("Libro no encontrado.");
    }

    /**
     * Ordena los libros en la biblioteca según el criterio seleccionado
     */
    private static void ordenarLibros(Scanner scanner) {
        System.out.println("Seleccione el criterio de ordenamiento:");
        System.out.println("1. Código");
        System.out.println("2. Nombre");
        System.out.println("3. Autor");
        int criterio = scanner.nextInt();

        biblioteca.sort((libro1, libro2) -> {
            switch (criterio) {
                case 1: return libro1.codigo.compareTo(libro2.codigo);
                case 2: return libro1.nombre.compareTo(libro2.nombre);
                case 3: return libro1.autor.compareTo(libro2.autor);
                default: return 0;
            }
        });
        System.out.println("Libros ordenados según el criterio seleccionado.");
    }

    /**
     * Muestra todos los libros almacenados en la biblioteca
     */
    private static void mostrarTodosLosLibros() {
        System.out.println("\n--- Lista de todos los libros ---");
        if (biblioteca.isEmpty()) {
            System.out.println("No hay libros almacenados.");
        } else {
            for (Libro libro : biblioteca) {
                System.out.println(libro);
            }
        }
    }
}

/**
 * Clase Libro que representa un libro en la biblioteca
 */
class Libro {
    String codigo;
    String nombre;
    String autor;
    String materia;
    int numPaginas;

    public Libro(String codigo, String nombre, String autor, String materia, int numPaginas) {
        this.codigo = codigo;
        this.nombre = nombre;
        this.autor = autor;
        this.materia = materia;
        this.numPaginas = numPaginas;
    }

    @Override
    public String toString() {
        return "Código: " + codigo + ", Nombre: " + nombre + ", Autor: " + autor +
               ", Materia: " + materia + ", Número de Páginas: " + numPaginas;
    }
}

/**
 * Compilar
 * javac -cp ".;lib/json.jar" BibliotecaJSON.java
 * Ejecutar
 * java -cp ".;lib/json.jar" BibliotecaJSON
 */