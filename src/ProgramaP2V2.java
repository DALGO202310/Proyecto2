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

public class ProgramaP2V2 {

    
    private static  Particion g1;

    private static Particion g2;

    private static int[] verLoc; 

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
                    ampliarRed(tuple);
                }
                System.out.print("\n");
			}
            br.close();
		}catch(Exception e){
            System.out.println("Ocurrio un problema durante la lectura de la informacion:\n" +
            "-----------------------------------\n");
            e.printStackTrace();
        }
	}

    //-------------------------------------------------------------------------------------------
    // PROGRAMA PRINCIPAL:
    //-------------------------------------------------------------------------------------------
    

    public static void ampliarRed(int[] conexion){

        
        if(conexion[2]==1){//si la conexion es con cable optico:
            g1.union(conexion[0], conexion[1]);
            for (int i = 0; i < 2; i++) {
                if(verLoc[conexion[i]]==0){verLoc[conexion[i]]=1;}
                else if (verLoc[conexion[i]]==2){verLoc[conexion[i]]=3;}
            }
        }
        else{ //si es con cable coaxial:
            g2.union(conexion[0], conexion[1]);
            for (int i = 0; i < 2; i++) {
                if(verLoc[conexion[i]]==0){verLoc[conexion[i]]=2;}
                else if (verLoc[conexion[i]]==1){verLoc[conexion[i]]=3;}
            }
        }

        //Revisa si hay redundancia:
        boolean testRed = testRedundandy();
        if (testRed) {
            System.out.print("1 ");
        } else{
            System.out.print("0 ");
        }

    }

    public static boolean testRedundandy(){

        //si ambos grafos no tienen en mismo numero de vertices retorna false:
        for (int i = 0; i < verLoc.length; i++) {
            if(verLoc[i]==0)
                continue;
            if(verLoc[i]==1 || verLoc[i]==2)
                return false;
        }

        //Si el numero de arboles en el bosque generado por la red Optica difiere del numero generado por la red Coaxial, retorna false
        Hashtable<Integer,ArrayList<Integer>> bosque1= new Hashtable<>();
        Hashtable<Integer,ArrayList<Integer>> bosque2= new Hashtable<>();

        for (int i = 0; i < verLoc.length; i++) {
            if(verLoc[i]==0)
                continue;
            else{
                int clase1 = g1.find(i);
                int clase2 =  g2.find(i);

                if(bosque1.containsKey(clase1)){
                    bosque1.get(clase1).add(i);
                }else{
                    bosque1.put(clase1, new ArrayList<Integer>());
                    bosque1.get(clase1).add(i);
                }

                if(bosque2.containsKey(clase2)){
                    bosque2.get(clase2).add(i);
                }else{
                    bosque2.put(clase2, new ArrayList<Integer>());
                    bosque2.get(clase2).add(i);
                }

            }
        }
        if(bosque1.size() != bosque2.size()){
            return false;
        }

        //recorre todos los arboles de cada red y revisa si existe redundancia:
        Set<Integer> keyset = bosque1.keySet();     
        ArrayList<Integer> cc;
        for (Integer key : keyset) {
         cc = bosque1.get(key);
         int set = g2.find(cc.get(0)); // Clase en el grafo 2 (coaxial) a la que pertenece el vertice.
         for (int i = 1; i < cc.size(); i++) {
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

        private int[] parents;
        private int[] alturaEstimada;
    
        /**
         * Constructor
         * Asigna cada vertice del grafo a un subset y actualiza la altura estimada de cada vertice a 1.
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
         * @return el vertice padre del vertice {@code v} ingresado por parametro .
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
         */
        public boolean sameSubSet(int v1, int v2){
            return find(v1)==find(v2);
        }
    
        /**
         * Une el subset al que pertenece {@code v1} con el subset al que pertenece {@code v2}
         * @param v1 vertice 1
         * @param v2 vertice 2
         */
        public void union(int v1, int v2){
            int s1 = find(v1);
            int s2 = find(v2);
            if(alturaEstimada[s1]>alturaEstimada[s2]){
                parents[s2]=s1;
            }else
                parents[s1]=s2;
            if(alturaEstimada[s1]==alturaEstimada[s2])
                alturaEstimada[s2]++;
        }

    }
    
}
