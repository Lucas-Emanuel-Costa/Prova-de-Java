package br.uniesp.si.techback.service;

import br.uniesp.si.techback.client.ViaCepClient;
import br.uniesp.si.techback.dto.ViaCepResponseDTO;
import br.uniesp.si.techback.exception.CustomBeanException;
import feign.FeignException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class ViaCepService {

    private final ViaCepClient viaCepClient;

    public ViaCepResponseDTO buscarPorCep(String cep) {
        log.info("Consultando CEP no ViaCEP: {}", cep);

        String cepLimpo = cep.replaceAll("\\D", "");

        if (cepLimpo.length() != 8) {
            log.warn("CEP inválido informado: {}", cep);
            throw new CustomBeanException("CEP inválido. Informe um CEP com 8 dígitos.");
        }

        ViaCepResponseDTO resposta;
        try {
            resposta = viaCepClient.buscarPorCep(cepLimpo);
        } catch (FeignException ex) {
            log.error("Falha ao consultar o servico ViaCEP para o CEP: {}", cepLimpo, ex);
            throw new CustomBeanException("Nao foi possivel consultar o ViaCEP no momento.");
        }

        if (resposta == null || Boolean.TRUE.equals(resposta.getErro())) {
            log.warn("CEP não encontrado: {}", cepLimpo);
            throw new CustomBeanException("CEP não encontrado: " + cepLimpo);
        }

        log.info("CEP consultado com sucesso: {}", cepLimpo);
        return resposta;
    }
}
