### nome: "primeira_entrega"
#### atualizações:

- [ ] arquivo .pdf com um descritivo do projeto (1)

- [x] img do modelo ER: (1)

- [x] Spring boot (1)

- [x] Flyway (1)

- [x] Spring Data JPA com Mapeamentos NxN completos (2) 
> - [x] N x 1
> - [x] N x N

- [x] Rest API (0,5) -- controllers, endpoints

- [x] Spring doc swagger (1)

- [ ] Boas práticas REST (1,5)

> - [x] Spring Validation
> - [x] Tratamento de retornos
> - [ ] Tratamento de erros

- [ ] Coleção de endpoits para teste feitas no insomia ou postman (1)
- [x] Spring Security
> - [x] Autenticação: criar usuário (/login), validar token no https://www.jwt.io/
> - [x] Autorização: se pode acessar cada endpoint


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