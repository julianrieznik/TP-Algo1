import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import excepciones.FormatoTablaInvalido;
import interfaces.Lectura;

public class GestorCSV implements Lectura {
    @Override
    public Tabla leer(String rutaArchivo) throws IOException, FormatoTablaInvalido {
        try (BufferedReader br = new BufferedReader(new FileReader(rutaArchivo))) {
            
            // Leer la primera línea como etiquetas de columnas
            String linea = br.readLine();
            if (linea == null) {
                throw new FormatoTablaInvalido("El archivo CSV está vacío.");
            }
            
            String[] etiquetasColumnas = linea.split(","); // Suponiendo separación por comas
            //K[] etiquetasColumnasCast = (K[]) etiquetasColumnas; // Casteo a K[]

            // Crear lista para almacenar las filas de datos
            List<Object[]> filasDatos = new ArrayList<>();

            // Leer cada línea como una nueva fila en la tabla
            while ((linea = br.readLine()) != null) {
                String[] valores = linea.split(",");
                filasDatos.add(valores);
            }

            // Convertir filas de datos en un arreglo bidimensional
            //Object[][] datosTabla = new Object[etiquetasColumnas.length][filasDatos.size()];
            //for (int i = 0; i < filasDatos.size(); i++) {
                //datosTabla[i] = filasDatos.get(i);
            //}

            Object[][] listaColumnas = transponer(filasDatos);

            // Crear una nueva instancia de Tabla con etiquetas de columnas y datos
            return new Tabla<>(etiquetasColumnas, listaColumnas);
        } catch (IOException e) {
            throw new IOException("Error al leer el archivo CSV.", e);
        }
    }

    public static Object[][] transponer(List<Object[]> matriz) { 
        // Obtener el tamaño de la matriz original 
        int filas = matriz.size(); 
        int columnas = matriz.get(0).length; 
        // Crear una nueva matriz para la transpuesta 
        Object[][] transpuesta = new Object[columnas][filas]; 
        
        for (int i = 0; i < columnas; i++) { 
            for (int j = 0; j < filas; j++) { 
                transpuesta[i][j] = matriz.get(j)[i]; 
            } 
        } 
        return transpuesta; 
    }
}
