package com.grupocastores.viajes.repository;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import org.springframework.stereotype.Repository;

import com.grupocastores.commons.inhouse.TalonCustomResponse;
import com.grupocastores.commons.inhouse.DetaCo;
import com.grupocastores.commons.inhouse.FolioDos;
import com.grupocastores.commons.inhouse.FoliosGuias;
import com.grupocastores.commons.inhouse.GuMesAnio;
import com.grupocastores.commons.inhouse.GuMesAnioCustom;
import com.grupocastores.commons.inhouse.Guias;
import com.grupocastores.commons.inhouse.OperadorCustom;
import com.grupocastores.commons.inhouse.TgCustom;
import com.grupocastores.commons.oficinas.Guiaviaje;
import com.grupocastores.commons.oficinas.Viajes;



@Repository
public class DocumentacionRepository extends UtilitiesRepository{
    
    
    @PersistenceContext
    private EntityManager entityManager;
    
    static final String queryFindTalones =
            "SELECT * FROM OPENQUERY(%s, ' SELECT * FROM ( SELECT tr.cla_talon AS clatalon, tr.nomorigen, tr.calleorigen, tr.nomdestino, tr.calledestino, et.idesquema, et.idnegociacion, et.idcliente, et.idoficina, tr.importeseguro,tr.recoleccion, tr.entrega, tr.maniobras, tr.ferry, tr.revac, tr.otroscargos, tr.gps, tr.importesubtotal, tr.importeiva, tr.importeiva_ret AS importeivaret,tr.otras_lineas AS otraslineas, tr.importetotal, tr.val_decl AS valdecl FROM talones.tr%s tr  INNER JOIN talones.especificacion_talon et ON tr.cla_talon = et.cla_talon INNER JOIN talones.ajustesgenerales taj ON  (tr.idofirte = taj.idlugarorigen OR tr.idofirte = taj.oficinaajusta) AND (tr.idofidest = taj.idlugardestino OR tr.idofidest = taj.oficinaajusta) WHERE tr.idclasificaciondoc = 2 AND tr.no_guia IS NULL AND tr.tipounidad = %s AND et.idesquema = %s AND et.idcliente = %s AND et.idoficina = \"%s\" AND tr.idcdrec IN (%s) AND tr.idcddes IN (%s)  ORDER BY taj.porcentaje DESC ) AS tem GROUP BY tem.clatalon');";
    
    static final String queryDetaCoSumatoria =
             "SELECT * FROM OPENQUERY(%s, ' SELECT cla_talon AS clatalon,SUM(bultos) AS bultos, empaque, que_contiene AS quecontiene, SUM(peso_total) AS pesototal, SUM(flete) AS flete, idpromocion, preciopromocion, preciosinpromocion  FROM talones.detaco where cla_talon = \"%s\" GROUP BY cla_talon');";
           
    static final String queryGetViaje =
             "SELECT * FROM OPENQUERY(%s, 'SELECT * FROM talones.viajes v WHERE v.idviaje = %s AND v.idoficina = \"%s\";');";
    
    static final String queryGetGuMesAnio =
             "SELECT * FROM OPENQUERY(%s, 'SELECT * FROM talones.gu%s tr WHERE tr.no_guia = \"%s\" ;');";
    
