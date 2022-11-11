package com.grupocastores.viajes.service;

import java.util.Calendar;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.grupocastores.commons.inhouse.DetaCo;
import com.grupocastores.commons.inhouse.FolioDos;
import com.grupocastores.commons.inhouse.FoliosGuias;
import com.grupocastores.commons.inhouse.GuMesAnio;
import com.grupocastores.commons.inhouse.GuMesAnioCustom;
import com.grupocastores.commons.inhouse.Guias;
import com.grupocastores.commons.inhouse.ImporteGuia;
import com.grupocastores.commons.inhouse.OperadorCustom;
import com.grupocastores.commons.inhouse.TalonCustomResponse;
import com.grupocastores.commons.inhouse.TgCustom;
import com.grupocastores.commons.oficinas.Guiaviaje;
import com.grupocastores.commons.oficinas.Personal;
import com.grupocastores.commons.oficinas.Servidores;
import com.grupocastores.commons.oficinas.Viajes;
import com.grupocastores.viajes.repository.DocumentacionRepository;
import com.grupocastores.viajes.repository.UtilitiesRepository;

@Service
public class DocumentacionServiceImpl implements IDocumentacionService{
    
    @Autowired
    private DocumentacionRepository documentacionRepository;
    
    @Autowired
    private UtilitiesRepository utilitiesRepository;
    
    
    public static final String DBPRUEBA = "TIJUANAPRUEBA";

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
    public List<TalonCustomResponse> findTalones(String mesAnio, int idEsquema, int tipoViaje, int tipoUnidad, int idCliente, String idOficinaCliente, String idOficinaDocumenta, String determinanteOrigen, String determinanteDestino ) throws Exception {
           
        Servidores server = utilitiesRepository.getLinkedServerByOfice(idOficinaDocumenta);
        List<TalonCustomResponse> list = documentacionRepository.findTalones( mesAnio, idEsquema, tipoViaje, tipoUnidad, idCliente, idOficinaCliente, determinanteOrigen, determinanteDestino, DBPRUEBA);      
        return list;
       
    }
    
    @Override
    public DetaCo findDetacoSumatoria(String claTalon, String idOficinaDocumenta) {
        Servidores server = utilitiesRepository.getLinkedServerByOfice(idOficinaDocumenta);
        DetaCo list = documentacionRepository.findDetacoSumatoria( claTalon, DBPRUEBA);      
        return list;
    }

    /**
     * getFolioViaje: Se consulta el proximo folio de viaje.
     * 
     * @param String idFolioViaje
     * @param String idOficinaDocumenta
     * @version 0.0.1
     * @author Oscar Eduardo Guerra Salcedo [OscarGuerra] 
     * @return folio String
     * @throws Exception 
     * @date 2022-10-17
     */
    @Override
    public FolioDos getFolioViaje(int idFolio, String idOficinaDocumenta) {
        Servidores server = utilitiesRepository.getLinkedServerByOfice(idOficinaDocumenta);
        FolioDos response = documentacionRepository.getFolioViaje(idFolio, DBPRUEBA);  
      
        // TODO Auto-generated method stub
        return response;
    }
    
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
    @Override
    public Boolean updateFolioViaje(long idFolio, String claveFolio, String linkedServer) {
       
 
        long nextIdFolio = idFolio+1;
        String idOficina = claveFolio.substring(0,4);
        String oldClaveFolio = claveFolio.substring(5);
        String newClaveFolio = Integer.toString(Integer.valueOf(oldClaveFolio) + 1) ;
        String nextClaveFolio = StringUtils.leftPad(newClaveFolio, 7, "0");
        nextClaveFolio = idOficina.concat(nextClaveFolio);
        boolean response = documentacionRepository.updateFolioViaje(nextIdFolio, nextClaveFolio, linkedServer); 
        return response;
    }

