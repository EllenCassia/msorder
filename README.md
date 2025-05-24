# 🛒 MSOrder - Gerenciamento de Pedidos

## 📚 Sobre o Desafio

**Desafio Klok**  
O desafio consiste em refatorar e otimizar o serviço de Pedido para melhorar sua legibilidade, manutenção e eficiência, garantindo que a lógica de negócios seja clara e bem organizada.

### Requisitos para entrega

- Disponibilização de um projeto utilizando Spring ou NestJS com o serviço de pedidos refatorado;  
- Implementação de testes de unidade para o serviço de pedidos;  
- Repositório no GitHub ou na GitLab;  
- Código-fonte original em Java;  
- Lembrando que a entrega pode ser feita utilizando Spring/Java ou NestJS/TypeScript.

### Código-fonte original em Java

```java
public class PedidoService {

    public void processarPedidos(List<Pedido> pedidos) {
        for (Pedido pedido : pedidos) {
            double total = 0;

            for (Item item : pedido.getItems()) {
                total += item.getPreco() * item.getQuantidade();
            }

            pedido.setTotal(total);

            if (pedido.getCliente().isVip()) {
                total *= 0.9;
            }

            pedido.setTotalComDesconto(total);

            boolean emEstoque = true;
            for (Item item : pedido.getItems()) {
                if (item.getQuantidade() > item.getEstoque()) {
                    emEstoque = false;
                    break;
                }
            }
            pedido.setEmEstoque(emEstoque);

            if (emEstoque) {
                pedido.setDataEntrega(LocalDate.now().plusDays(3));
            } else {
                pedido.setDataEntrega(null);
            }

            if (emEstoque) {
                enviarNotificacao(pedido.getCliente().getEmail(), "Seu pedido será entregue em breve.");
            } else {
                enviarNotificacao(pedido.getCliente().getEmail(), "Um ou mais itens do seu pedido estão fora de estoque.");
            }
        }
    }

    private void enviarNotificacao(String email, String mensagem) {
        System.out.println("Enviando e-mail para " + email + ": " + mensagem);
    }
}
```

## 🚀 Visão Geral

Este serviço é responsável por gerenciar pedidos, clientes e produtos dentro do sistema, incluindo criação, consulta, atualização, exclusão e controle de status.

## 📹 Vídeo Demonstrativo

https://www.youtube.com/watch?v=7PnarHsnWgs

## 🛠️ Requisitos

- ☕ **Java 17**  
- 🐘 **Maven**  
- 🌱 **Spring Boot**  
- 📦 **Spring Data**  
- 🌐 **Spring MVC**  
- ✅ **Hibernate Validation**  
- 🧰 **Lombok**  
- 🐬 **MySQL**  
- 🔧 **Git**  
- 🧪 **Spring Test (JUnit)**  

## 📥 Instalação e Configuração

1. Clone o repositório:  
```bash
git clone <URL_DO_REPOSITORIO>
```

2. Configure o banco de dados MySQL editando o arquivo `src/main/resources/application.yml` com o modelo abaixo:  
```yaml
spring:
  datasource:
    url: jdbc:mysql://localhost:3306/seu_banco_de_dados?useUnicode=true&characterEncoding=utf8&useSSL=false&serverTimezone=UTC
    driver-class-name: com.mysql.cj.jdbc.Driver
    username: seu_usuario
    password: sua_senha
  jpa:
    hibernate:
      ddl-auto: update
    properties:
      hibernate:
        show_sql: true
        format_sql: true
```

## ▶️ Inicialização do Serviço

No diretório do projeto, execute:  
```bash
./mvnw spring-boot:run
```
ou  
```bash
mvn spring-boot:run
```

O serviço será iniciado na porta padrão `8080`.

## 🔌 Endpoints Disponíveis

### 🛒 Order Service (`/api/orders`)

| Método | Endpoint                         | Descrição                                  |
|--------|---------------------------------|--------------------------------------------|
| POST   | `/api/orders`                   | Criar um novo pedido                        |
| GET    | `/api/orders/{id}`              | Buscar pedido por ID                        |
| GET    | `/api/orders`                   | Listar todos os pedidos                     |
| GET    | `/api/orders/client/{clientId}`| Buscar pedidos por cliente (com paginação)|
| GET    | `/api/orders/status/{status}`  | Buscar pedidos por status                   |
| GET    | `/api/orders/client/{clientId}/status/{status}` | Buscar pedidos por cliente e status |
| GET    | `/api/orders/out-of-stock`      | Buscar pedidos fora de estoque              |
| GET    | `/api/orders/client/{clientId}/count` | Contar pedidos por cliente            |
| PUT    | `/api/orders/{id}`              | Atualizar pedido                           |
| DELETE | `/api/orders/{id}`              | Deletar pedido                            |
| PATCH  | `/api/orders/{id}/status`       | Atualizar status do pedido                  |

