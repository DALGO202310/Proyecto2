import java.util.ArrayList;
import java.util.Hashtable;
import java.util.Random;

public class GeneradorPruebas {

    /**
     * Genera pruebas aleatorias con las caracteristicas dadas
     * PARA UTILIZAR EL PROGRAMA:
     * MODIFICAR LAS VARIABLES: {@code casos}, {@code vertices}  y {@code conexiones} con los valores deseados.
     * @param args
     */
    public static void main(String[] args) throws Exception {

        int casos = 1;
        int vertices = 2;
        int conexiones = 3;
        System.out.println(casos);
        System.out.println(vertices + " " + conexiones);

        Hashtable<Integer,ArrayList<int[]>> conn = new Hashtable<>(); //Para evitar que una misma conexion se agregue mas de 1 vez.
        int v1, v2, k;

        //inicializa la tabla
        Random rand = new Random();
        v1 = 1 + rand.nextInt(vertices);
        v2 = 1 + rand.nextInt(vertices);
        k = rand.nextInt(3);

        while(v1==v2){ //un computador no puede estar conectado consigo mismo.
            v2 =  1 + rand.nextInt(vertices);
        }

        int [] tuple = new int[2];
        conn.put(v1, new ArrayList<int[]>());
        conn.put(v2, new ArrayList<int[]>());
        tuple[0] = v2;
        tuple[1] = k;
        conn.get(v1).add(tuple);
        tuple[0] = v1;
        conn.get(v2).add(tuple);

        //Agrega el resto de conexiones:
        ArrayList<int[]> adj;
        int i =0;
        while(i < conexiones) {
            
            v1 = 1 + rand.nextInt(vertices);
            v2 = 1 + rand.nextInt(vertices);
            k = 1 + rand.nextInt(3);

            while(v1==v2){ //un computador no puede estar conectado consigo mismo.
                v2 = 1 + rand.nextInt(vertices);
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
                i++;
            }
        }
	}
    
}
