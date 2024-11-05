import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.io.FileOutputStream;
import java.io.OutputStream;

import excepciones.EtiquetaInvalida;
import excepciones.FormatoTablaInvalido;
import interfaces.Escritura;
import interfaces.Lectura;

public class GestorCSV implements Lectura, Escritura<Tabla> {
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

    

    @Override
    public void escribirCSV(Tabla tabla, String ruta) {
        try {
            // Especifica el nombre del archivo
            String nombreArchivo = ruta + "/Output.csv";
            
            // Crea un OutputStream para escribir en el archivo
            OutputStream outputStream = new FileOutputStream(nombreArchivo);

            String contenido = "";
            for(int i = 0 ; i < tabla.getEtiquetas_columna().size() ; i++){
                if (tabla.getEtiquetas_columna().get(i) instanceof Etiqueta){
                    contenido += ((Etiqueta) tabla.getEtiquetas_columna().get(i)).getNombre() + ",";
                }else{
                    throw new EtiquetaInvalida("La lista: " + tabla.getEtiquetas_columna() + " no es una lista de etiquetas");
                }
            }
            contenido = contenido.substring(0,contenido.length()-1);
            
            List<Columna<?>> lista_columnas = tabla.getListaColumnas();
            for( int i = 0 ; i < tabla.getCantidadFilas() ; i++){
                String fila = "";
                for ( int j = 0 ; j < tabla.getCantidadColumnas(); j ++){
                    fila += lista_columnas.get(j).valorCelda(i) + ",";
                }
                contenido += "\n" + fila.substring(0,fila.length()-1);
            }



            byte[] datos = contenido.getBytes();
            outputStream.write(datos);
            
            
            // Cierra el OutputStream
            outputStream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static Object[][] transponer(List<Object[]> matriz) { 
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
