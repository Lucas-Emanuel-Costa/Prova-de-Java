# Checklist de Entrega

## Equipe

Projeto desenvolvido e apresentado por um unico integrante.

| Integrante | Endpoint para apresentacao |
| --- | --- |
| Lucas Emanuel De Oliveira Costa | `GET /filmes` e `POST /usuarios` |

O integrante pode demonstrar os demais endpoints durante a apresentacao.

## Requisitos implementados

- Equipe com um integrante e dois endpoints para apresentacao: `GET /filmes` e `POST /usuarios`.
- Lombok nas entidades JPA `Filme`, `Funcionario` e `Usuario`.
- ORM com Spring Data JPA.
- Consultas JPQL personalizadas em `FilmeRepository`.
- Integracao externa com ViaCEP.
- Bean Validation nas requisicoes.
- Validator customizado de CPF/CNPJ com verificacao dos digitos validadores.
- Validator customizado de senha forte e armazenamento com hash BCrypt.
- Validacao das faixas de ano, duracao e relevancia dos filmes.
- E-mail obrigatorio, com formato valido e unicidade garantida no banco e no servico de usuarios.
- Logs com `X-Correlation-Id`.
- Paginacao em `GET /filmes` e `GET /funcionarios`.
- Tratamento global centralizado com resposta `application/problem+json`.
- Swagger UI em `http://localhost:8080/swagger-ui.html`.

## Testes automatizados

- Validacao algoritmica de CPF/CNPJ.
- Validacao de senha forte.
- Validacao integrada de e-mail, senha e CPF/CNPJ no cadastro de usuario.
- Hash BCrypt e bloqueio de e-mail duplicado.
- Faixas validas de ano, duracao e relevancia.
- Controllers, services, repositories e mappers de filmes.

## Publicacao

Apenas o integrante deve criar o repositorio Git e realizar a entrega. Antes do envio:

1. Execute `mvn test`.
2. Confira os endpoints no Swagger.
