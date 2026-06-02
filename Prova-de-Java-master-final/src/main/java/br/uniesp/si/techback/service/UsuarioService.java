package br.uniesp.si.techback.service;

import br.uniesp.si.techback.dto.UsuarioDTO;
import br.uniesp.si.techback.exception.CustomBeanException;
import br.uniesp.si.techback.mapper.UsuarioMapper;
import br.uniesp.si.techback.model.Usuario;
import br.uniesp.si.techback.repository.UsuarioRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class UsuarioService {

    private final UsuarioRepository usuarioRepository;
    private final UsuarioMapper usuarioMapper;
    private final BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    public Page<UsuarioDTO> listar(Pageable pageable) {
        log.info("Listando usuarios: pagina={}, tamanho={}", pageable.getPageNumber(), pageable.getPageSize());
        return usuarioRepository.findAll(pageable).map(usuarioMapper::toDTO);
    }

    @Transactional
    public UsuarioDTO criar(UsuarioDTO dto) {
        log.info("Cadastrando usuario: {}", dto.getEmail());

        if (usuarioRepository.existsByEmailIgnoreCase(dto.getEmail())) {
            throw new CustomBeanException("E-mail ja cadastrado.");
        }

        Usuario usuario = usuarioMapper.toEntity(dto);
        usuario.setId(null);
        usuario.setSenhaHash(passwordEncoder.encode(dto.getSenha()));

        return usuarioMapper.toDTO(usuarioRepository.save(usuario));
    }
}
