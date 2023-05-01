import java.util.ArrayList;
import java.util.Hashtable;

public class GeneradorPruebas {

    /**
     * Genera pruebas aleatorias con las caracteristicas dadas
     * @param args
     */
    public static void main(String[] args) throws Exception {

        int casos = 5;
        int vertices = 1000;
        int conexiones = 1000;
        System.out.println(casos);
        System.out.println(vertices + " " + conexiones);

        Hashtable<Integer,ArrayList<int[]>> conn = new Hashtable<>(); //Para evitar que una misma conexion se agregue mas de 1 vez.
        int v1, v2, k;

        //inicializa la tabla
        v1 = (int) Math.random()*(vertices+1);
        v2 = (int) Math.random()*(vertices+1);
        k = (int) Math.random()*(3);

        while(v1==v2){ //un computador no puede estar conectado consigo mismo.
            v2 = (int) Math.random()*(vertices+1);
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
        while(i < (conexiones-1)) {
            
            v1 = (int) Math.random()*(vertices+1);
            v2 = (int) Math.random()*(vertices+1);
            k = (int) Math.random()*(3);

            while(v1==v2){ //un computador no puede estar conectado consigo mismo.
                v2 = (int) Math.random()*(vertices+1);
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