    /**
     * insertViaje: inserta nuevo viaje.
     * 
     * @param (Viajes) dataViaje
     * @version 0.0.1
     * @author Oscar Eduardo Guerra Salcedo [OscarGuerra] 
     * @return dataViaje Viajes
     * @throws Exception 
     * @date 2022-10-18
     */
    @Override
    public Viajes insertViaje(Viajes dataViaje) throws Exception {
        
        Servidores server = utilitiesRepository.getLinkedServerByOfice("1144");
        FolioDos folioViaje = getFolioViaje(1, dataViaje.getIdoficina());
        Personal personal = utilitiesRepository.getPersonalByIdUsuario(dataViaje.getIdpersonal());
        int idPersonal = personal.getIdpersonal();
        dataViaje.setIdpersonal(idPersonal);
        dataViaje.setIdviaje(folioViaje.getIdfolio());
        dataViaje.setFolio(folioViaje.getClavefolio());
        boolean inserted = documentacionRepository.insertViaje(dataViaje, DBPRUEBA);
        if(inserted) {
            updateFolioViaje(folioViaje.getIdfolio(),folioViaje.getClavefolio(), DBPRUEBA);
            return dataViaje;
        }
        return null;
        
    }
    
    /**
     * updateViajes: Actualiza un viaje.
     * 
     * @param (Viajes) dataViaje
     * @param (String) linkedServer
     * @version 0.0.1
     * @author Oscar Eduardo Guerra Salcedo [OscarGuerra] 
     * @return Viajes | null
     * @throws Exception 
     * @date 2022-10-24
     */
    @Override
    public Viajes updateViajes(Viajes dataViaje) {
        Servidores server = utilitiesRepository.getLinkedServerByOfice("1144");
        Viajes viaje = getViaje(dataViaje.getIdviaje(), dataViaje.getIdoficina());
        viaje.setIdcatalogoViajes(dataViaje.getIdcatalogoViajes());
        viaje.setIdruta(dataViaje.getIdruta());
        viaje.setIdoficinadestino(dataViaje.getIdoficinadestino());
        viaje.setSerecogera(dataViaje.getSerecogera());
        viaje.setSeentregara(dataViaje.getSeentregara());
        viaje.setIdunidad(dataViaje.getIdunidad());
        viaje.setTipounidad(dataViaje.getTipounidad());
        viaje.setIdremolque(dataViaje.getIdremolque());
        viaje.setIdoperador(dataViaje.getIdoperador());
        viaje.setEstatus(dataViaje.getEstatus());
        viaje.setObservaciones(dataViaje.getObservaciones());
        viaje.setFechamod(dataViaje.getFechamod());
        viaje.setHoramod(dataViaje.getHoramod());
        viaje.setFechaviaje(dataViaje.getFechaviaje());
        viaje.setHoraviaje(dataViaje.getHoramod());
        viaje.setGdiesel(dataViaje.getGdiesel());
        viaje.setViaticos(dataViaje.getViaticos());
        viaje.setExcedente(dataViaje.getExcedente());
        viaje.setCasetas(dataViaje.getCasetas());
        viaje.setNumerocircuito(dataViaje.getNumerocircuito());
        viaje.setSerie(dataViaje.getSerie());
        viaje.setFechacarga(dataViaje.getFechacarga());
        viaje.setHoracarga(dataViaje.getHoracarga());
        viaje.setGdieselporofidest(dataViaje.getGdieselporofidest());
        viaje.setCasetasporofidest(dataViaje.getCasetasporofidest());
     
        boolean inserted = documentacionRepository.updateViajes(dataViaje, DBPRUEBA);
        if(inserted) {
            return dataViaje;
        }
        return null;
    }

    @Override
    public Viajes getViaje(long idViaje, String idOficina) {
        Servidores server = utilitiesRepository.getLinkedServerByOfice("1144");
        Viajes response = documentacionRepository.getViaje(idViaje, idOficina, DBPRUEBA);
        if(response != null)
            return response;
        return null;
    }
    
