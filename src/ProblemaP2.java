import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.Set;

/**
 * @author Juan Andres Jaramillo P.
 * @author Daniela Camacho.
 * @author Yann Chové.
 */
public class ProblemaP2 {

    /**
     * Instancia de Particion que guarda el bosque construido por la red de cable optico.
     */
    private static Particion g1;

    /**
     * Instancia de Particion que guarda el bosque construido por la red de cable coaxial.
     */
    private static Particion g2;

    /**
     * Array que guarda la ubicacion de cada computador a nivel global.
     * 0: si no ha sido conectado con ningun cable
     * 1: si tiene alguna conexion por cable optico
     * 2: si tiene alguna conexion por cable coaxial
     * 3: si tiene conexion por cable optico y coaxial
     */
    private static int[] verLoc; 


    //-------------------------------------------------------------------------------------------
    // METODOS:
    //-------------------------------------------------------------------------------------------

    /**
     * Metodo principal del programa.
     * Recibe y procesa el input desde consola (entrada estandar), e imprime la solucion tambien en consola (salida estandar).
     * @param args
     * @throws Exception si ocurre algun error durante la lectura del input o durante la impresion de los resultados.
     */
    public static void main(String[] args) throws Exception {

		try ( 
			InputStreamReader is= new InputStreamReader(System.in);
			BufferedReader br = new BufferedReader(is);
		) { 
			String line = br.readLine();
			int casos = Integer.parseInt(line);
			for(int i=0;i<casos && line!=null && !line.isBlank() && line.length()>0 && !"0".equals(line);i++) {
                
                //inicializa e problema
                line = br.readLine();
                String [] dim = line.split(" ");//numero de vertices y ejes
                int vertices = Integer.parseInt(dim[0]);
                int connections = Integer.parseInt(dim[1]);
                g1 = new Particion(vertices); //red de computadores conectados con cable optico (k=1).
                g2 = new Particion(vertices); //red de computadores conectados con cable coaxial (k=2).
                verLoc =  new int[vertices]; // 0: no ha sido asignado, 1: pertenece a red optica, 2: red coaxial, 3: red optica y coaxial.
                
                for (int j = 0; j < connections; j++) {
                    String [] sTuple = br.readLine().split(" ");
                    int[] tuple = new int[3];
                    for (int k = 0; k < 3; k++) {
                        if(k==0 || k==1)
                            tuple[k] = Integer.parseInt(sTuple[k])-1;
                        else
                            tuple[k] = Integer.parseInt(sTuple[k]);
                    }
                    if(j == (connections-1))
                        ampliarRed(tuple, true);
                    else
                        ampliarRed(tuple, false);
                }
                System.out.print("\n");
			}
            br.close();
		}catch(Exception e){
            System.out.println("ERROR: Ocurrio un problema durante la lectura de la informacion\n");
            e.printStackTrace();
        }
	}


    /**
     * Recibe una nueva conexion y la agrega a la red de computadores. 
     * @param conexion la nueva conexion a agregar.
     * Se supone que la conexion es nueva y que no existen conexiones de un computador consigo mismo (self-loops).
     * 
     * Complejidad: O(v).
     */
    public static void ampliarRed(int[] conexion, boolean ultima){

        
        if(conexion[2]==1){//si la conexion es con cable optico:
            g1.union(conexion[0], conexion[1]); // depende principalmente del metodo find(int v) de la misma clase
                                                // que es O(1) como se dedujo en clase.
            for (int i = 0; i < 2; i++) { //O(1) (solo da 2 ciclos, el contenido de adentro tambien es O(1) porque son comparaciones
                                          // y asignaciones)
                if(verLoc[conexion[i]]==0){verLoc[conexion[i]]=1;}
                else if (verLoc[conexion[i]]==2){verLoc[conexion[i]]=3;}
            }
        }
        else{ //si es con cable coaxial:
            g2.union(conexion[0], conexion[1]); //O(1)
            for (int i = 0; i < 2; i++) { //O(1)
                if(verLoc[conexion[i]]==0){verLoc[conexion[i]]=2;}
                else if (verLoc[conexion[i]]==1){verLoc[conexion[i]]=3;}
            }
        }

        //Revisa si hay redundancia:
        // O(v)
        boolean testRed = testRedundandy();
        if (testRed && ultima) {
            System.out.print("1");
        } else if (testRed && !ultima){
            System.out.print("1 ");
        }else if(!testRed && ultima){
            System.out.print("0");
        }else{
            System.out.print("0 ");
        }

    }

