package ro.usv.dao;

import java.io.*;
import java.util.*;

public class SerializareDaoComplet<T extends Entitate, K> implements Dao<T, K> {
    private File fisierSerializare;
    private Map<K, T> mapMem;

    public SerializareDaoComplet() {
        fisierSerializare = null;
        mapMem = new LinkedHashMap<>();
    }

    public SerializareDaoComplet(String fisierSerializare) {
        this.fisierSerializare = new File(fisierSerializare);
        mapMem = new LinkedHashMap<>();
    }

    @Override
    public List<T> getAll() {
        mapMem =  getAllModifiable();
        return Collections.unmodifiableList( new ArrayList<T>(mapMem.values()) );
    }

    private Map<K, T> getAllModifiable() {
        if(fisierSerializare != null) {
            mapMem = new LinkedHashMap<>();
            try {
                InputStream fisinput = new FileInputStream(fisierSerializare);
                ObjectInputStream objis = new ObjectInputStream(fisinput);
                mapMem = (Map<K,T>) objis.readObject();
                fisinput.close();
                objis.close();
            } catch (Exception e) {
                System.err.println("Eroare deserializare lstObj: " + e.getMessage());
                //e.printStackTrace();
            }
        }
        // else - la fidierSerializare==null ramane mapMem
        return mapMem;
    }

    @Override
    public T get(K id) {
        if (id == null) {
            throw new IllegalArgumentException("get: parametru id=null");
        }
        mapMem = getAllModifiable();
        return mapMem.get(id);
    }

    @Override
    public void save(T obj) throws IllegalArgumentException {
        if (obj == null)
            throw new IllegalArgumentException("save: parametru obj. null");
        mapMem = getAllModifiable();
        if (mapMem.containsValue(obj)) {
            //System.out.println(mapMem.values());
            throw new IllegalArgumentException("save: obj. exista deja id=" + obj.getId());
        }
        mapMem.put((K)obj.getId(), obj);
        saveAll();
    }

    //
    // saveAll(lstObj) - la obiectele existente se adauga cele din lstObj
    //
    public void saveAll(List<T> lstObj){
        if(lstObj==null){
            throw new IllegalArgumentException("saveAll: lstObj = null");
        }
        mapMem = getAllModifiable();
        for (T obj: lstObj) {
            if (mapMem.containsValue(obj)) {
                throw new IllegalArgumentException("saveAll: obj. exista deja id=" + obj.getId());
            }
            mapMem.put((K)obj.getId(), obj);
        }
        saveAll();
    }

    private void saveAll() {
        if (fisierSerializare != null) {
            if (fisierSerializare.exists()) {
                fisierSerializare.delete();
            }
            try {
                OutputStream fisoutput = new FileOutputStream(fisierSerializare);
                ObjectOutputStream objws = new ObjectOutputStream(fisoutput);
                objws.writeObject(mapMem);
                objws.flush();
                objws.close();
            } catch (Exception e) {
                //e.printStackTrace();
                System.err.println("Eroare in timpul serilalizarii: " + e.getMessage());
                System.exit(1);
            }
        }
    }

    @Override
    public void deleteAll() {

        mapMem.clear();
        saveAll();
    }

    @Override
    public void update(T objActualizat) throws IllegalArgumentException {
        if (objActualizat == null)
            throw new IllegalArgumentException("update, parametru null");
        K idCautat = (K) objActualizat.getId();
        mapMem = getAllModifiable();
        if (mapMem.containsKey(idCautat)==false) {
            throw new IllegalArgumentException("update, id inexistent: " + idCautat);
        }
        mapMem.put(idCautat, objActualizat);
        saveAll();
    }

    @Override
    public void delete(K id) throws IllegalArgumentException {
        if (id == null) {
            throw new IllegalArgumentException("delete: parametru id=null");
        }
        mapMem = getAllModifiable();
        if (mapMem.containsKey(id)) {
            mapMem.remove(id);
            saveAll();
        } else {
            throw new IllegalArgumentException("delete, id inexistent: " + id);
        }
    }
}
