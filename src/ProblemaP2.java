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

    /**
     * funcion para determinar si hay conexión de a hacia b con tipo de conexión k
     * @param adyacencias lista de adyacencias
     * @param a vertice fuente
     * @param b vertice destino
     * @param k tipo de conexión
     * @return boolean 
     */
    public boolean hayCamino (ArrayList<ArrayList<int[]>> adyacencias, int a, int b,int k){
        ArrayList<Integer> cola= new ArrayList<>();
        ArrayList<Integer> result= new ArrayList<>();
        //se debee arreglar la resta del 1 en la funcion principal
        cola.add(a);
        int n=a;
        ArrayList <int []> list;
        while (cola.size()>0){
            n=cola.get(0);
            cola.remove(0);
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

}
