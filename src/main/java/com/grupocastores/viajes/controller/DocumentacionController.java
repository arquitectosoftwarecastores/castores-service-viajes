package com.grupocastores.viajes.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.grupocastores.commons.inhouse.TalonCustomResponse;
import com.grupocastores.commons.ViajeEsquemaGasto;
import com.grupocastores.commons.inhouse.CcpRemolque;
import com.grupocastores.commons.inhouse.CcpRemolqueExterno;
import com.grupocastores.commons.inhouse.DetaCo;
import com.grupocastores.commons.inhouse.EspecificacionTalon;
import com.grupocastores.commons.inhouse.FolioDos;
import com.grupocastores.commons.inhouse.FoliosGuias;
import com.grupocastores.commons.inhouse.GuMesAnio;
import com.grupocastores.commons.inhouse.GuMesAnioCustom;
import com.grupocastores.commons.inhouse.Guias;
import com.grupocastores.commons.inhouse.ImporteGuia;
import com.grupocastores.commons.inhouse.OperadorCustom;
import com.grupocastores.commons.inhouse.RemolqueInternoCustom;
import com.grupocastores.commons.inhouse.TablaTalonesOficina;
import com.grupocastores.commons.inhouse.TgCustom;
import com.grupocastores.commons.oficinas.Guiaviaje;
import com.grupocastores.commons.oficinas.Talones;
import com.grupocastores.commons.oficinas.Viajes;
import com.grupocastores.viajes.service.IDocumentacionService;


@RestController
@RequestMapping(value ="/documentacion")
public class DocumentacionController {
    
    @Autowired
    IDocumentacionService documentacionService;
    
    @RequestMapping("/findTalones/{mesAnio}/{idEsquema}/{tipoViaje}/{tipoUnidad}/{idCliente}/{idOficinaCliente}/{idOficinaDocumenta}")
    public ResponseEntity<List<TalonCustomResponse>> findTalones(
            @PathVariable("mesAnio") String mesAnio, 
            @PathVariable("idEsquema") int idEsquema,
            @PathVariable("tipoViaje") int tipoViaje,
            @PathVariable("tipoUnidad") int tipoUnidad,
            @PathVariable("idCliente") int idCliente,
            @PathVariable("idOficinaCliente") String idOficinaCliente,
            @PathVariable("idOficinaDocumenta") String idOficinaDocumenta,
            @RequestParam String determinantesOrigen,
            @RequestParam String determinantesDestino
            ) throws Exception{
        List<TalonCustomResponse> response = documentacionService.findTalones(mesAnio, idEsquema, tipoViaje, tipoUnidad, idCliente, idOficinaCliente, idOficinaDocumenta, determinantesOrigen, determinantesDestino);
       
        
        return ResponseEntity.ok(response);
        
    } 
    
    @GetMapping("/findDetacoSumatoria/{claTalon}/{idOficinaDocumenta}")
    public ResponseEntity<DetaCo> fintDetacoSumatoria(
            @PathVariable("claTalon") String claTalon,       
            @PathVariable("idOficinaDocumenta") String idOficinaDocumenta) throws Exception{
        DetaCo response = documentacionService.findDetacoSumatoria(claTalon,idOficinaDocumenta);
             
        return ResponseEntity.ok(response);
        
    } 
      
    @GetMapping("/getFolioViaje/{idFolio}/{idOficinaDocumenta}")
    public ResponseEntity<FolioDos> getFolioViaje(
            @PathVariable("idFolio") int idFolio, @PathVariable("idOficinaDocumenta") String idOficinaDocumenta) throws Exception{
        FolioDos folio = documentacionService.getFolioViaje(idFolio, idOficinaDocumenta);
        return ResponseEntity.ok(folio);
    }
    
    @GetMapping("/getFolioGuia/{idFolio}/{idOficinaDocumenta}")
    public ResponseEntity<FoliosGuias> getFolioTalon(
            @PathVariable("idFolio") int idFolio, @PathVariable("idOficinaDocumenta") String idOficinaDocumenta) throws Exception{
        FoliosGuias folio = documentacionService.getFolioGuia(idFolio, idOficinaDocumenta);
        return ResponseEntity.ok(folio);
    }
    
    @PostMapping("/insertViaje")
    public ResponseEntity<Viajes> insertViajes(
          @RequestBody Viajes dataViaje) throws Exception{
      Viajes response = documentacionService.insertViaje(dataViaje);
      if (response == null)
          return ResponseEntity.noContent().build();
      return ResponseEntity.ok(response);
    }
    @PutMapping("/updateViaje")
    public ResponseEntity<Viajes> updateViajes(
        @RequestBody Viajes dataViaje) throws Exception{
        Viajes response = documentacionService.updateViajes(dataViaje);
        if (response == null)
            return ResponseEntity.noContent().build();
        return ResponseEntity.ok(response);
      }
    
