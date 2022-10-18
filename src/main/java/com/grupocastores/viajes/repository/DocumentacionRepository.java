package com.grupocastores.viajes.repository;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import org.springframework.stereotype.Repository;

import com.grupocastores.commons.inhouse.FindTalonCustomResponse;
import com.grupocastores.commons.oficinas.Servidores;



@Repository
public class DocumentacionRepository extends UtilitiesRepository{
    
    
    @PersistenceContext
    private EntityManager entityManager;
    
    static final String queryFindTalones =
//            "SELECT * FROM OPENQUERY( %s, 'SELECT tr.cla_talon,tr.importetotal,tr.nomorigen,tr.calleorigen, tr.nomdestino, tr.calledestino, et.idesquema, et.idnegociacion FROM talones.tr%s tr  INNER JOIN talones.especificacion_talon et ON tr.cla_talon = et.cla_talon WHERE tr.no_guia IS NULL AND tr.tipounidad = %s AND et.idesquema = %s AND et.idcliente = %s AND et.idoficina = \"%s\";');";
            "SELECT * FROM OPENQUERY(" + DB_23 + ", 'SELECT tr.cla_talon,tr.importetotal,tr.nomorigen,tr.calleorigen, tr.nomdestino, tr.calledestino, et.idesquema, et.idnegociacion FROM talones.tr%s tr  INNER JOIN talones.especificacion_talon et ON tr.cla_talon = et.cla_talon WHERE tr.no_guia IS NULL AND tr.tipounidad = %s AND et.idesquema = %s AND et.idcliente = %s AND et.idoficina = \"%s\";');";

    @SuppressWarnings("unchecked")
    public List<Object[]> findTalones( String mesAnio, int idEsquema, int tipoViaje, int tipoUnidad, int idCliente, String idOficinaCliente){
        
        Query query = entityManager.createNativeQuery(String.format(queryFindTalones,
                 mesAnio, tipoUnidad, idEsquema, idCliente, idOficinaCliente),Servidores.class
            );
        return query.getResultList();
        
    }
}
