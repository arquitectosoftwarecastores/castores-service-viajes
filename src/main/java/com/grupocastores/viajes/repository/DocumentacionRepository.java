package com.grupocastores.viajes.repository;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import org.springframework.stereotype.Repository;

import com.grupocastores.commons.inhouse.TalonCustomResponse;

import com.grupocastores.commons.inhouse.CcpRemolque;
import com.grupocastores.commons.inhouse.CcpRemolqueExterno;
import com.grupocastores.commons.inhouse.DetaCo;
import com.grupocastores.commons.inhouse.EspecificacionTalon;
import com.grupocastores.commons.inhouse.FolioDos;
import com.grupocastores.commons.inhouse.FoliosGuias;
import com.grupocastores.commons.inhouse.GuMesAnio;
import com.grupocastores.commons.inhouse.GuMesAnioCustom;
import com.grupocastores.commons.inhouse.GuiaViajeCustom;
import com.grupocastores.commons.inhouse.Guias;
import com.grupocastores.commons.inhouse.ImporteGuia;
import com.grupocastores.commons.inhouse.OperadorCustom;
import com.grupocastores.commons.inhouse.RemolqueInternoCustom;
import com.grupocastores.commons.inhouse.TablaTalonesOficina;
import com.grupocastores.commons.inhouse.TgCustom;
import com.grupocastores.commons.inhouse.ViajeEsquemaGasto;
import com.grupocastores.commons.oficinas.Guiaviaje;
import com.grupocastores.commons.oficinas.Talones;
import com.grupocastores.commons.oficinas.Viajes;



@Repository
public class DocumentacionRepository extends UtilitiesRepository{
    
    
    @PersistenceContext
    private EntityManager entityManager;
    
    static final String queryFindTalones =
            "SELECT * FROM OPENQUERY(%s, ' SELECT * FROM ( SELECT tr.cla_talon AS clatalon, tr.nomorigen, tr.calleorigen, tr.nomdestino, tr.calledestino, et.idesquema, et.idnegociacion, et.idcliente, et.idoficina, tr.importeseguro,tr.recoleccion, tr.entrega, tr.maniobras, tr.ferry, tr.revac, tr.otroscargos, tr.gps, tr.importesubtotal, tr.importeiva, tr.importeiva_ret AS importeivaret,tr.otras_lineas AS otraslineas, tr.importetotal, tr.val_decl AS valdecl FROM talones.tr%s tr  INNER JOIN talones.especificacion_talon et ON tr.cla_talon = et.cla_talon INNER JOIN talones.ajustesgenerales taj ON  (tr.idofirte = taj.idlugarorigen OR tr.idofirte = taj.oficinaajusta) AND (tr.idofidest = taj.idlugardestino OR tr.idofidest = taj.oficinaajusta) WHERE tr.idclasificaciondoc = 2 AND tr.no_guia IS NULL AND tr.tipounidad = %s AND et.idesquema = %s AND et.idcliente = %s AND et.idoficina = \"%s\" AND tr.idcdrec IN (%s) AND tr.idcddes IN (%s)  ORDER BY taj.porcentaje DESC ) AS tem GROUP BY tem.clatalon');";
    
    static final String queryGetEspecificacionTalon =
            "SELECT * FROM OPENQUERY(%s, 'SELECT tr.cla_talon, tet.idesquema, tr.tipounidad, tr.idcddes, tr.idcdrec FROM talones.tr%s tr INNER JOIN talones.especificacion_talon tet ON tr.cla_talon = tet.cla_talon WHERE tr.tp_dc = 1 AND tr.no_guia IS NULL  AND tr.cla_talon = \"%s\";');";
    
    static final String queryGetTablaTalon =
            "SELECT * FROM OPENQUERY(%s, 'SELECT * FROM talones.talones t WHERE t.cla_talon = \"%s\";');";
    
    
    static final String queryDetaCoSumatoria =
             "SELECT * FROM OPENQUERY(%s, ' SELECT cla_talon AS clatalon,SUM(bultos) AS bultos, empaque, que_contiene AS quecontiene, SUM(peso_total) AS pesototal, SUM(flete) AS flete, idpromocion, preciopromocion, preciosinpromocion  FROM talones.detaco where cla_talon = \"%s\" GROUP BY cla_talon');";
    