    @GetMapping("/getViaje/{idViaje}/{idOficina}")
    public ResponseEntity<Viajes> getViaje(
            @PathVariable("idViaje") long idViaje, @PathVariable("idOficina") String idOficina) throws Exception{
        Viajes response = documentacionService.getViaje(idViaje, idOficina);
        if (response == null)
            return ResponseEntity.noContent().build();
        return ResponseEntity.ok(response);
    }
    
    @PostMapping("/insertViajeEsquema")
    public ResponseEntity<Boolean> insertViajeEsquema(
          @RequestBody ViajeEsquemaGasto dataViajeEsquema,
          @RequestParam String idOficinaDocumenta) throws Exception{
        Boolean response = documentacionService.insertViajeEsquema(dataViajeEsquema, idOficinaDocumenta);
      if (!response)
          return ResponseEntity.noContent().build();
      return ResponseEntity.ok(response);
    }
    
    @GetMapping("/filterViajes/{table}/{idEsquema}/{idesquemagasto}/{tipounidad}/{tiporuta}/{idruta}/{idoficinadocumenta}")
    public ResponseEntity<List<Viajes>> filterViajes(
            @PathVariable("table") String table,
            @PathVariable("idEsquema") int idEsquema,
            @PathVariable("idesquemagasto") int idesquemagasto,
            @PathVariable("tipounidad") int tipounidad,
            @PathVariable("tiporuta") int tiporuta,
            @PathVariable("idruta") int idruta,
            @PathVariable("idoficinadocumenta") String idOficinaDocumenta
            ) throws Exception{
        List<Viajes> response = documentacionService.filterViajes(table, idEsquema, idesquemagasto, tipounidad, tiporuta, idruta, idOficinaDocumenta);
        if (response == null)
            return ResponseEntity.noContent().build();
        return ResponseEntity.ok(response);
      }
    
    @PostMapping("/insertGuiaViaje")
    public ResponseEntity<Boolean> insertGuia(
          @RequestBody Guiaviaje dataGuiaViaje) throws Exception{
        Boolean response = documentacionService.insertGuiaViaje(dataGuiaViaje);
      if (response == null)
          return ResponseEntity.noContent().build();
      return ResponseEntity.ok(response);
    }
    
    @PostMapping("/insertGuia")
    public ResponseEntity<Guias> insertGuia(
          @RequestBody Guias dataGuia) throws Exception{
        Guias response = documentacionService.insertGuia(dataGuia);
      if (response == null)
          return ResponseEntity.noContent().build();
      return ResponseEntity.ok(response);
    }
    
    @PutMapping("/updateGuia")
    public ResponseEntity<Guias> updateGuia(
            @RequestBody Guias dataGuia) throws Exception{
        Guias response = documentacionService.updateGuia(dataGuia);
        if (response == null)
            return ResponseEntity.noContent().build();
        return ResponseEntity.ok(response);
      }
    
    @GetMapping("/getGuia/{noGuia}/{tabla}")
    public ResponseEntity<Guias> getGuia(
            @PathVariable("noGuia") String noGuia, @PathVariable("tabla") String tabla) throws Exception{
        Guias response = documentacionService.getGuia(noGuia, tabla);
        if (response == null)
            return ResponseEntity.noContent().build();
        return ResponseEntity.ok(response);
      }
    
    @PostMapping("/insertTalonGuia")
    public ResponseEntity<Boolean> insertTalonGuia(
          @RequestBody List<TgCustom> dataGuia) throws Exception{
        Boolean response = documentacionService.insertTalonGuia(dataGuia);
        if (!response)
          return ResponseEntity.noContent().build();
        return ResponseEntity.ok(response);
    }
    
    @PostMapping("/insertGuMesAnio")
    public ResponseEntity<Boolean> insertGuMesAnio(
          @RequestBody GuMesAnioCustom dataGuiaMesAnio) throws Exception{
        Boolean response = documentacionService.insertGuMesAnio(dataGuiaMesAnio);
        if (!response)
          return ResponseEntity.noContent().build();
        return ResponseEntity.ok(response);
    }
    
    @PostMapping("/insertImporteGuia/{idoficinaDocumenta}")
    public ResponseEntity<Boolean> insertImporteGuia(
        @PathVariable("idoficinaDocumenta") String idoficinaDocumenta,
          @RequestBody ImporteGuia dataImporte) throws Exception{
        Boolean response = documentacionService.insertImporteGuia(dataImporte, idoficinaDocumenta);
        if (!response)
            return ResponseEntity.noContent().build();
          return ResponseEntity.ok(response);
    }
    
