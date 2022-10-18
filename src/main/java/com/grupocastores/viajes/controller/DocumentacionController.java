package com.grupocastores.viajes.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.grupocastores.commons.inhouse.FindTalonCustomResponse;
import com.grupocastores.viajes.service.IDocumentacionService;


@RestController
@RequestMapping(value ="/documentacion")
public class DocumentacionController {
    
    @Autowired
    IDocumentacionService documentacionService;
    
    @GetMapping("/findTalones/{mesAnio}/{idEsquema}/{tipoViaje}/{tipoUnidad}/{idCliente}/{idOficinaCliente}/{idOficinaDocumenta}")
    public ResponseEntity<List<FindTalonCustomResponse>> findTalones(
            @PathVariable("mesAnio") String mesAnio, 
            @PathVariable("idEsquema") int idEsquema,
            @PathVariable("tipoViaje") int tipoViaje,
            @PathVariable("tipoUnidad") int tipoUnidad,
            @PathVariable("idCliente") int idCliente,
            @PathVariable("idOficinaCliente") String idOficinaCliente,
            @PathVariable("idOficinaDocumenta") String idOficinaDocumenta
            ) throws Exception{
        List<FindTalonCustomResponse> response = documentacionService.findTalones(mesAnio, idEsquema, tipoViaje, tipoUnidad, idCliente, idOficinaCliente, idOficinaDocumenta);
       
        
        return ResponseEntity.ok(response);
        
    }
    
    @GetMapping("/getFolio/{idFolio}/{idOficinaDocumenta}")
    public ResponseEntity<String> getFolio(
            @PathVariable("idFolio") String idFolio, @PathVariable("idOficinaDocumenta") String idOficinaDocumenta) throws Exception{
         String folio = documentacionService.getFolio(idFolio, idOficinaDocumenta);
       
        
        return ResponseEntity.ok(folio);
        
    }
    
}