    @Override
    public List<Viajes> filterViajes (String table, int idEsquema, int idesquemagasto, int tipounidad,int tiporuta, int idruta, String idOficinaDocumenta) {
        Servidores server = utilitiesRepository.getLinkedServerByOfice(idOficinaDocumenta);
        List<Viajes> response = documentacionRepository.filterViajes(table, idEsquema, idesquemagasto, tipounidad, tiporuta, idruta, DBPRUEBA);
        if(response != null)
            return response;
        return null;
    }
    
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
    @Override
    public FoliosGuias getFolioGuia(int idFolio, String idOficinaDocumenta) {
        Servidores server = utilitiesRepository.getLinkedServerByOfice(idOficinaDocumenta);
        FoliosGuias response = documentacionRepository.getFolioGuia(idFolio, DBPRUEBA);  
      
        return response;
    }
    
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
    @Override
    public Boolean updateFolioGuia( String noFolioGuia, String linkedServer) {
        String idOficina = noFolioGuia.substring(0,4);
        String oldNoFolioGuia = noFolioGuia.substring(5);
        String newNoFolioGuia = Integer.toString(Integer.valueOf(oldNoFolioGuia) + 1) ;
        String nextClaveFolio = StringUtils.leftPad(newNoFolioGuia, 7, "0");
        nextClaveFolio = idOficina.concat(nextClaveFolio);
        boolean response = documentacionRepository.updateFolioGuia(nextClaveFolio, DBPRUEBA); 
        return response;
    }
    
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
    @Override
    public Guias insertGuia(Guias dataGuia) throws Exception {
        
        Servidores server = utilitiesRepository.getLinkedServerByOfice(dataGuia.getIdoficina());
        boolean inserted = documentacionRepository.insertGuia(dataGuia, DBPRUEBA);
        
        if(inserted) {
            updateFolioGuia(dataGuia.getNoGuia(), DBPRUEBA);
            return dataGuia;
        }
        return null;
    }
    
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
    @Override
    public Boolean insertImporteGuia(ImporteGuia dataImporte, String idoficinaDocumenta ) throws Exception {
        
        Servidores server = utilitiesRepository.getLinkedServerByOfice(idoficinaDocumenta);
        boolean inserted = documentacionRepository.insertImporteGuia(dataImporte, DBPRUEBA);
        System.out.println(inserted);
        if(inserted) {
          
            return inserted;
        }
        return false;
    }
    
    
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
    @Override
    public Boolean insertTalonGuia(List<TgCustom> dataGuia ) {
          
        Servidores server = utilitiesRepository.getLinkedServerByOfice(dataGuia.get(0).getIdOficinaDocumenta());
        int mes = Calendar.getInstance().get(Calendar.MONTH)+1;
        int anio = Calendar.getInstance().get(Calendar.YEAR);
        String castedMes = Integer.toString(mes);
        String castesAnio = Integer.toString(anio);
        String mesAnio = castedMes.concat(castesAnio);
        
        int size = dataGuia.size();   
        int[] error = {};
        dataGuia.forEach(item -> {
            
            try {
              boolean response = documentacionRepository.insertTalonGuia(item, mesAnio, DBPRUEBA);
              if(!response) {
                  throw new Exception("No fue posible agregar la guia: "+item.getNoGuia() + " Y talon"+item.getClaTalon());
              }
            } catch (Exception e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        });
      
        return true;
    }

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
    @Override
    public Boolean insertGuiaViaje(Guiaviaje dataViajeGuia) throws Exception {
        Servidores server = utilitiesRepository.getLinkedServerByOfice(dataViajeGuia.getIdoficinaguia());
        Personal personal = utilitiesRepository.getPersonalByIdUsuario(dataViajeGuia.getIdpersonal());
        int idPersonal = personal.getIdpersonal();
        dataViajeGuia.setIdpersonal(idPersonal);   
        boolean response = documentacionRepository.insertGuiaViaje(dataViajeGuia, DBPRUEBA);
        if(!response)
           return false;
        return true;
    }

    @Override
    public Guias updateGuia(Guias dataGuia) {
        Servidores server = utilitiesRepository.getLinkedServerByOfice("1144");
        Guias guia = getGuia(dataGuia.getNoGuia(), dataGuia.getTabla());
        guia.setNoGuia(dataGuia.getNoGuia());
        guia.setTabla(dataGuia.getTabla());
        guia.setStatus(dataGuia.getStatus());
        guia.setIdoficina(dataGuia.getIdoficina());
        guia.setFechamod(dataGuia.getFechamod());
        guia.setHoramod(dataGuia.getHoramod());
      
        boolean inserted = documentacionRepository.updateGuia(guia, DBPRUEBA);
        if(inserted) {
            return guia;
        }
        return null;
    }

    @Override
    public Guias getGuia(String noGuia, String tabla) {
        Servidores server = utilitiesRepository.getLinkedServerByOfice("1144");
        Guias response = documentacionRepository.getGuia(noGuia, tabla, DBPRUEBA);
        if(response != null)
            return response;
        return null;
    }

    @Override
    public Boolean insertGuMesAnio(GuMesAnioCustom dataGuia) {
        Servidores server = utilitiesRepository.getLinkedServerByOfice("1144");
        boolean inserted = documentacionRepository.insertGuMesAnio(dataGuia, DBPRUEBA);
        if(inserted) {
            return inserted;
        }
        return null;
    }

    @Override
    public Boolean updateGuMesAnio(GuMesAnioCustom dataGuiaMesAnio) throws Exception{
        Servidores server = utilitiesRepository.getLinkedServerByOfice(dataGuiaMesAnio.getIdoficinadocumenta());
        GuMesAnio guMesAnio = getGuMesAnio(dataGuiaMesAnio.getNoGuia(), dataGuiaMesAnio.getTabla(), dataGuiaMesAnio.getIdoficinadocumenta());
        guMesAnio.setNoGuia(dataGuiaMesAnio.getNoGuia());
        guMesAnio.setUnidad(dataGuiaMesAnio.getUnidad());
        guMesAnio.setPlacas(dataGuiaMesAnio.getPlacas());
        guMesAnio.setIdoperador(dataGuiaMesAnio.getIdoperador());
        guMesAnio.setRemolque(dataGuiaMesAnio.getRemolque());
        guMesAnio.setOrigen(dataGuiaMesAnio.getOrigen());
        guMesAnio.setDestino(dataGuiaMesAnio.getDestino());
        guMesAnio.setDespacho(dataGuiaMesAnio.getUnidad());
        
//        Personal personal = utilitiesRepository.getPersonalByIdUsuario(dataGuiaMesAnio.getIdpersonal());
//        int idPersonal = personal.getIdpersonal();
        guMesAnio.setIdpersonal(dataGuiaMesAnio.getIdpersonal());
        guMesAnio.setIdoficina(dataGuiaMesAnio.getIdoficina());
        guMesAnio.setMoneda(dataGuiaMesAnio.getMoneda());
        guMesAnio.setConversion(dataGuiaMesAnio.getConversion());
        guMesAnio.setFecha(dataGuiaMesAnio.getFecha());
        guMesAnio.setHora(dataGuiaMesAnio.getHora());
        guMesAnio.setStatus(dataGuiaMesAnio.getStatus());
        Boolean response = documentacionRepository.updateGuMesAnio(guMesAnio,dataGuiaMesAnio.getTabla(), DBPRUEBA);
        if(response != null)
            return response;
        return null;
    }

    @Override
    public GuMesAnio getGuMesAnio(String noGuia, String tabla, String idOficinaDocumenta) {
        Servidores server = utilitiesRepository.getLinkedServerByOfice(idOficinaDocumenta);
        GuMesAnio response = documentacionRepository.getGuMesAnio(noGuia,tabla, DBPRUEBA);
        if(response != null)
            return response;
        return null;
    }

    @Override
    public List<OperadorCustom> getOperador(String unidad, int tipoUnidad) {
       
       return documentacionRepository.getOperador(unidad, tipoUnidad);
       
    }



   
    
   

   
    
    
}