    @PutMapping("/updateGuMesAnio")
    public ResponseEntity<Boolean> updateGuMesAnio(
            @RequestBody GuMesAnioCustom dataGuiaMesAnio) throws Exception{
        Boolean response = documentacionService.updateGuMesAnio(dataGuiaMesAnio);
        if (response == null)
            return ResponseEntity.noContent().build();
        return ResponseEntity.ok(response);
    }
    
    @GetMapping("/getGuMesAnio/{noGuia}/{tabla}/{idOficinaDocumenta}")
    public ResponseEntity<GuMesAnio> getGuMesAnio(
            @PathVariable("noGuia") String noGuia, @PathVariable("tabla") String tabla, @PathVariable String idOficinaDocumenta) throws Exception{
        GuMesAnio response = documentacionService.getGuMesAnio(noGuia, tabla, idOficinaDocumenta);
        if (response == null)
            return ResponseEntity.noContent().build();
        return ResponseEntity.ok(response);
    }
    
    @GetMapping("/findOperadores/{unidad}/{tipoUnidad}")
    public ResponseEntity<List<OperadorCustom>> getOperador(
            @PathVariable("unidad") String unidad, @PathVariable("tipoUnidad") int tipoUnidad ) throws Exception{
        List<OperadorCustom> response = documentacionService.getOperador(unidad, tipoUnidad);
        if (response == null)
            return ResponseEntity.noContent().build();
        return ResponseEntity.ok(response);
      }
    
    @GetMapping("/getRemolqueInterno/{idRemolque}/{idOficinaDocumenta}")
    public ResponseEntity<List<RemolqueInternoCustom>> getRemolqueInterno(
            @PathVariable("idRemolque") int idRemolque, @PathVariable("idOficinaDocumenta") String idOficinaDocumenta) throws Exception{
        List<RemolqueInternoCustom> remolque = documentacionService.getRemolqueInterno(idRemolque, idOficinaDocumenta);
        return ResponseEntity.ok(remolque);
    }
    
    @GetMapping("/getRemolqueExterno/{idRemolque}/{idOficinaDocumenta}")
    public ResponseEntity<List<RemolqueInternoCustom>> getRqmolqueExterno(
            @PathVariable("idRemolque") int idRemolque, @PathVariable("idOficinaDocumenta") String idOficinaDocumenta) throws Exception{
        List<RemolqueInternoCustom> folio = documentacionService.getRqmolqueExterno(idRemolque, idOficinaDocumenta);
        return ResponseEntity.ok(folio);
    }
    
    @GetMapping("/getEspecificacionTalon/{claTalon}/{idOficinaDocumenta}")
    public ResponseEntity<EspecificacionTalon> getEspecificacionTalon(
            @PathVariable("claTalon") String claTalon, @PathVariable("idOficinaDocumenta") String idOficinaDocumenta) throws Exception{
        EspecificacionTalon especificacion = documentacionService.getEspecificacionTalon(claTalon, idOficinaDocumenta);
        return ResponseEntity.ok(especificacion);
    }
    
    @GetMapping("/getTablaTalon/{claTalon}/{idOficinaDocumenta}")
    public ResponseEntity<TablaTalonesOficina> getTablaTalon(
            @PathVariable("claTalon") String claTalon, @PathVariable("idOficinaDocumenta") String idOficinaDocumenta) throws Exception{
        TablaTalonesOficina especificacion = documentacionService.getTablaTalon(claTalon, idOficinaDocumenta);
        return ResponseEntity.ok(especificacion);
    }
    
    @PostMapping("/insertCcpRemolque/{idoficinaDocumenta}")
    public ResponseEntity<Boolean> insertCcpRemolque(
        @PathVariable("idoficinaDocumenta") String idoficinaDocumenta,
          @RequestBody CcpRemolque dataRemolque) throws Exception{
        Boolean response = documentacionService.insertCcpRemolque(dataRemolque, idoficinaDocumenta);
        if (!response)
            return ResponseEntity.noContent().build();
          return ResponseEntity.ok(response);
    }
    
    @PostMapping("/insertCcpRemolqueExterno/{idoficinaDocumenta}")
    public ResponseEntity<Boolean> insertCcpRemolqueExterno(
        @PathVariable("idoficinaDocumenta") String idoficinaDocumenta,
          @RequestBody CcpRemolqueExterno dataRemolque) throws Exception{
        Boolean response = documentacionService.insertCcpRemolqueExterno(dataRemolque, idoficinaDocumenta);
        if (!response)
            return ResponseEntity.noContent().build();
          return ResponseEntity.ok(response);
    }
      
}
