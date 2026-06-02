package br.uniesp.si.techback.mapper;

import br.uniesp.si.techback.dto.FuncionarioDTO;
import br.uniesp.si.techback.model.Funcionario;
import org.springframework.stereotype.Component;

@Component
public class FuncionarioMapper {

    public Funcionario toEntity(FuncionarioDTO dto) {
        if (dto == null) {
            return null;
        }

        Funcionario funcionario = new Funcionario();
        funcionario.setId(dto.getId());
        funcionario.setNome(dto.getNome());
        funcionario.setEmail(dto.getEmail());
        funcionario.setCpfCnpj(dto.getCpfCnpj());
        funcionario.setCargo(dto.getCargo());
        funcionario.setCep(dto.getCep());
        funcionario.setLogradouro(dto.getLogradouro());
        funcionario.setBairro(dto.getBairro());
        funcionario.setLocalidade(dto.getLocalidade());
        funcionario.setUf(dto.getUf());

        return funcionario;
    }

    public FuncionarioDTO toDTO(Funcionario entity) {
        if (entity == null) {
            return null;
        }

        FuncionarioDTO dto = new FuncionarioDTO();
        dto.setId(entity.getId());
        dto.setNome(entity.getNome());
        dto.setEmail(entity.getEmail());
        dto.setCpfCnpj(entity.getCpfCnpj());
        dto.setCargo(entity.getCargo());
        dto.setCep(entity.getCep());
        dto.setLogradouro(entity.getLogradouro());
        dto.setBairro(entity.getBairro());
        dto.setLocalidade(entity.getLocalidade());
        dto.setUf(entity.getUf());

        return dto;
    }
}
