### nome: "primeira_entrega"
#### atualizações:

- [x] arquivo .pdf com um descritivo do projeto (1)

- [x] img do modelo ER (1)

- [x] Spring boot (1)

- [x] Flyway (1)

- [x] Spring Data JPA com Mapeamentos NxN completos (2) 
> - [x] N x 1
> - [x] N x N

- [x] Rest API (0,5) -- controllers, endpoints

- [x] Spring doc swagger (1)

- [x] Boas práticas REST (1,5)
> - [x] Spring Validation
> - [x] Tratamento de retornos
> - [x] Tratamento de erros

- [x] Coleção de endpoits para teste feitas no insomia ou postman (1)

- [x] Spring Security
> - [x] Autenticação: criar usuário (/login), validar token no https://www.jwt.io/
> - [x] Autorização: se pode acessar cada endpoint

----------------

# Biblioteca 3.0
> vídeo: https://youtu.be/Q6V2Jbwqj-4  

Sistema de gerenciamento para uma biblioteca (backend) com gestão de usuários, autores, livros e empréstimos: autenticação, cadastro, controle de acervo
- Spring Boot - framework
- Spring Data JPA - persistência de dados, mapeamento objeto-relacional com Hibernate
- PostgreSQL - banco de dados
- Flyway - gerenciamento de schema (alterações no banco de dados)
- Spring Web (API) - endpoints RESTful, manipulação de requisições HTTP e JSON
- Spring Security (segurança) - autenticação via JWT (JSON Web Tokens) e autorização baseada em permissões (roles)
- Springdoc OpenAPI - documentação Swagger UI
- Spring Boot Starter Validation - validação nos dados de entrada da API
- Lombok - getter, setter, construtor
- Maven - gerenciamento de dependências e build
- SDK temurin-21


### Banco de dados
- diagrama Mermaid
![diagrama - Mermaid.png](diagrama%20-%20Mermaid.png)
**Tabelas**
- usuario (tipo) - admin, usuario
- usuario (permissao) - role_admin, role_usuario
- emprestimo (status) - ativo, concluido, atrasado
- livro_autor: tabela de junção (NxN)

**Relacionamentos**
- [N:N] livro <--> autor (@ManyToMany)
- [1:N] usuario -> emprestimo (@ManyToOne, FetchType.LAZY, @JsonIgnoreProperties)
- [1:N] livro -> emprestimo (@ManyToOne, FetchType.LAZY, @JsonIgnoreProperties)


### Funcionalidades
* Autenticação (```/login```) - endpoint público
  * `POST /login`: Recebe `DadosAutenticacao` (login/senha), valida as credenciais no `AuthenticationManager` (Spring Security) e retorna um token JWT se der tudo certo

* Usuários (`/usuarios`) - requer autenticação
    * `POST /usuarios/registrar`: Endpoint público para criação de novos usuários (default: `USUARIO`), a senha é criptografada com `BCryptPasswordEncoder` e depois é salva no banco de dados
    * `GET /usuarios`: Lista todos os usuários ativos, retorna `DadosUsuario` (DTO) para não expor a senha
    * `GET /usuarios/{id}`: Busca um usuário por ID e retorna `DadosUsuario`
    * `PUT /usuarios/{id}`: Atualiza dados de um usuário 
    * `DELETE /usuarios/{id}`: Exclui um usuário (requer permissão de ADMIN)

* Autores (`/autores`) - requer autenticação
    * Endpoints de CRUD (`GET`, `GET /{id}`, `POST /registrar`, `PUT /{id}`, `DELETE /{id}`)
    * Operações de escrita (`POST`, `PUT`, `DELETE`) requerem permissão de ADMIN

* Livros (`/livros`) - requer autenticação
    * `GET /livros`: Lista todos os livros *ativos*
    * `GET /livros/all`: Lista *todos* os livros, incluindo os inativos (soft delete)
    * `GET /livros/{id}`: Busca um livro por ID
    * `POST /livros/registrar`: Cria um novo livro, recebe um `LivroDTO` com os dados do livro e o(s) ID(s) do(s) autor(es), requer permissão de ADMIN
    * `PUT /livros/{id}`: Atualiza um livro existente, recebe um `LivroDTO` (requer permissão de ADMIN)
    * `DELETE /livros/{id}`: Faz um *soft delete* do livro (define `ativoLiv = false`), requer permissão de ADMIN

* Empréstimos (`/emprestimos`) - requer autenticação
    * `GET /emprestimos`: Lista os empréstimos, recebe `idUsuario` como parâmetro (`?usuarioId = x`)
      * se o usuário for ADMIN, retorna todos os empréstimos
      * se for USUARIO comum, retorna apenas os seus
    * `GET /emprestimos/{id}`: Busca um empréstimo por ID
    * `POST /emprestimos/registrar`: Cria um novo empréstimo, recebe um `EmprestimoDTO` com `idLivro` e `idUsuario` 
    * `PUT /emprestimos/{id}/devolver`: Registra a devolução de um empréstimo 
    * `PUT /emprestimos/{id}/renovar`: Renova a data de devolução prevista de um empréstimo 


