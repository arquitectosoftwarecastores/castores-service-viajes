package com.grupocastores.viajes.repository;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.ParameterMode;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.persistence.StoredProcedureQuery;

import org.springframework.stereotype.Repository;

import com.grupocastores.commons.oficinas.Personal;
import com.grupocastores.commons.oficinas.Servidores;

@Repository
public class UtilitiesRepository {

    @PersistenceContext
    private EntityManager entityManager;
    
    public static final String DB_23 = "PRUEBAS23";
    public static final String DB_13 = "TIJUANAPRUEBA";
    public static final String queryGetLinkedServerByIdOficina = 
            "SELECT * FROM syn.dbo.v_Oficinas where Oficina = \'%s\'";
    static final String queryGetPersonalByIdUsuario = 
            "SELECT * FROM OPENQUERY("+ DB_13 +",'SELECT * FROM personal.personal p WHERE p.idusuario = \"%s\"');";
    
    
    /**
     * executeStoredProcedure: Ejecuta un procedimiento alamcenado para Guardar, Editar
     * 
     * @param query (String) consulta a ejecutar
     * @return Boolean
     * @author Moises Lopez Arrona [moisesarrona]
     * @date 2022-06-14
     */
    public Boolean executeStoredProcedure(String query) {
        int resp = 0;
        StoredProcedureQuery storedProcedure = entityManager.createStoredProcedureQuery("PcrExecSQL");
        storedProcedure.registerStoredProcedureParameter("sql", String.class, ParameterMode.IN);
        storedProcedure.registerStoredProcedureParameter("respuesta", String.class, ParameterMode.OUT);
        storedProcedure.setParameter("sql", query);
        storedProcedure.execute();
        resp = Integer.valueOf((String) storedProcedure.getOutputParameterValue("respuesta"));
        return resp > 0 ? true : false;
    }
    
    /**
     * getLinkedServerByOfice: Obtiene el servidor vinculado por id de oficina
     * 
     * @param idOficina (String) consulta a ejecutar
     * @return Boolean
     * @author Oscar Eduardo Guerra Salcedo [OscarGuerra]
     * @date 2022-09-22
     */
    @SuppressWarnings("unchecked")
    public Servidores getLinkedServerByOfice(String idOficina) {
        Query query = entityManager.createNativeQuery(String.format(queryGetLinkedServerByIdOficina,
                idOficina),Servidores.class
            );
        return (Servidores) query.getResultList().get(0);
    }
    
    /**
     * getPersonalByIdUsuario: Obtiene el personal por id de usuario.
     * 
     * @version 0.0.1
     * @author Oscar Eduardo Guerra Salcedo [OscarGuerra]
     * @throws Exception 
     * @date 2022-08-19
     */
    @SuppressWarnings("unchecked")
    public Personal getPersonalByIdUsuario (int idUsuario) throws Exception {
        
        Query query = entityManager.createNativeQuery(String.format(
                queryGetPersonalByIdUsuario, idUsuario),Personal.class);
        
        List<Personal> list = query.getResultList();
        if (list == null)
            throw new Exception("No se pudo obtener el registro del usuario: "+idUsuario );
        return (Personal) list.get(0);
    }
}
