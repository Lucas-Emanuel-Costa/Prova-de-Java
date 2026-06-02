package br.uniesp.si.techback.service;

import br.uniesp.si.techback.dto.UsuarioDTO;
import br.uniesp.si.techback.exception.CustomBeanException;
import br.uniesp.si.techback.mapper.UsuarioMapper;
import br.uniesp.si.techback.model.Usuario;
import br.uniesp.si.techback.repository.UsuarioRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class UsuarioServiceTest {

    @Mock
    private UsuarioRepository usuarioRepository;

    @Mock
    private UsuarioMapper usuarioMapper;

    @InjectMocks
    private UsuarioService usuarioService;

    @Test
    void deveArmazenarSenhaComoHashBCrypt() {
        UsuarioDTO entrada = UsuarioDTO.builder()
                .nomeCompleto("Lucas Emanuel De Oliveira Costa")
                .email("lucas@example.com")
                .senha("Senha123")
                .perfil("USER")
                .build();
        Usuario usuario = Usuario.builder().email("lucas@example.com").build();
        Usuario usuarioSalvo = Usuario.builder().id(1L).email("lucas@example.com").senhaHash("hash").build();
        UsuarioDTO resposta = UsuarioDTO.builder().id(1L).email("lucas@example.com").build();

        when(usuarioRepository.existsByEmailIgnoreCase(entrada.getEmail())).thenReturn(false);
        when(usuarioMapper.toEntity(entrada)).thenReturn(usuario);
        when(usuarioRepository.save(usuario)).thenAnswer(invocation -> {
            assertThat(usuario.getSenhaHash()).startsWith("$2");
            assertThat(new BCryptPasswordEncoder().matches("Senha123", usuario.getSenhaHash())).isTrue();
            return usuarioSalvo;
        });
        when(usuarioMapper.toDTO(usuarioSalvo)).thenReturn(resposta);

        assertThat(usuarioService.criar(entrada)).isEqualTo(resposta);
        verify(usuarioRepository).save(usuario);
    }

    @Test
    void deveRecusarEmailDuplicado() {
        UsuarioDTO entrada = UsuarioDTO.builder().email("lucas@example.com").senha("Senha123").build();
        when(usuarioRepository.existsByEmailIgnoreCase(entrada.getEmail())).thenReturn(true);

        assertThatThrownBy(() -> usuarioService.criar(entrada))
                .isInstanceOf(CustomBeanException.class)
                .hasMessage("E-mail ja cadastrado.");
    }
}
