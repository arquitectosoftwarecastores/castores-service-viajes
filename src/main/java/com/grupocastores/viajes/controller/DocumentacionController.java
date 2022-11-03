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

import com.grupocastores.commons.inhouse.FindTalonCustomResponse;
import com.grupocastores.commons.inhouse.FolioDos;
import com.grupocastores.commons.inhouse.FoliosGuias;
import com.grupocastores.commons.inhouse.GuMesAnio;
import com.grupocastores.commons.inhouse.GuMesAnioCustom;
import com.grupocastores.commons.inhouse.Guias;
import com.grupocastores.commons.inhouse.OperadorCustom;
import com.grupocastores.commons.inhouse.TgCustom;
import com.grupocastores.commons.oficinas.Viajes;
import com.grupocastores.viajes.service.IDocumentacionService;


@RestController
@RequestMapping(value ="/documentacion")
public class DocumentacionController {
    
    @Autowired
    IDocumentacionService documentacionService;
    
    @RequestMapping("/findTalones/{mesAnio}/{idEsquema}/{tipoViaje}/{tipoUnidad}/{idCliente}/{idOficinaCliente}/{idOficinaDocumenta}")
    public ResponseEntity<List<FindTalonCustomResponse>> findTalones(
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
        List<FindTalonCustomResponse> response = documentacionService.findTalones(mesAnio, idEsquema, tipoViaje, tipoUnidad, idCliente, idOficinaCliente, idOficinaDocumenta, determinantesOrigen, determinantesDestino);
       
        
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
    
    
      
}
