package br.uniesp.si.techback.controller;

import br.uniesp.si.techback.dto.ViaCepResponseDTO;
import br.uniesp.si.techback.service.ViaCepService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/ceps")
@RequiredArgsConstructor
@Slf4j
public class ViaCepController {

    private final ViaCepService viaCepService;

    @GetMapping("/{cep}")
    public ResponseEntity<ViaCepResponseDTO> buscarPorCep(@PathVariable String cep) {
        log.info("Recebida requisição para consultar CEP: {}", cep);
        return ResponseEntity.ok(viaCepService.buscarPorCep(cep));
    }
}