    static final String queryGetViaje =
             "SELECT * FROM OPENQUERY(%s, 'SELECT * FROM talones.viajes v WHERE v.idviaje = %s AND v.idoficina = \"%s\";');";
    
    static final String queryGetViajeEsquemaGasto =
            "SELECT * FROM OPENQUERY(%s, 'SELECT * FROM talones.viajes_esquema_gasto v WHERE v.idviaje = %s ;');";
    
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
            "SELECT * FROM OPENQUERY(%s, 'SELECT * FROM talones.guias v WHERE v.no_guia = \"%s\" ;');";
       
    static final String queryFindFolioViajes =
          "SELECT * FROM OPENQUERY(%s, 'SELECT tipofolio, idfolio, clavefolio, descripcion FROM talones.foliosdos WHERE tipofolio = \"%s\" ;');";
    
    static final String queryFindFolioGuia =
            "SELECT * FROM OPENQUERY(%s, 'SELECT * FROM talones.foliosguias WHERE idfolio = \"%s\" ;');";

    static final String queryFilterViajes =
            "SELECT * FROM OPENQUERY(%s, 'SELECT tv.* FROM talones.viajes tv INNER JOIN talones.viajes_esquema_gasto tve ON tv.idviaje = tve.idviaje INNER JOIN talones.viajes_esquema_gasto tveg ON tv.idviaje = tveg.idviaje INNER JOIN talones.guiaviaje tgv ON tv.idviaje = tgv.idviaje INNER JOIN talones.guias tg ON tgv.no_guia = tg.no_guia INNER JOIN talones.tg%s tgma ON tg.no_guia = tgma.no_guia INNER JOIN talones.talones tt ON tgma.cla_talon = tt.cla_talon INNER JOIN talones.especificacion_talon tet ON tgma.cla_talon =tet.cla_talon WHERE tet.idesquema = %s AND tve.idesquemagasto = %s AND tv.tipounidad = %s AND tv.tiporuta = %s AND tv.idruta = %s GROUP BY tv.idviaje ;');";
    
    static final String queryGetRemolqueInterno =
            "SELECT * FROM OPENQUERY(%s, 'select idremolque, placas from camiones.remolques rem where rem.`status` = 1 AND idremolque = %s ;');";
    
    static final String queryGetRemolqueExterno =
            "SELECT * FROM OPENQUERY(%s, 'select idremolqueext AS idremolque, empresa AS placas from camiones.entradas_remolquesext ORDER BY fechamod DESC ;');";

    static final String queryTalonesTrGuia =
            "SELECT * FROM OPENQUERY(%s, 'SELECT tr.cla_talon AS clatalon, tr.nomorigen, tr.calleorigen, tr.nomdestino, tr.calledestino, et.idesquema, et.idnegociacion, et.idcliente, et.idoficina, tr.importeseguro,tr.recoleccion, tr.entrega, tr.maniobras, tr.ferry, tr.revac, tr.otroscargos, tr.gps, tr.importesubtotal, tr.importeiva, tr.importeiva_ret AS importeivaret,tr.otras_lineas AS otraslineas, tr.importetotal, tr.val_decl AS valdecl FROM talones.tr%s tr INNER JOIN talones.especificacion_talon et ON tr.cla_talon = et.cla_talon WHERE tr.cla_talon = \"%s\"; ');";
    
    static final String queryTalonesGuia =
            "SELECT * FROM OPENQUERY(%s, 'SELECT * FROM talones.tg%s tg WHERE no_guia = \"%s\"; ');";
    
    static final String queryGetGuiasViaje =
            "SELECT * FROM OPENQUERY(%s, 'SELECT tgv.*, tg.tabla FROM talones.guiaviaje tgv INNER JOIN talones.guias tg ON tgv.no_guia = tg.no_guia  WHERE tgv .idviaje = \"%s\"; ');";
    
    
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
    
