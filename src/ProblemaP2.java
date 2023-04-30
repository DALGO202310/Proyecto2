import java.io.BufferedReader;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Queue;

/**
 * @author Juan Andres Jaramillo P.
 * @author Daniela Camacho.
 * @author Yann Chové.
 */

public class ProblemaP2 {

    public static void main(String[] args) throws Exception{
        ProblemaP2 instancia= new ProblemaP2();
        try ( 
			InputStreamReader is= new InputStreamReader(System.in);
			BufferedReader br = new BufferedReader(is);
		){
            String line = br.readLine();
			int casos = Integer.parseInt(line);
			line = br.readLine();
            for(int i=0;i<casos && line!=null && line.length()>0; i++){
                String [] especificacion= line.split(" ");
                int n= Integer.parseInt(especificacion[0]);
                int m= Integer.parseInt(especificacion[1]);
                ArrayList<ArrayList<int[]>> adyacencias= instancia.inicLista(n);
                int [][] m= instancia.inicMatriz(n);
                ArrayList<Integer> rta= new ArrayList<>();
                line=br.readLine();
                for (int j=0; j<=m ;j++){
                    String [] conexion=line.split(" ");
                    int v1= Integer.parseInt(conexion[0])-1;
                    int v2= Integer.parseInt(conexion[1])-1;
                    int k= Integer.parseInt(conexion[3]);
                    adyacencias= instancia.agregarConexion(adyacencias, v1, v2, k);
                    rta.add(instancia.esRedundante(adyacencias, m));
                }
                instancia.printList(rta);
            }
        }
    }
    
    /**
     * funcion para inicializar la lista de adyacencias.Es una lista de listas.
     * Cada posición representa un computador y dentro de cada posición tendrá tuplas represetadas como 
     * (vertice con que tiene conexión, tipo de conexión (k))
     * cada posic
     * @param vertices numero de computadores 
     * @return lista de adyacencias vacia
     */
    public ArrayList<ArrayList<int[]>> inicLista (int vertices){
        ArrayList<ArrayList<int []>> adyacencias= new ArrayList<>();
        ArrayList<int[]> list;
        for (int i=0; i<=vertices; i++){
            list= new ArrayList<>();
            adyacencias.add(list);
        }
        return adyacencias;
    }

    /**
     * funcion para inicializar la matriz donde se especifica el estado de redundancia entre todos 
     * los pares de vertices (computadores) según los tipos de conexiones que hayan entre ellos
     * 0 -> hay conexión con un solo cable (1 o 2)
     * 1 -> hay conexión con los dos cables (1 y 2)
     * 2 -> no hay ninguna conexión 
     * @param vertices numero de computadores
     * @return matriz inicializada
     */
    public int[][] inicMatriz (int vertices){
        new int[][] m= new int [vertices][vertices];
        for (int i=0;i<=vertices;i++){
            for (int j=0; j<=vertices;j++){
                if (i==j){
                    m[i][j]=null;
                }else{
                    m[i][j]=2;
                }
            }
        }
    }

    /**
     * funcion para agregar una conexión en la lista de adyacencias
     * @param adyacencias lista de adyacencias
     * @param i vertice que representa a computador
     * @param j vertice que representa a computador
     * @param k representa el tipo de conexión
     * @return  lista de adyacencias actualizada
     */
    public ArrayList<ArrayList<int[]>> agregarConexion (ArrayList<ArrayList<int[]>> adyacencias, int i, int j, int k){
        //agregar adyacencia para el eje i
        int [] tuple = new int[2];
        tuple[0]=j;
        tuple[1]=k;
        adyacencias.get(i).add(i, tuple);

        //agregar adyacencia para el eje j
        tuple[0]=i;
        adyacencias.get(j).add(j, tuple);

        return adyacencias;
    }

    /**
     * funcion para determinar si hay conexión de a hacia b con tipo de conexión k
     * @param adyacencias lista de adyacencias
     * @param a vertice fuente
     * @param b vertice destino
     * @param k tipo de conexión
     * @return boolean 
     */
    public boolean hayCamino (ArrayList<ArrayList<int[]>> adyacencias, int a, int b,int k){
        Queue<Integer> cola= new LinkedList<>();
        ArrayList<Integer> result= new ArrayList<>();
        cola.add(a);
        int n=a;
        ArrayList <int []> list;
        while (cola.size()>0){
            n=cola.remove();
            result.add(n);
            list= adyacencias.get(n);
            for (int i=0; i<list.size();i++){
                int[] v= list.get(i);
                if (!cola.contains(v[0]) && !result.contains(v[0]) && v[1]==k){
                    cola.add(v[0]);
                    if (v[0]==b){
                        return true;
                    }
                }
            }
        }
        return false;
    }

    /**
     * funcion para determinar si la conexión entre a y b es redundante
     * @param adyacencias lista de adyacencias
     * @param a vertice a
     * @param b vertice b
     * @return int que indica si es redundante
     * 0 -> hay conexión con un solo cable (1 o 2) - no redundante
     * 1 -> hay conexión con los dos cables (1 y 2) - redundante
     * 2 -> no hay ninguna conexión - redundante
     * p1= ¿Hay camino de a-b con 1?
     * p2= ¿Hay camino de a-b con 2?
     */
    public int esRedundantePorVertice (ArrayList<ArrayList<int[]>> adyacencias, int a, int b){
        boolean p1= hayCamino(adyacencias, a, b, 1);
        boolean p2= hayCamino(adyacencias, a, b, 2);
        if (p1 && p2){
            return 1
        }else if (!p1 && !p2){
            return 2
        }else{
            return 0;
        }
    }

    /**
     * funcion para determinar si la red es redundante
     * @param adyacencias lista de adyacencias
     * @param m matriz que indica el tipo de redundancia
     * @return boolean 
     */
    public boolean esRedundante (ArrayList<ArrayList<int[]>> adyacencias, int [][] m){
        for (int i=0; i<adyacencias.size();i++){
            for (int j=0; j<adyacencias.size();j++){
                if (m[i][j]!=1 && i!=j){
                    int c= esRedundantePorVertice(adyacencias, i, j);
                    m[i][j]=c;
                    if (c==0){
                        return false;
                    }
                }
            }
        }
        return true;
    }

    public void printList (ArrayList<Integer> list){

    }

}
