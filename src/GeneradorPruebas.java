import java.io.BufferedReader;
import java.io.File;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.Random;

public class GeneradorPruebas {

    /**
     * Genera pruebas aleatorias con las caracteristicas dadas
     * @param args
     */
    public static void main(String[] args) throws Exception {

        //----------------------------------lectura de datos:
        
		InputStreamReader is= new InputStreamReader(System.in);
		BufferedReader br = new BufferedReader(is);

        System.out.println("Escriba el nombre del archivo .in donde desea guardar los datos (agregue .in al final):");
        String fileName = br.readLine();

        System.out.println("Escriba el numero de casos a probar:");
        int casos = Integer.parseInt(br.readLine());

        int[] vertices = new int[casos];
        int[] conexiones = new int[casos];

        System.out.println("Escriba el numero de COMPUTADORES para cada caso separados por un (1) espacio:");
        String[] aux = br.readLine().split(" ");
        for (int i = 0; i < aux.length; i++) {
            vertices[i] = Integer.parseInt(aux[i]);
        }
        if(aux.length != casos){
            System.out.println("ERROR: el num de computadores no concuerda con el num de casos");
            casos = 0;
        }

        System.out.println("Escriba el numero de CONEXIONES para cada caso separados por un (1) espacio:");
        aux = br.readLine().split(" ");
        for (int i = 0; i < aux.length; i++) {
            conexiones[i] = Integer.parseInt(aux[i]);
        }

        if(aux.length != casos){
            System.out.println("ERROR: el num de conexiones no concuerda con el num de casos");
            casos = 0;
        }
        //----------------------------------Finaliza lectura de datos


        //----------------------------------Inicia Generacion de datos de prueba:

        PrintStream o = new PrintStream(new File(fileName));
        PrintStream console = System.out;
        System.setOut(o); //redirige la salida a un archivo con el nonbre dado.
        
        System.out.println(casos);
        for (int i = 0; i < casos; i++) {

            System.out.println(vertices[i] + " " + conexiones[i]);
            Hashtable<Integer,ArrayList<int[]>> conn = new Hashtable<>(); //Para evitar que una misma conexion se agregue mas de 1 vez.
            int v1, v2, k;
            int [] tuple = new int[2];
            Random rand = new Random();

            //Agrega las conexiones:
            ArrayList<int[]> adj;
            int x =0;
            while(x < conexiones[i]) {
                
                v1 = 1 + rand.nextInt(vertices[i]);
                v2 = 1 + rand.nextInt(vertices[i]);
                k = 1 + rand.nextInt(2);

                while(v1==v2){ //un computador no puede estar conectado consigo mismo.
                    v2 = 1 + rand.nextInt(vertices[i]);
                }
                
                if(!conn.containsKey(v1)){
                    conn.put(v1, new ArrayList<int[]>());
                }

                adj = conn.get(v1);
                boolean repetida = false;
                for (int j = 0; j < adj.size() && !repetida; j++) { //revisa que la conexion no exista.

                    if(adj.get(j)[0]==v2 && adj.get(j)[1]== k){
                        repetida = true;
                    }
                }
                if(repetida==false){

                    if(conn.containsKey(v2)){
                        tuple[0] = v1;
                        tuple[1] = k;
                        conn.get(v2).add(tuple);
                    }else{
                        conn.put(v2, new ArrayList<int[]>());
                        tuple[0] = v1;
                        tuple[1] = k;
                        conn.get(v2).add(tuple);
                    }
                    tuple[0] = v2;
                    tuple[1] = k;
                    conn.get(v1).add(tuple);
                    System.out.println(v1 + " "+ v2 + " " + k);
                    x++;
                }
            }
        }
	}
}