    /**
     * Metodo encargado de revisar si hay redundancia en la red de computadores
     * @return {@code True} si hay redundancia y {@code False} de lo contrario. 
     * 
     * Complejidad: O(v)
     */
    public static boolean testRedundandy(){

        //si ambos grafos no tienen el mismo numero de vertices retorna false:
        // O(v) v: numero de vertices
        for (int i = 0; i < verLoc.length; i++) {
            if(verLoc[i]==0)
                continue;
            if(verLoc[i]==1 || verLoc[i]==2)
                return false;
        }

        //Si el numero de arboles en el bosque generado por la red Optica difiere del numero generado por la red Coaxial, retorna false
        Hashtable<Integer,ArrayList<Integer>> bosque1= new Hashtable<>();
        Hashtable<Integer,ArrayList<Integer>> bosque2= new Hashtable<>();

        // O(v) v: numero de vertices
        for (int i = 0; i < verLoc.length; i++) {
            if(verLoc[i]==0)
                continue;
            else{
                int clase1 = g1.find(i); // O(1)
                int clase2 =  g2.find(i); // O(1)

                if(bosque1.containsKey(clase1)){// O(1) segun javadoc, worst case O(v) si la hash function esta muy mal implementada, 
                                                // caso que se descarta aca porque la llave es un dato simple que puede ser mapeado
                                                // directamente como la posición en el arreglo.
                    bosque1.get(clase1).add(i); // O(1) segun javadoc, worst case O(v) si se debe ampliar el tamano de la lista.
                                                // donde n: el numero de computadores en el componente conectado clase1.
                }else{
                    bosque1.put(clase1, new ArrayList<Integer>(50)); // O(1)
                    bosque1.get(clase1).add(i); // O(1)
                }

                if(bosque2.containsKey(clase2)){
                    bosque2.get(clase2).add(i);
                }else{
                    bosque2.put(clase2, new ArrayList<Integer>(50));
                    bosque2.get(clase2).add(i);
                }

            }
        }
        if(bosque1.size() != bosque2.size()){ // O(1)
            return false;
        }

        //Recorre todos los arboles de cada red y revisa si existe redundancia:
        // O(v)
        Set<Integer> keyset = bosque1.keySet(); // O(1) segun javadocs
        ArrayList<Integer> cc;
        for (Integer key : keyset) {
            cc = bosque1.get(key); // O(1)
            int set = g2.find(cc.get(0)); // O(1) Clase en el grafo 2 (coaxial) a la que pertenece el vertice.
            for (int i = 1; i < cc.size(); i++) { // O(v)
                int v = cc.get(i);
                if(g2.find(v)!=set){ // los vertices que pertenecen a un cc del grafo 1 (optico) tienen que pertenecer tambien a un componente conectado en el grafo 2 (coaxial)
                    return false;    // de lo contrario la red no seria redundante porque hay una conexion entre 2 vertices por optico que no existe por coaxial (o vice-versa). 
                }
            }
        }

        return true;

    }


    //-------------------------------------------------------------------------------------------
    // CLASE INTERNA DESARROLLADA PARA MAPEAR EL BOSQUE GENERADO POR CADA RED: 
    //-------------------------------------------------------------------------------------------

    public static class Particion {

        /**
         * Arreglo con la informacion del arbol al que pertenece cada vertice.
         */
        private int[] parents;

        /**
         * Arreglo utilizado para mejorar la eficiencia del metodo {@code union}.
         */
        private int[] alturaEstimada;
    
        /**
         * Constructor
         * Asigna cada vertice del grafo a un subset y actualiza la altura estimada de cada vertice a 1.
         * @param n numero de vertices que tiene el grafo.
         * 
         * Complejidad: O(n)
         */
        public Particion(int n){
            parents = new int[n];
            alturaEstimada = new int[n];
            for (int i = 0; i < n; i++) {
                parents[i] = i;
                alturaEstimada[i]=1;
            }
        }
    
        /**
         * Encuentra el representante del subset al que pertenece el vertice v
         * @param v el vertice del cual se quiere encontrar el padre (representante del subset al que pertenece).
         * @return el vertice padre del vertice {@code v} ingresado por parametro.
         * 
         * Complejidad: O(1)
         */
        public int find(int v){
            if(parents[v]==v) return v;
            else{
                int r = find(parents[v]);
                parents[v] =r;
                return r;
            } 
        }
    
        /**
         * Determina si los vertices v1 y v2 pertenecen al mismo subset.
         * @param v1 vertice 1.
         * @param v2 vertice 2.
         * @return {@code true} si v1 y v2 pertenecen al mismo subset, {@code false} si no.
         * 
         * Complejidad: O(1)
         */
        public boolean sameSubSet(int v1, int v2){
            return find(v1)==find(v2);
        }
    
        /**
         * Une el subset al que pertenece {@code v1} con el subset al que pertenece {@code v2}
         * @param v1 vertice 1
         * @param v2 vertice 2
         * 
         * Complejidad: O(1)
         */
        public void union(int v1, int v2){
            int s1 = find(v1); //O(1)
            int s2 = find(v2); //O(1)
            if(alturaEstimada[s1]>alturaEstimada[s2]){
                parents[s2]=s1;
            }else
                parents[s1]=s2;
            if(alturaEstimada[s1]==alturaEstimada[s2])
                alturaEstimada[s2]++;
        }

    }
    
}
