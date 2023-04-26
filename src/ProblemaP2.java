import java.util.ArrayList;

public class ProblemaP2 {
    
    /**
     * funcion para inicializar la lista de adyacaencias
     * @param vertices numero de computadores 
     * @return lista de adyacencias vacia
     */
    public ArrayList<ArrayList<int[]>> inicLista (int vertices){
        ArrayList<ArrayList<int []>> adyacencias= new ArrayList<>();
        ArrayList<int[]> list;
        for (int i=0; i<vertices; i++){
            list= new ArrayList<>();
            adyacencias.add(list);
        }
        return adyacencias;
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
        int [] tuple= new int[2];
        tuple[0]=j-1;
        tuple[1]=k;
        ArrayList<int []> list=adyacencias.get(i-1);
        list.add(tuple);
        adyacencias.add(i-1, list);

        //agregar adyacencia para el eje j
        tuple[0]=i-1;
        list= adyacencias.get(j-1);
        list.add(tuple);
        adyacencias.add(j-1, list);
        return adyacencias;
    }

    public boolean hayCamino (ArrayList<ArrayList<int[]>> adyacencias, int a, int w,int k){
        
        return false;
    }

}
