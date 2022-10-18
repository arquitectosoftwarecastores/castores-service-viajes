package com.grupocastores.viajes.service;

import java.util.List;

import com.grupocastores.commons.inhouse.FindTalonCustomResponse;

public interface IDocumentacionService {

    /**
     * findTalones:Se consultan talones para documentacion de viaje .
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
     * @date 2022
     */
    public List<FindTalonCustomResponse> findTalones(String mesAnio, int idEsquema, int tipoViaje, int tipoUnidad, int idCliente,String idOficinaCliente, String idOficinaDocumenta) throws Exception;

    public String getFolio(String idFolio, String idOficinaDocumenta);

}
