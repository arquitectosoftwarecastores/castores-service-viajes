package com.grupocastores.viajes.service;

import java.util.List;

import com.grupocastores.commons.inhouse.TalonCustomResponse;
import com.grupocastores.commons.inhouse.DetaCo;
import com.grupocastores.commons.inhouse.FolioDos;
import com.grupocastores.commons.inhouse.FoliosGuias;
import com.grupocastores.commons.inhouse.GuMesAnio;
import com.grupocastores.commons.inhouse.GuMesAnioCustom;
import com.grupocastores.commons.inhouse.Guias;
import com.grupocastores.commons.inhouse.ImporteGuia;
import com.grupocastores.commons.inhouse.OperadorCustom;
import com.grupocastores.commons.inhouse.RemolqueInternoCustom;
import com.grupocastores.commons.inhouse.TgCustom;
import com.grupocastores.commons.oficinas.Guiaviaje;
import com.grupocastores.commons.oficinas.Tg;
import com.grupocastores.commons.oficinas.Viajes;

//import castores.dao.talones.GuiaviajeDao;

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
    public List<TalonCustomResponse> findTalones(String mesAnio, int idEsquema, int tipoViaje, int tipoUnidad, int idCliente,String idOficinaCliente, String idOficinaDocumenta, String determinanteOrigen, String determinanteDestino) throws Exception;
    
    /**
     * getFolioViaje: Se consulta el proximo folio de viaje.
     * 
     * @param String idFolioViaje
     * @param String idOficinaDocumenta
     * @version 0.0.1
     * @author Oscar Eduardo Guerra Salcedo [OscarGuerra] 
     * @return FolioDos
     * @throws Exception 
     * @date 2022-10-17
     */
    public FolioDos getFolioViaje(int idFolio, String idOficinaDocumenta);
    
    /**
     * insertViaje: inserta nuevo viaje.
     * 
  @Override
       * @param (Viajes) dataViaje
     * @version 0.0.1
     * @author Oscar Eduardo Guerra Salcedo [OscarGuerra] 
     * @return dataViaje Viajes
     * @throws Exception 
     * @date 2022-10-18
     */
    public Viajes insertViaje(Viajes dataViaje) throws Exception;
    
    /**
     * insertGuia: inserta nueva guia.
     * 
     * @param (Guias) dataViaje
     * @version 0.0.1
     * @author Oscar Eduardo Guerra Salcedo [OscarGuerra] 
     * @return Guias
     * @throws Exception 
     * @date 2022-10-18
     */
    public Guias insertGuia(Guias dataViaje) throws Exception;
    
    /**
     * insertImporteGuia: inserta importe de guia.
     * 
     * @param (ImporteGuiaCustom) dataImporte
     * @param (String) idoficinaDocumenta
     * @version 0.0.1
     * @author Oscar Eduardo Guerra Salcedo [OscarGuerra] 
     * @return Boolean
     * @throws Exception 
     * @date 2022-11-08
     */
    Boolean insertImporteGuia(ImporteGuia dataImporte, String idoficinaDocumenta) throws Exception;
    
    /**
     * getFolioViaje: Se consulta el proximo folio  de guia.
     * 
     * @param String idFolioGuia
     * @param String idOficinaDocumenta
     * @version 0.0.1
     * @author Oscar Eduardo Guerra Salcedo [OscarGuerra] 
     * @return FoliosGuias
     * @throws Exception 
     * @date 2022-10-18
     */    
    public FoliosGuias getFolioGuia(int idFolio, String idOficinaDocumenta);

    /**
     * updateFolioViaje: incrementa idfolio y clavefolio para su proximo uso.
     * 
     * @param int idFolio
     * @param String claveFolio
     * @version 0.0.1
     * @author Oscar Eduardo Guerra Salcedo [OscarGuerra] 
     * @return Boolean
     * @throws Exception 
     * @date 2022-10-20
     */
    public Boolean updateFolioViaje(long idFolio, String claveFolio, String linkedServer);
    
    /**
     * insertTalonGuia: inserta detalle de talonguia.
     * 
     * @param  dataGuia (List<Tg>)
     * @version 0.0.1
     * @author Oscar Eduardo Guerra Salcedo [OscarGuerra] 
     * @return Boolean
     * @throws Exception 
     * @date 2022-10-20
     */
    public Boolean insertTalonGuia(List<TgCustom> dataGuia);
    
    /**
     * insertGuiaViaje: inserta detalle de GuiaViaje.
     * 
     * @param  GuiaviajeDao 
     * @version 0.0.1
     * @author Oscar Eduardo Guerra Salcedo [OscarGuerra] 
     * @return Boolean
     * @throws Exception 
     * @date 2022-10-21
     */
    public Boolean insertGuiaViaje(Guiaviaje dataGuia) throws Exception;

    /**
     * updateFolioGuia: incrementa idfolio y clavefolio para su proximo uso.
     * 
     * @param int idFolioGuia
     * @param String claveFolioGuia
     * @param String linkedServer
     * @version 0.0.1
     * @author Oscar Eduardo Guerra Salcedo [OscarGuerra] 
     * @return Boolean
     * @throws Exception 
     * @date 2022-10-21
     */
    Boolean updateFolioGuia(String noFolioGuia, String linkedServer);
    
    /**
     * updateViajes: inserta nuevo viaje.
     * 
     * @param (Viajes) dataViaje
     * @version 0.0.1
     * @author Oscar Eduardo Guerra Salcedo [OscarGuerra] 
     * @return dataViaje Viajes
     * @throws Exception 
     * @date 2022-10-24
     */
    public Viajes updateViajes(Viajes dataViaje);

    public Viajes getViaje(long idViaje, String idOficina);

    public Guias updateGuia(Guias dataGuia);

    public Guias getGuia(String noGuia, String tabla);

    public Boolean insertGuMesAnio(GuMesAnioCustom dataGuiaMesAnio);

    public Boolean updateGuMesAnio(GuMesAnioCustom dataGuiaMesAnio) throws Exception;

    public GuMesAnio getGuMesAnio(String noGuia, String tabla, String idOficinaDocumenta);

    public List<OperadorCustom> getOperador(String unidad, int tipoUnidad);

    public DetaCo findDetacoSumatoria(String claTalon, String idOficinaDocumenta);

    public List<Viajes> filterViajes(String table, int idEsquema, int idesquemagasto, int tipounidad, int tiporuta, int idruta, String idOficinaDocumenta);

    public List<RemolqueInternoCustom> getRemolqueInterno(int idRemolque, String idOficinaDocumenta);

    public List<RemolqueInternoCustom> getRqmolqueExterno(int idFolio, String idOficinaDocumenta);

    
    

}
