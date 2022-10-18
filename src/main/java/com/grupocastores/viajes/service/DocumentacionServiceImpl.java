package com.grupocastores.viajes.service;

import java.math.BigDecimal;
import java.sql.Date;
import java.sql.Time;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.grupocastores.commons.inhouse.BitacoraViajesRequest;
import com.grupocastores.commons.inhouse.FindTalonCustomResponse;
import com.grupocastores.commons.oficinas.Servidores;
import com.grupocastores.viajes.repository.DocumentacionRepository;
import com.grupocastores.viajes.repository.UtilitiesRepository;

@Service
public class DocumentacionServiceImpl implements IDocumentacionService{
    
    @Autowired
    private DocumentacionRepository documentacionRepository;
    
    @Autowired
    private UtilitiesRepository utilitiesRepository;
    

    /**
     * findTalones: Se consultan talones para documentacion de viaje .
     * 
     * @param String mesAnio
     * @param int idEsquema
     * @param int tipoViaje
     * @param int tipoUnidad
     * @param int idCliente
     * @param int idOficina
     * @version 0.0.1
     * @author Oscar Eduardo Guerra Salcedo [OscarGuerra] 
     * @return Coordenada
     * @throws Exception 
     * @date 2022-10-17
     */
    @Override
    public List<FindTalonCustomResponse> findTalones(String mesAnio, int idEsquema, int tipoViaje, int tipoUnidad, int idCliente, String idOficinaCliente, String idOficinaDocumenta ) throws Exception {
       
        
//        Servidores server = utilitiesRepository.getLinkedServerByOfice(idOficinaDocumenta);
//        List<Object[]> list = documentacionRepository.findTalones(server.getServidorVinculado(), mesAnio, idEsquema, tipoViaje, tipoUnidad, idCliente, idOficinaCliente);

        List<Object[]> list = documentacionRepository.findTalones( mesAnio, idEsquema, tipoViaje, tipoUnidad, idCliente, idOficinaCliente);
        List<FindTalonCustomResponse> listTalones = new ArrayList<>();
        
        list.stream().forEach(item -> {
           
            listTalones.add(new FindTalonCustomResponse((String) item[0], (float) item[1], (String) item[2],(String) item[3],(String) item[4],(String) item[5],(String) item[6],(int) item[7], (int) item[8], (int) item[9])  ) ;         
        });
        
        return listTalones;
       
    }

    /**
     * findTalones: Se consulta el proximo folio ya sea de viaje o guia.
     * 
     * @param String idFolio
     * @param String idOficinaDocumenta
    
     * @version 0.0.1
     * @author Oscar Eduardo Guerra Salcedo [OscarGuerra] 
     * @return folio String
     * @throws Exception 
     * @date 2022-10-17
     */
    @Override
    public String getFolio(String idFolio, String idOficinaDocumenta) {
        // TODO Auto-generated method stub
        return null;
    }
    
    
}
