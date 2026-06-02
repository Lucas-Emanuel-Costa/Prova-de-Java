package br.uniesp.si.techback.controller;

import br.uniesp.si.techback.dto.FuncionarioDTO;
import br.uniesp.si.techback.service.FuncionarioService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/funcionarios")
@RequiredArgsConstructor
@Slf4j
public class FuncionarioController {

    private final FuncionarioService funcionarioService;

    @GetMapping
    public Page<FuncionarioDTO> listar(Pageable pageable) {
        log.info("Recebida requisição para listar funcionários");
        return funcionarioService.listar(pageable);
    }

    @PostMapping
    public FuncionarioDTO incluir(@Valid @RequestBody FuncionarioDTO funcionarioDTO) {
        log.info("Recebida requisição para cadastrar funcionário: {}", funcionarioDTO.getNome());
        return funcionarioService.incluir(funcionarioDTO);
    }
}
