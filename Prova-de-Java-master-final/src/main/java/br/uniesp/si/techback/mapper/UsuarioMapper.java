package br.uniesp.si.techback.mapper;

import br.uniesp.si.techback.dto.UsuarioDTO;
import br.uniesp.si.techback.model.Usuario;
import org.springframework.stereotype.Component;

@Component
public class UsuarioMapper {

    public Usuario toEntity(UsuarioDTO dto) {
        if (dto == null) {
            return null;
        }

        return Usuario.builder()
                .id(dto.getId())
                .nomeCompleto(dto.getNomeCompleto())
                .email(dto.getEmail())
                .cpfCnpj(dto.getCpfCnpj())
                .perfil(dto.getPerfil())
                .build();
    }

    public UsuarioDTO toDTO(Usuario entity) {
        if (entity == null) {
            return null;
        }

        return UsuarioDTO.builder()
                .id(entity.getId())
                .nomeCompleto(entity.getNomeCompleto())
                .email(entity.getEmail())
                .cpfCnpj(entity.getCpfCnpj())
                .perfil(entity.getPerfil())
                .build();
    }
}