    static final String queryGetOperador =
            "SELECT * FROM OPENQUERY(" + DB_23 + ", 'SELECT c.unidad, u.tipounidad, o.idpersonal AS operadoroperador, CONCAT_WS(\" \",pe.nombre,pe.apepaterno,pe.apematerno) AS nombre, pe.status"
            + " FROM camiones.camiones c"
            + " INNER JOIN camiones.unidades u ON c.unidad = u.unidad"
            + " INNER JOIN camiones.operadores o ON o.unidad = c.unidad"
            + " LEFT JOIN personal.personal pe ON pe.idpersonal = o.idpersonal AND pe.status = 1"
            + " WHERE c.unidad = \"%s\"  AND u.tipounidad = %s AND pe.status >= 0 "
            + " UNION"
            + " SELECT c.unidad, u.tipounidad, os.idoperador,  CONCAT_WS(\" \",pe.nombre,pe.apepaterno,pe.apematerno) AS nombre, pe.status"
            + " FROM camiones.camiones c "
            + " INNER JOIN camiones.unidades u ON c.unidad = u.unidad"
            + " INNER JOIN camiones.operadores o ON o.unidad = c.unidad"
            + " LEFT JOIN bitacorasinhouse.operadores_secundarios_unidad os ON c.unidad = os.idunidad"
            + " LEFT JOIN personal.personal pe ON pe.idpersonal = os.idoperador AND pe.status = 1"
            + " WHERE c.unidad = \"%s\" AND  u.tipounidad = %s  AND pe.status >= 0 ;');";
        
    
    static final String queryGetGuia =
            "SELECT * FROM OPENQUERY(%s, 'SELECT * FROM talones.guias v WHERE v.no_guia = \"%s\" AND v.tabla = \"%s\";');";
       
    static final String queryFindFolioViajes =
          "SELECT * FROM OPENQUERY(%s, 'SELECT tipofolio, idfolio, clavefolio, descripcion FROM talones.foliosdos WHERE tipofolio = \"%s\" ;');";
    
    static final String queryFindFolioGuia =
            "SELECT * FROM OPENQUERY(%s, 'SELECT * FROM talones.`foliosguias` WHERE idfolio = \"%s\" ;');";

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
    @SuppressWarnings("unchecked")
    public List<TalonCustomResponse> findTalones( String mesAnio, int idEsquema, int tipoViaje, int tipoUnidad, int idCliente, String idOficinaCliente, String determinanteOrigen, String determinanteDestino, String linkedServer){
        
        Query query = entityManager.createNativeQuery(String.format(queryFindTalones,linkedServer,
                 mesAnio, tipoUnidad, idEsquema, idCliente, idOficinaCliente, determinanteOrigen, determinanteDestino),
                TalonCustomResponse.class
            );
        List<TalonCustomResponse> list = query.getResultList();
        return list;
        
    }
    