#### Exemplo de requisição POST para criar um pedido

```json
{
  "clientId": "95620bd2-bef9-45fe-b6b5-4fc6cb332251",
  "status": "PENDING",
  "items": [
    {
      "productId": "ae99cc77-68bc-4e15-bdfb-88c8ac5f9ee8",
      "quantity": 1,
      "pricePerUnit": 10.0
    }
  ],
  "deliveryDate": null,
  "totalAmount": 0,
  "totalAmountWithDiscount": 0
}
```


### 👥 Client Service (`/api/clients`)

| Método | Endpoint                  | Descrição                          |
|--------|---------------------------|----------------------------------|
| GET    | `/api/clients/{id}`       | Buscar cliente por ID             |
| GET    | `/api/clients/email/{email}` | Buscar cliente por email       |
| POST   | `/api/clients`            | Criar um novo cliente             |
| PUT    | `/api/clients/{id}`       | Atualizar cliente                 |
| GET    | `/api/clients/vip`        | Listar todos os clientes VIP      |
| GET    | `/api/clients/vip/count`  | Contar clientes VIP               |

#### Exemplo de requisição POST para criar um cliente

```json
{
  "name": "Cliente Exemplo",
  "email": "cliente@example.com",
  "vip": false
}
```


### 📦 Product Service (`/api/products`)

| Método | Endpoint                    | Descrição                          |
|--------|-----------------------------|----------------------------------|
| GET    | `/api/products/{id}`        | Buscar produto por ID             |
| GET    | `/api/products`             | Listar todos os produtos ativos   |
| GET    | `/api/products/available`  | Listar produtos disponíveis       |
| POST   | `/api/products`             | Criar um novo produto             |
| PUT    | `/api/products/{id}/stock` | Atualizar estoque do produto      |
| GET    | `/api/products/low-stock`  | Listar produtos com estoque baixo |
| GET    | `/api/products/{id}/name`  | Buscar nome do produto por ID     |

#### Exemplo de requisição POST para criar um produto

```json
{
  "name": "Produto Exemplo",
  "price": 99.99,
  "stockQuantity": 20
}
```


## 🧪 Testes

Este projeto utiliza o Spring Test com JUnit para testes automatizados. Os testes de integração para os controladores estão localizados nas seguintes classes, que cobrem os endpoints listados:

### ClientControllerSimpleIntegrationTest

Testa os endpoints:  
- GET `/api/clients/{id}`  
- GET `/api/clients/email/{email}`  
- PUT `/api/clients/{id}`  
- GET `/api/clients/vip`  
- GET `/api/clients/vip/count`  

### OrderControllerIntegrationTest

Testa os endpoints:  
- GET `/api/orders`  
- GET `/api/orders/{id}`  
- GET `/api/orders/status/{status}`  
- GET `/api/orders/client/{clientId}`  
- GET `/api/orders/client/{clientId}/status/{status}`  
- GET `/api/orders/out-of-stock`  
- GET `/api/orders/client/{clientId}/count`  
- POST `/api/orders`  
- DELETE `/api/orders/{id}`  
- PATCH `/api/orders/{id}/status`  

### ProductControllerIntegrationTest

Testa os endpoints:  
- GET `/api/products`  
- GET `/api/products/{id}`  
- GET `/api/products/available`  
- POST `/api/products`  
- PUT `/api/products/{id}/stock`  
- GET `/api/products/low-stock`  
- GET `/api/products/{id}/name`  

Para executar os testes, utilize o comando:  
```bash
./mvnw test
```
ou  
```bash
mvn test
```

Os testes cobrem as camadas de serviço e controle, garantindo a integridade das funcionalidades do microsserviço.

## 🔗 Frontend

O frontend deste projeto está disponível no repositório GitHub:  
[https://github.com/seu-usuario/seu-frontend-repositorio](https://github.com/EllenCassia/order-management-frontend.git)

## 🛠️ Solução de Problemas

- Verifique se o banco de dados MySQL está em execução e acessível.  
- Confirme as credenciais e URL no arquivo `application.yml`.  
- Certifique-se de que a porta 8080 não está ocupada por outro serviço.  
- Verifique os logs do serviço para mensagens de erro.

🚀 **Agora você está pronto para rodar o microsserviço MSOrder!**