### Regras de negócio
* Criação de usuário: novos usuários registrados via API sempre recebem o tipo `USUARIO` e a permissão `ROLE_USUARIO`, com status `ativoUs = true`
* Senhas são criptografadas e depois salvas no banco de dados
* Empréstimos:
    * Só é possível emprestar um livro se ele estiver `ativoLiv = true` e `disponivelLiv = true` 
    * Ao criar um empréstimo, o livro é marcado como `disponivelLiv = false` e com status `ATIVO`
    * A data de devolução prevista é calculada como 14 dias após a data do empréstimo
    * Ao devolver, o livro é marcado como `disponivelLiv = true`, o status do empréstimo muda para `CONCLUIDO` e a data de devolução efetiva (atual) é registrada
* Deletes:
    * Livros: Utiliza *soft delete* (`ativoLiv = false`)
    * Um livro não pode ser inativado se estiver emprestado (`disponivelLiv = false`)
    * Autores: Não podem ser excluídos se possuírem livros associados (`FOREIGN KEY` + `ON DELETE RESTRICT` tratado no `AutorService` com `DataIntegrityViolationException`)
    * Usuários: Não podem ser excluídos se possuírem empréstimos associados (`FOREIGN KEY` e tratado no `UsuarioService` com `DataIntegrityViolationException`)
* Usuários
  * Todo usuário criado pelo endpoint é do tipo USUARIO


### Segurança (Spring Security)
**Autenticação**
* O endpoint `POST /login` utiliza o `AuthenticationManager` para validar as credenciais (`DadosAutenticacao`)
* Em caso de sucesso, o `TokenServiceJWT` gera um token JWT contendo o email do usuário (subject) e sua permissão (ROLE)

**Autorização -** `SecurityConfig`
* `/login`, `/usuarios/registrar` e endpoints do Swagger são públicos (`permitAll`)
* Operações de exclusão (`DELETE`) e escrita (`POST`, `PUT`) em `autores` e `livros` exigem a permissão `ROLE_ADMIN` (`hasRole("ADMIN")`)
* As demais requisições exigem apenas que o usuário esteja autenticado (`authenticated()`)

**Filtro de Token**
* A classe `FiltroToken` intercepta cada requisição
* extrai o token JWT do cabeçalho `Authorization`
* valida o token usando o `TokenServiceJWT`
* carrega os dados do usuário (`UserDetails`) pelo `AutenticacaoService`
* estabelece o contexto de segurança para a requisição


### Boas Práticas REST 
* Tratamento de Retornos: Todos os endpoints dos controllers retornam `ResponseEntity` (códigos de status HTTP)
* Validação de Dados: As entidades e DTOs utilizam anotações do Spring Validation (`@NotBlank`, `@NotNull`, `@Email`, `@Past`, `@NotEmpty`, `@Valid`)
* Tratamento de Erros: Exceções específicas (`RecursoNaoEncontradoException`, `RegraDeNegocioException`) são lançadas pelos `Services` para erros específicos -- a classe `RestExceptionHandler` (`@RestControllerAdvice`) intercepta essas exceções e retorna respostas JSON com os status HTTP (`404 Not Found`, `400 Bad Request`)
* DTOs: Classes DTO (`LivroDTO`, `EmprestimoDTO`, `DadosUsuario`, `DadosAutenticacao`) são utilizadas para "separar" a API das entidades internas e controlar o fluxo dos dados



### Autenticação 
- POST http://localhost:8080/biblioteca3.0/login body (json) - login de admin (autorização total):
```bash 
{
"login": "admin@email.com",
"senha": "admin123"
} 
```
- request, ex: http://localhost:8080/biblioteca3.0/livros/all
- Auth - bearer token: colar; prefix: "Bearer "

#### Autorização
- Todos
```
POST /login
POST /usuarios/registrar
Swagger (navegador)
```
- Todos usuários logados
```
GET /autores
GET /autores/{id}
GET /livros
GET /livros/all
GET /livros/{id}
GET /usuarios
GET /usuarios/{id}
PUT /usuarios/{id} 
GET /emprestimos?usuarioId={id}
GET /emprestimos/{id}
POST /emprestimos/registrar
PUT /emprestimos/{id}/devolver
PUT /emprestimos/{id}/renovar
```
- Admin 
``` 
DELETE /autores/{id}
DELETE /livros/{id}
DELETE /usuarios/{id}
DELETE /emprestimos/{id} (Se você criar esse endpoint)
POST /autores/registrar
PUT /autores/{id}
POST /livros/registrar
PUT /livros/{id}
```
