package br.uniesp.si.techback.service;

import br.uniesp.si.techback.dto.FuncionarioDTO;
import br.uniesp.si.techback.dto.ViaCepResponseDTO;
import br.uniesp.si.techback.exception.CustomBeanException;
import br.uniesp.si.techback.mapper.FuncionarioMapper;
import br.uniesp.si.techback.model.Funcionario;
import br.uniesp.si.techback.repository.FuncionarioRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class FuncionarioService {

    private final FuncionarioRepository funcionarioRepository;
    private final ViaCepService viaCepService;
    private final FuncionarioMapper funcionarioMapper;

    public Page<FuncionarioDTO> listar(Pageable pageable) {
        log.info("Listando funcionários cadastrados");

        return funcionarioRepository.findAll(pageable)
                .map(funcionarioMapper::toDTO);
    }

    public FuncionarioDTO incluir(FuncionarioDTO funcionarioDTO) {
        log.info("Cadastrando funcionário: {}", funcionarioDTO.getNome());

        Funcionario funcionario = funcionarioMapper.toEntity(funcionarioDTO);

        if (funcionario.getCep() != null && !funcionario.getCep().isBlank()) {
            String cepLimpo = funcionario.getCep().replaceAll("\\D", "");

            if (cepLimpo.length() != 8) {
                log.warn("CEP inválido informado: {}", funcionario.getCep());
                throw new CustomBeanException("CEP inválido. Informe um CEP com 8 dígitos.");
            }

            ViaCepResponseDTO endereco = viaCepService.buscarPorCep(cepLimpo);

            if (endereco == null || Boolean.TRUE.equals(endereco.getErro())) {
                log.warn("CEP não encontrado no ViaCEP: {}", cepLimpo);
                throw new CustomBeanException("CEP inválido para consulta no ViaCEP");
            }

            funcionario.setCep(endereco.getCep());
            funcionario.setLogradouro(endereco.getLogradouro());
            funcionario.setBairro(endereco.getBairro());
            funcionario.setLocalidade(endereco.getLocalidade());
            funcionario.setUf(endereco.getUf());
        }

        Funcionario funcionarioSalvo = funcionarioRepository.save(funcionario);

        log.info("Funcionário cadastrado com sucesso. ID: {}", funcionarioSalvo.getId());

        return funcionarioMapper.toDTO(funcionarioSalvo);
    }
}