    public boolean insertViajeEsquema(ViajeEsquemaGasto dataViajeEsquema, String linkedServer) {
        String queryCreateViajes ="INSERT INTO OPENQUERY("+ linkedServer +", "
                + "'SELECT idviaje, idesquemagasto"
                + " FROM talones.viajes_esquema_gasto LIMIT 1') VALUES('"+dataViajeEsquema.getIdviaje()+"', '"+dataViajeEsquema.getIdesquemagasto()+"')";
        
        if (executeStoredProcedure(queryCreateViajes) == false)
           return false; 
        return true;    
    }
    
    public Boolean updateViajeEsquema(ViajeEsquemaGasto dataViajeEsquema, String linkedServer) {
        String queryCreateViajes = "EXEC ('UPDATE talones.viajes_esquema_gasto  SET idesquemagasto = \""+dataViajeEsquema.getIdesquemagasto()+"\" WHERE idviaje = \""+dataViajeEsquema.getIdviaje()+"\"') AT "+ linkedServer +";";
        
        if (executeStoredProcedure(queryCreateViajes) == false)
           return false; 
        return true;
    }

    
    public ViajeEsquemaGasto getViajeEsquema(long idViaje, String linkedServer) {
        Query query = entityManager.createNativeQuery(String.format(queryGetViajeEsquemaGasto,
                linkedServer, idViaje),
                ViajeEsquemaGasto.class
          );
        ViajeEsquemaGasto resultList = (ViajeEsquemaGasto) query.getResultList().get(0);
        if(resultList == null)
            return null;
        return resultList;
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
        String queryCreateViajes ="INSERT INTO OPENQUERY("+ linkedServer +", "
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
    
    public Guias getGuia(String noGuia, String linkedServer) {
        Query query = entityManager.createNativeQuery(String.format(queryGetGuia, linkedServer,
                noGuia),Guias.class
          );
        
        return (Guias) query.getResultList().get(0);
    }
    
    @SuppressWarnings("unchecked")
    //idesquema %s AND tev.idesquemagasto = %s AND tv.tipounidad = %s AND tv.tiporuta = %s AND tv.idruta = %s
    public List<Viajes> filterViajes(String table, int idEsquema, int idesquemagasto, int tipounidad, int tiporuta, int idruta, String linkedServer) {
        Query query = entityManager.createNativeQuery(String.format(
                queryFilterViajes,
                linkedServer,
                table, 
                idEsquema, 
                idesquemagasto,
                tipounidad,
                tiporuta,
                idruta
                ),Viajes.class
          );
        
        return (List<Viajes>) query.getResultList();
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
     * @param (TgCustom) dataGuia
     * @version 0.0.1
     * @author Oscar Eduardo Guerra Salcedo [OscarGuerra] 
     * @return Boolean
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
    
    @SuppressWarnings("unchecked")
    public List<TgCustom> getTalonesGuia(String noGuia, String tabla, String linkedServer) {
        Query query = entityManager.createNativeQuery(String.format(queryTalonesGuia,linkedServer,
                tabla, noGuia),TgCustom.class
          );
        return (List<TgCustom>) query.getResultList();    
    }
    
    public TalonCustomResponse getTalonesTrGuia(String claTalon, String tabla, String linkedServer) {
        Query query = entityManager.createNativeQuery(String.format(queryTalonesTrGuia,linkedServer,
                tabla, claTalon),TalonCustomResponse.class
          );
        return (TalonCustomResponse) query.getResultList().get(0);    
    }
    
    /**
     * insertComplementoCpRemolque: inserta detalle de talon guia.
     * 
     * @param (TgCustom) dataGuia
     * @version 0.0.1
     * @author Oscar Eduardo Guerra Salcedo [OscarGuerra] 
     * @return Boolean
     * @throws Exception 
     * @date 2022-10-18
     */
    @SuppressWarnings("unchecked")
    public Boolean insertComplementoCpRemolque(TgCustom dataGuia, String mesAnio, String linkedServer) throws Exception{
        String queryCreateViajes ="INSERT INTO OPENQUERY("+ linkedServer +", "
                + "' SELECT * FROM talones.tg"+mesAnio+" LIMIT 1') VALUES('"+dataGuia.getClaTalon()+"', '"+dataGuia.getNoGuia()+"' ,'"+dataGuia.getNorenglon()+"')";
        
        if (executeStoredProcedure(queryCreateViajes) == false)
           return false; 
        return true;
    }
    
    
    /**
     * updateTr: actualiza tabla tr
     * Si se van necesitando mas campos agregarlos por favor y actualizar los argumentos de los metodos o generar una entidad.
     * 
     * @param  GuMesAnio 
     * @param tabla
     * @param linkedServer
     * @version 0.0.1
     * @author Oscar Eduardo Guerra Salcedo [OscarGuerra] 
     * @return Boolean
     * @throws Exception 
     * @date 2022-10-21
     */
    public Boolean updateTr(String claTalon, String tabla, String noGuia, String linkedServer) {
        String queryCreateViajes = "EXEC ('UPDATE talones.tr"+tabla+" SET no_guia = \""+noGuia+"\"  WHERE cla_talon = \""+claTalon+"\"  ') AT "+ linkedServer +";";

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
    public boolean insertGuiaViaje(Guiaviaje dataViajeGuia, String linkedServer) {
        String queryCreateGuiaViaje ="INSERT INTO OPENQUERY("+ linkedServer +", "
                + "' SELECT * FROM talones.guiaviaje LIMIT 1') VALUES('"+dataViajeGuia.getIdviaje()+"', '"+dataViajeGuia.getIdoficina()+"' ,'"+dataViajeGuia.getNoGuia()+"', '"+dataViajeGuia.getIdoficinaguia()+"', '"+dataViajeGuia.getEstatusguia()+"', '"+dataViajeGuia.getConsecutivo()+"', '"+dataViajeGuia.getImpresionpreguia()+"', '"+dataViajeGuia.getImpresionguia()+"', '"+dataViajeGuia.getEstatus()+"', '"+dataViajeGuia.getIdpersonal()+"', '"+dataViajeGuia.getFechamod()+"', '"+dataViajeGuia.getHoramod()+"', '"+dataViajeGuia.getTotalguia()+"', '"+dataViajeGuia.getIdoficinadeposito()+"', '"+dataViajeGuia.getTotaldeposito()+"', '"+dataViajeGuia.getOperacionguia()+"', '"+dataViajeGuia.getIdoficinadestino()+"', '"+dataViajeGuia.getVisitada()+"' )";
        
        if (executeStoredProcedure(queryCreateGuiaViaje) == false)
           return false; 
        return true;
    }
    
    public Boolean updateGuiaViaje(Guiaviaje dataGuiaViaje, String linkedServer) {
        String queryCreateViajes = "EXEC ('UPDATE talones.guiaviaje SET estatusguia = \""+dataGuiaViaje.getEstatusguia()+"\", consecutivo = \""+dataGuiaViaje.getConsecutivo()+"\" , impresionpreguia =\""+dataGuiaViaje.getImpresionpreguia()+"\", impresionguia = \""+dataGuiaViaje.getImpresionguia()+"\", estatus = \""+dataGuiaViaje.getEstatus()+"\", idpersonal = \""+dataGuiaViaje.getIdpersonal()+"\", fechamod = \""+dataGuiaViaje.getFechamod()+"\", horamod = \""+dataGuiaViaje.getHoramod()+"\", totalguia = \""+dataGuiaViaje.getTotalguia()+"\", idoficinadeposito =\""+dataGuiaViaje.getIdoficinadeposito()+"\", totaldeposito = \""+dataGuiaViaje.getTotaldeposito()+"\", operacionguia = \""+dataGuiaViaje.getOperacionguia()+"\", idoficinadestino = \""+dataGuiaViaje.getIdoficinadestino()+"\", visitada = \""+dataGuiaViaje.getVisitada()+"\" WHERE idviaje = \""+dataGuiaViaje.getIdviaje()+"\" AND idoficina = \""+dataGuiaViaje.getIdoficina()+"\" AND no_guia= \""+dataGuiaViaje.getNoGuia()+"\" ') AT "+ linkedServer +";";
        
        if (executeStoredProcedure(queryCreateViajes) == false)
           return false; 
        return true;
    }

    @SuppressWarnings("unchecked")
    public List<GuiaViajeCustom> getGuiasViaje(int idViaje, String linkedServer) {
        Query query = entityManager.createNativeQuery(String.format(queryGetGuiasViaje,linkedServer,
                idViaje),GuiaViajeCustom.class
          );
        return (List<GuiaViajeCustom>) query.getResultList();  
    }
    /**
     * updateGuMesAnio: actualiza tabla gumesanio.
     * 
     * @param  GuMesAnio 
     * @param tabla
     * @param linkedServer
     * @version 0.0.1
     * @author Oscar Eduardo Guerra Salcedo [OscarGuerra] 
     * @return Boolean
     * @throws Exception 
     * @date 2022-10-21
     */
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
                + " 'SELECT * FROM talones.gu"+dataGuia.getTabla()+" LIMIT 1') VALUES('"+dataGuia.getNoGuia()+"', '"+dataGuia.getUnidad()+"','"+dataGuia.getPlacas()+"','"+dataGuia.getIdoperador()+"','"+dataGuia.getRemolque()+"','"+dataGuia.getOrigen()+"','"+dataGuia.getDestino()+"','"+dataGuia.getDespacho()+"','"+dataGuia.getIdpersonal()+"','"+dataGuia.getIdoficina()+"','"+dataGuia.getMoneda()+"','"+dataGuia.getConversion()+"','"+dataGuia.getFecha()+"','"+dataGuia.getHora()+"','"+dataGuia.getStatus()+"','"+dataGuia.getIdcliente()+"','"+dataGuia.getIdproducto()+"','"+dataGuia.getCita()+"','"+dataGuia.getTipounidad()+"' )";
        
        if (executeStoredProcedure(queryCreateViajes) == false)
           return false; 
        return true;  
    }
    
    /**
     * insertImporteGuia: inserta importe de guia.
     * 
     * @param (Guias) dataViaje
     * @version 0.0.1
     * @author Oscar Eduardo Guerra Salcedo [OscarGuerra] 
     * @return Guias
     * @throws Exception 
     * @date 2022-10-19
     */
    public Boolean insertImporteGuia(ImporteGuia dataImporte, String linkedServer) throws Exception{
        System.out.println(dataImporte);
        String queryCreateViajes ="INSERT INTO OPENQUERY("+ linkedServer +", "
                + "'SELECT * "
                + " FROM talones.importeguias LIMIT 1') VALUES('"+dataImporte.getNo_guia()+"', '"+dataImporte.getTotalflete()+"','"+dataImporte.getTotalseguro()+"','"+dataImporte.getTotalcapufe()+"','"+dataImporte.getTotalrecoleccion()+"','"+dataImporte.getTotalentrega()+"','"+dataImporte.getTotalmaniobras()+"','"+dataImporte.getTotalcdp()+"','"+dataImporte.getTotalferry()+"','"+dataImporte.getTotaldescuento()+"','"+dataImporte.getTotalolrvc()+"','"+dataImporte.getTotalotros()+"','"+dataImporte.getTotalgps()+"','"+dataImporte.getTotalsubtotal()+"','"+dataImporte.getTotaliva()+"','"+dataImporte.getTotalretencion()+"','"+dataImporte.getTotalotrasLineas()+"','"+dataImporte.getTotaltotal()+"','"+dataImporte.getTotalvalordeclarado()+"','"+dataImporte.getTotalbultos()+"','"+dataImporte.getTotalpeso()+"','"+dataImporte.getTotaldetalones()+"','"+dataImporte.getTotaldetalonesocurre()+"', '"+dataImporte.getEscompleto()+"')";
        
        System.out.println(queryCreateViajes);
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
    
    /**
     * getRemolqueInterno: Obtiene remolques internos
     * 
     * @param idRemolque 
     * @return List<RemolqueInternoCustom>
     * @author OscarEduardo Guerra Salcedo [OscarGuerra]
     * @date 2022-09-08
     */
    @SuppressWarnings("unchecked")
    public List<RemolqueInternoCustom> getRemolqueInterno(int idRemolque, String linkedServer) {
        Query query = entityManager.createNativeQuery(String.format(queryGetRemolqueInterno,
                linkedServer,
                idRemolque),
                RemolqueInternoCustom.class
          );
       return ( List<RemolqueInternoCustom>) query.getResultList();
    }
    
    /**
     * getRqmolqueExterno: Obtiene remolques externos
     * 
     * @param idRemolque 
     * @return List<RemolqueInternoCustom>
     * @author OscarEduardo Guerra Salcedo [OscarGuerra]
     * @date 2022-09-08
     */
    public List<RemolqueInternoCustom> getRemolqueExterno(int idRemolque, String linkedServer) {
        Query query = entityManager.createNativeQuery(String.format(queryGetRemolqueExterno,
                linkedServer),
                RemolqueInternoCustom.class
          );
       return ( List<RemolqueInternoCustom>) query.getResultList();
    }
    
    /**
     * getEspecificacionTalon: Obtiene especificacion de talon 
     * 
     * @param claTalon
     * @param idOficinaDocumenta
     * @return EspecificacionTalon
     * @author OscarEduardo Guerra Salcedo [OscarGuerra]
     * @date 2022-11-14
     */
    public EspecificacionTalon getEspecificacionTalon(String claTalon, String mesanio, String linkedServer) {
        Query query = entityManager.createNativeQuery(String.format(queryGetEspecificacionTalon,
                linkedServer,
                mesanio,
                claTalon
                ),
                EspecificacionTalon.class
          );
       return (EspecificacionTalon) query.getResultList().get(0);
    }
    
    /**
     * getTablaTalon: Obtiene la tabla y registro de talon 
     * 
     * @param claTalon
     * @param idOficinaDocumenta
     * @return Talones
     * @author OscarEduardo Guerra Salcedo [OscarGuerra]
     * @date 2022-11-14
     */
    public TablaTalonesOficina getTablaTalon(String claTalon, String linkedServer) {
        Query query = entityManager.createNativeQuery(String.format(queryGetTablaTalon,
                linkedServer,
                claTalon
                ),
                TablaTalonesOficina.class
          );
        TablaTalonesOficina resultList = (TablaTalonesOficina) query.getResultList().get(0);
        if(resultList == null)
            return null;
        return resultList;
      
    }
    
    /**
     * insertCcpRemolque: inserta registro en complementocpremolque.
     * 
     * @param (CcpRemolque) dataViaje
     * @param (String) linkedServer
     * @version 0.0.1
     * @author Oscar Eduardo Guerra Salcedo [OscarGuerra] 
     * @return Guias
     * @throws Exception 
     * @date 2022-11-16
     */
    public Boolean insertCcpRemolque(CcpRemolque dataRemolque, String linkedServer) throws Exception{
        String queryCreateCcpRemolque ="INSERT INTO OPENQUERY("+ linkedServer +", "
                + "'SELECT *"
                + " FROM cfdinomina.complementocp_remolque LIMIT 1') VALUES('"+dataRemolque.getIdperFac()+"', '"+dataRemolque.getConsCcp()+"','"+dataRemolque.getSubtipoRem()+"','"+dataRemolque.getDescripcion()+"', '"+dataRemolque.getPlaca()+"')";
        
        if (executeStoredProcedure(queryCreateCcpRemolque) == false)
           return false; 
        return true;        
    }
    
    /**
     * insertCcpRemolqueExterno: inserta registro en complementocpremolque.
     * 
     * @param (CcpRemolqueExterno) dataViaje
     * @param (String) linkedServer
     * @version 0.0.1
     * @author Oscar Eduardo Guerra Salcedo [OscarGuerra] 
     * @return Guias
     * @throws Exception 
     * @date 2022-11-16
     */
    public Boolean insertCcpRemolqueExterno(CcpRemolqueExterno dataRemolque, String linkedServer) throws Exception{
        String queryCreateCcpRemolque ="INSERT INTO OPENQUERY("+ linkedServer +", "
                + "'SELECT *"
                + " FROM cfdinomina.complementocp_remolque_ext LIMIT 1') VALUES('"+dataRemolque.getIdViaje()+"', '"+dataRemolque.getIdOficinaViaje()+"','"+dataRemolque.getPlacas()+"','"+dataRemolque.getFechamod()+"', '"+dataRemolque.getHoramod()+"')";
        
        if (executeStoredProcedure(queryCreateCcpRemolque) == false)
           return false; 
        return true;        
    }

   


    
   

    
   
    

   
   
}
