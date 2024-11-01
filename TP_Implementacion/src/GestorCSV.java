import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import excepciones.FormatoTablaInvalido;
import interfaces.Lectura;

public abstract class GestorCSV {
    public <K, F> Tabla<K, F> leerCSV(String rutaArchivo) throws IOException, FormatoTablaInvalido {
        try (BufferedReader br = new BufferedReader(new FileReader(rutaArchivo))) {
            
            // Leer la primera línea como etiquetas de columnas
            String linea = br.readLine();
            if (linea == null) {
                throw new FormatoTablaInvalido("El archivo CSV está vacío.");
            }
            
            String[] etiquetasColumnas = linea.split(","); // Suponiendo separación por comas
            K[] etiquetasColumnasCast = (K[]) etiquetasColumnas; // Casteo a K[]

            // Crear lista para almacenar las filas de datos
            List<Object[]> filasDatos = new ArrayList<>();

            // Leer cada línea como una nueva fila en la tabla
            while ((linea = br.readLine()) != null) {
                String[] valores = linea.split(",");
                filasDatos.add(valores);
            }

            // Convertir filas de datos en un arreglo bidimensional
            Object[][] datosTabla = new Object[etiquetasColumnas.length][filasDatos.size()];
            for (int i = 0; i < filasDatos.size(); i++) {
                datosTabla[i] = filasDatos.get(i);
            }

            // Crear una nueva instancia de Tabla con etiquetas de columnas y datos
            return new Tabla<>(etiquetasColumnasCast, datosTabla);
        } catch (IOException e) {
            throw new IOException("Error al leer el archivo CSV.", e);
        }
    }
}