    /**
     * findTalones: Se consultan los detalles de el talon .
     * 
     * @param String cla_talon
   
     * @version 0.0.1
     * @author Oscar Eduardo Guerra Salcedo [OscarGuerra] 
     * @return Coordenada
     * @throws Exception 
     * @date 2022-11-07
     */
    @SuppressWarnings("unchecked")
    public DetaCo findDetacoSumatoria( String claTalon, String linkedServer){
        
        Query query = entityManager.createNativeQuery(String.format(queryDetaCoSumatoria,linkedServer,
                claTalon),
                DetaCo.class
            );
        return (DetaCo) query.getResultList().get(0);

        
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
    public FolioDos getFolioViaje(int idFolio, String linkedServer) {
        Query query = entityManager.createNativeQuery(String.format(queryFindFolioViajes,linkedServer,
                 idFolio),FolioDos.class
           );
       return (FolioDos) query.getResultList().get(0);       
    }
    
    /**
     * updateFolioViaje: incrementa idfolio y clavefolio para su proximo uso.
     * 
     * @param long idFolio
     * @param String claveFolio
     * @version 0.0.1
     * @author Oscar Eduardo Guerra Salcedo [OscarGuerra] 
     * @return Boolean
     * @throws Exception 
     * @date 2022-10-20
     */
    public Boolean updateFolioViaje(long idFolio, String claveFolio, String linkedServer) {
        String update = "EXEC ('UPDATE talones.foliosdos SET idfolio = \"" +idFolio+"\", clavefolio = \""+claveFolio+"\" WHERE tipofolio = 1 ' ) AT "+ linkedServer +";";
        
        if (executeStoredProcedure(update) == false)          
            return false;            
        return true;
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
    @SuppressWarnings("unchecked")
    public Boolean insertViaje(Viajes dataViaje, String linkedServer) throws Exception{
        String queryCreateViajes ="INSERT INTO OPENQUERY("+ linkedServer +", "
                + "' SELECT idviaje, idoficina, folio, idcatalogo_viajes, idruta, idoficinaorigen, idoficinadestino, serecogera, seentregara, idunidad, tipounidad, idremolque, "
                + " idoperador, idcliente, idoficinacliente, estatus, idautorizo, observaciones, idpersonal, fechamod, horamod, fechaviaje, horaviaje, tiporuta, gdiesel, viaticos, "
                + " excedente, casetas, numerocircuito, serie, fechacarga, horacarga, gdieselporofidest, casetasporofidest "
                + " FROM talones.viajes LIMIT 1') VALUES('"+dataViaje.getIdviaje()+"', '"+dataViaje.getIdoficina()+"' ,'"+dataViaje.getFolio()+"','"+dataViaje.getIdcatalogoViajes()+"','"+dataViaje.getIdruta()+"','"+dataViaje.getIdoficinaorigen()+"','"+dataViaje.getIdoficinadestino()+"','"+dataViaje.getSerecogera()+"','"+dataViaje.getSeentregara()+"',"
                + " '"+dataViaje.getIdunidad()+"','"+dataViaje.getTipounidad()+"','"+dataViaje.getIdremolque()+"','"+dataViaje.getIdoperador()+"','"+dataViaje.getIdcliente()+"','"+dataViaje.getIdoficinacliente()+"','"+dataViaje.getEstatus()+"','"+dataViaje.getIdautorizo()+"','"+dataViaje.getObservaciones()+"','"+dataViaje.getIdpersonal()+"','"+dataViaje.getFechamod()+"','"+dataViaje.getHoramod()+"',"
                + " '"+dataViaje.getFechaviaje()+"', '"+dataViaje.getHoraviaje()+"', '"+dataViaje.getTiporuta()+"', '"+dataViaje.getGdiesel()+"', '"+dataViaje.getViaticos()+"', '"+dataViaje.getExcedente()+"', '"+dataViaje.getCasetas()+"', '"+dataViaje.getNumerocircuito()+"','"+dataViaje.getSerie()+"', '"+dataViaje.getFechacarga()+"', '"+dataViaje.getHoracarga()+"' , "
                + " '"+dataViaje.getGdieselporofidest()+"', '"+dataViaje.getCasetasporofidest()+"')";
        
        if (executeStoredProcedure(queryCreateViajes) == false)
           return false; 
        return true;
    }
    
    /**
     * updateViajes: Actualiza un viaje.
     * 
     * @param (Viajes) dataViaje
     * @param (String) linkedServer
     * @version 0.0.1
     * @author Oscar Eduardo Guerra Salcedo [OscarGuerra] 
     * @return Boolean
     * @throws Exception 
     * @date 2022-10-24
     */
    public boolean updateViajes(Viajes dataViaje, String linkedServer) {
        
        String queryCreateViajes = "EXEC ('UPDATE talones.viajes SET idviaje = \""+dataViaje.getIdviaje()+"\",idoficina = \""+dataViaje.getIdoficina()+"\" , folio =\""+dataViaje.getFolio()+"\",idcatalogo_viajes = \""+dataViaje.getIdcatalogoViajes()+"\", idruta = \""+dataViaje.getIdruta()+"\",idoficinaorigen = \""+dataViaje.getIdoficinaorigen()+"\",idoficinadestino = \""+dataViaje.getIdoficinadestino()+"\",serecogera = \""+dataViaje.getSerecogera()+"\",seentregara = \""+dataViaje.getSeentregara()+"\", idunidad = \""+dataViaje.getIdunidad()+"\",tipounidad = \""+dataViaje.getTipounidad()+"\",idremolque = \""+dataViaje.getIdremolque()+"\",idoperador = \""+dataViaje.getIdoperador()+"\",idcliente =\""+dataViaje.getIdcliente()+"\",idoficinacliente = \""+dataViaje.getIdoficinacliente()+"\",estatus = \""+dataViaje.getEstatus()+"\",idautorizo = \""+dataViaje.getIdautorizo()+"\",observaciones = \""+dataViaje.getObservaciones()+"\",idpersonal = \""+dataViaje.getIdpersonal()+"\",fechamod = \""+dataViaje.getFechamod()+"\",horamod = \""+dataViaje.getHoramod()+"\", fechaviaje = \""+dataViaje.getFechaviaje()+"\",horaviaje = \""+dataViaje.getHoraviaje()+"\", tiporuta = \""+dataViaje.getTiporuta()+"\", gdiesel = \""+dataViaje.getGdiesel()+"\",viaticos = \""+dataViaje.getViaticos()+"\",excedente = \""+dataViaje.getExcedente()+"\",casetas = \""+dataViaje.getCasetas()+"\",numerocircuito = \""+dataViaje.getNumerocircuito()+"\", serie = \""+dataViaje.getSerie()+"\",fechacarga = \""+dataViaje.getFechacarga()+"\",horacarga = \""+dataViaje.getHoracarga()+"\" , gdieselporofidest = \""+dataViaje.getGdieselporofidest()+"\",casetasporofidest = \""+dataViaje.getCasetasporofidest()+"\" WHERE idviaje = "+dataViaje.getIdviaje()+" AND idoficina = \""+dataViaje.getIdoficina()+"\" ') AT "+ linkedServer +";";
        
        if (executeStoredProcedure(queryCreateViajes) == false)
           return false; 
        return true;

    }
    
    public Viajes getViaje(long idViaje, String idOficina, String db) {
        Query query = entityManager.createNativeQuery(String.format(queryGetViaje, db,
                idViaje, idOficina),Viajes.class
          );
        
       Viajes viaje = (Viajes) query.getResultList().get(0);
      return viaje; 
      
    }
    
    /**
     * insertGuia: inserta nueva guia.
     * 
     * @param (Guias) dataViaje
     * @version 0.0.1
     * @author Oscar Eduardo Guerra Salcedo [OscarGuerra] 
     * @return Guias
     * @throws Exception 
     * @date 2022-10-19
     */
    public Boolean insertGuia(Guias dataGuia, String linkedServer) throws Exception{
        String queryCreateViajes ="INSERT INTO OPENQUERY("+ DB_23 +", "
                + "'SELECT no_guia As noGuia, tabla, status, idoficina, fechamod, horamod"
                + " FROM talones.guias LIMIT 1') VALUES('"+dataGuia.getNoGuia()+"', '"+dataGuia.getTabla()+"','"+dataGuia.getStatus()+"','"+dataGuia.getIdoficina()+"','"+dataGuia.getFechamod()+"','"+dataGuia.getHoramod()+"')";
        
        if (executeStoredProcedure(queryCreateViajes) == false)
           return false; 
        return true;        
    }
    
    /**
     * updateViajes: Actualiza una guia.
     * 
     * @param (Viajes) dataViaje
     * @param (String) linkedServer
     * @version 0.0.1
     * @author Oscar Eduardo Guerra Salcedo [OscarGuerra] 
     * @return Boolean
     * @throws Exception 
     * @date 2022-10-24
     */
    public boolean updateGuia(Guias dataGuia, String linkedServer) {
        
        String queryCreateViajes = "EXEC ('UPDATE talones.guias SET no_guia = \""+dataGuia.getNoGuia()+"\",tabla = \""+dataGuia.getTabla()+"\" , status =\""+dataGuia.getStatus()+"\",idoficina = \""+dataGuia.getIdoficina()+"\", fechamod = \""+dataGuia.getFechamod()+"\",horamod = \""+dataGuia.getHoramod()+"\" WHERE no_guia = "+dataGuia.getNoGuia()+" AND tabla = \""+dataGuia.getTabla()+"\" ') AT "+ linkedServer +";";
        
        if (executeStoredProcedure(queryCreateViajes) == false)
           return false; 
        return true;

    }
    
    public Guias getGuia(String noGuia, String tabla, String db) {
        Query query = entityManager.createNativeQuery(String.format(queryGetGuia, db,
                noGuia, tabla),Guias.class
          );
        
        return (Guias) query.getResultList().get(0);
    }

    
    /**
     * getFolioGuia: Se consulta el proximo folio de guia
     * 
     * @param String idFolioViaje
     * @param String idOficinaDocumenta
     * @version 0.0.1
     * @author Oscar Eduardo Guerra Salcedo [OscarGuerra] 
     * @return folio String
     * @throws Exception 
     * @date 2022-10-18
     */ 
    public FoliosGuias getFolioGuia(int idFolio, String linkedServer) {
        Query query = entityManager.createNativeQuery(String.format(queryFindFolioGuia,linkedServer,
                 idFolio),FoliosGuias.class
           );
       return (FoliosGuias) query.getResultList().get(0);       
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
    public boolean updateFolioGuia(String nextNoFolioGuia, String linkedServer) {
        String update = "EXEC ('UPDATE talones.foliosguias SET nofolio = \"" +nextNoFolioGuia+"\" WHERE idfolio = 1  ' ) AT "+ linkedServer +";";
        
        if (executeStoredProcedure(update) == false)          
            return false;            
        return true;
    }
    
    /**
     * insertTalonGuia: inserta detalle de talon guia.
     * 
     * @param (Viajes) dataViaje
     * @version 0.0.1
     * @author Oscar Eduardo Guerra Salcedo [OscarGuerra] 
     * @return dataViaje Viajes
     * @throws Exception 
     * @date 2022-10-18
     */
    @SuppressWarnings("unchecked")
    public Boolean insertTalonGuia(TgCustom dataGuia, String mesAnio, String linkedServer) throws Exception{
        String queryCreateViajes ="INSERT INTO OPENQUERY("+ linkedServer +", "
                + "' SELECT * FROM talones.tg"+mesAnio+" LIMIT 1') VALUES('"+dataGuia.getClaTalon()+"', '"+dataGuia.getNoGuia()+"' ,'"+dataGuia.getNorenglon()+"')";
        
        if (executeStoredProcedure(queryCreateViajes) == false)
           return false; 
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
    public boolean insertGuiaViaje(Guiaviaje dataViajeGuia, String db) {
        String queryCreateGuiaViaje ="INSERT INTO OPENQUERY("+ db +", "
                + "' SELECT * FROM talones.guiaviaje LIMIT 1') VALUES('"+dataViajeGuia.getIdviaje()+"', '"+dataViajeGuia.getIdoficina()+"' ,'"+dataViajeGuia.getNoGuia()+"', '"+dataViajeGuia.getIdoficinaguia()+"', '"+dataViajeGuia.getEstatusguia()+"', '"+dataViajeGuia.getConsecutivo()+"', '"+dataViajeGuia.getImpresionpreguia()+"', '"+dataViajeGuia.getImpresionguia()+"', '"+dataViajeGuia.getEstatus()+"', '"+dataViajeGuia.getIdpersonal()+"', '"+dataViajeGuia.getFechamod()+"', '"+dataViajeGuia.getHoramod()+"', '"+dataViajeGuia.getTotalguia()+"', '"+dataViajeGuia.getIdoficinadeposito()+"', '"+dataViajeGuia.getTotaldeposito()+"', '"+dataViajeGuia.getOperacionguia()+"', '"+dataViajeGuia.getIdoficinadestino()+"', '"+dataViajeGuia.getVisitada()+"' )";
        
        if (executeStoredProcedure(queryCreateGuiaViaje) == false)
           return false; 
        return true;
    }

    public Boolean updateGuMesAnio(GuMesAnio dataGuiaMesAnio,String tabla, String linkedServer) {
       String queryCreateViajes = "EXEC ('UPDATE talones.gu"+tabla+" SET no_guia = \""+dataGuiaMesAnio.getNoGuia()+"\", unidad = \""+dataGuiaMesAnio.getUnidad()+"\" , placas =\""+dataGuiaMesAnio.getPlacas()+"\", idoperador = \""+dataGuiaMesAnio.getIdoperador()+"\", remolque = \""+dataGuiaMesAnio.getRemolque()+"\", origen = \""+dataGuiaMesAnio.getOrigen()+"\", destino = \""+dataGuiaMesAnio.getDestino()+"\", despacho = \""+dataGuiaMesAnio.getDespacho()+"\", idpersonal = \""+dataGuiaMesAnio.getIdpersonal()+"\", idoficina =\""+dataGuiaMesAnio.getIdoficina()+"\", moneda = \""+dataGuiaMesAnio.getMoneda()+"\", conversion = \""+dataGuiaMesAnio.getConversion()+"\", fecha = \""+dataGuiaMesAnio.getFecha()+"\", hora = \""+dataGuiaMesAnio.getHora()+"\", status = \""+dataGuiaMesAnio.getStatus()+"\"   WHERE no_guia = \""+dataGuiaMesAnio.getNoGuia()+"\"  ') AT "+ linkedServer +";";
        
        if (executeStoredProcedure(queryCreateViajes) == false)
           return false; 
        return true;
    }

    public GuMesAnio getGuMesAnio(String noGuia, String tabla, String linkedServer) {
        
        Query query = entityManager.createNativeQuery(String.format(queryGetGuMesAnio,
                linkedServer,tabla,noGuia),GuMesAnio.class
          );
      return (GuMesAnio) query.getResultList().get(0);   
      
    }

    public boolean insertGuMesAnio(GuMesAnioCustom dataGuia, String linkedServer) {
        String queryCreateViajes ="INSERT INTO OPENQUERY("+ linkedServer +", "
                + " 'SELECT * FROM talones.gu%s LIMIT 1') VALUES('"+dataGuia.getNoGuia()+"', '"+dataGuia.getUnidad()+"','"+dataGuia.getPlacas()+"','"+dataGuia.getIdoperador()+"','"+dataGuia.getRemolque()+"','"+dataGuia.getOrigen()+"','"+dataGuia.getDestino()+"','"+dataGuia.getDespacho()+"','"+dataGuia.getIdpersonal()+"','"+dataGuia.getIdoficina()+"','"+dataGuia.getMoneda()+"','"+dataGuia.getConversion()+"','"+dataGuia.getFecha()+"','"+dataGuia.getHora()+"','"+dataGuia.getStatus()+"' )";
        
        if (executeStoredProcedure(queryCreateViajes) == false)
           return false; 
        return true;  
    }

    @SuppressWarnings("unchecked")
    public List<OperadorCustom> getOperador(String unidad, int tipoUnidad) {
        Query query = entityManager.createNativeQuery(String.format(queryGetOperador,
                unidad, tipoUnidad, unidad, tipoUnidad),OperadorCustom.class
          );
      List<OperadorCustom> resultList = (List<OperadorCustom>) query.getResultList();
    return resultList;
    }

    public boolean insertImporteguias(GuMesAnioCustom dataGuia, String linkedServer) {
        String queryCreateViajes ="INSERT INTO OPENQUERY("+ linkedServer +", "
                + " 'SELECT * FROM talones.gu%s LIMIT 1') VALUES('"+dataGuia.getNoGuia()+"', '"+dataGuia.getUnidad()+"','"+dataGuia.getPlacas()+"','"+dataGuia.getIdoperador()+"','"+dataGuia.getRemolque()+"','"+dataGuia.getOrigen()+"','"+dataGuia.getDestino()+"','"+dataGuia.getDespacho()+"','"+dataGuia.getIdpersonal()+"','"+dataGuia.getIdoficina()+"','"+dataGuia.getMoneda()+"','"+dataGuia.getConversion()+"','"+dataGuia.getFecha()+"','"+dataGuia.getHora()+"','"+dataGuia.getStatus()+"' )";
        
        if (executeStoredProcedure(queryCreateViajes) == false)
           return false; 
        return true;  
    }
    

   
   
}
