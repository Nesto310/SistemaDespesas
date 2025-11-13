Claro. Aqui está um `README.md` profissional e bem formatado para o repositório desse projeto.

-----

# Sistema de Controle de Despesas

 

Um sistema de console (CLI) robusto para controle de despesas pessoais, focado em demonstrar conceitos avançados de Programação Orientada a Objetos em Java.

## Descrição

Este projeto é um MVP (Minimum Viable Product) de um sistema de gerenciamento financeiro que permite aos usuários cadastrar e conciliar despesas. Ele gerencia usuários, categorias de despesa e armazena todos os dados de forma persistente em arquivos de texto (`.txt`), utilizando hash SHA-256 para a segurança das senhas.

-----

## 🚀 Funcionalidades (MVP)

O sistema apresenta um menu principal com as seguintes operações:

  * **Gestão de Despesas:**
      * `Entrar Despesa`: Registra novas despesas (descrição, valor, vencimento, categoria).
      * `Anotar Pagamento`: Concilia uma despesa, marcando-a como paga com data e valor.
  * **Relatórios e Listagem:**
      * `Listar Despesas em Aberto`: Filtra despesas pendentes por período (mês/ano).
      * `Listar Despesas Pagas`: Filtra despesas conciliadas por período (mês/ano).
  * **Submenu de Ação:**
      * Após listar, o usuário pode `Editar` ou `Excluir` uma despesa específica.
  * **Administração:**
      * `Gerenciar Tipos de Despesa`: CRUD completo (Criar, Listar, Editar, Excluir) para as categorias.
      * `Gerenciar Usuários`: CRUD completo (Cadastrar, Listar, Editar senha) para os usuários do sistema.

-----

## 🛠️ Conceitos e Tecnologias

Este projeto foi construído puramente em **Java (JDK 11+)** sem dependências externas, focando em demonstrar os seguintes conceitos:

### 1\. Programação Orientada a Objetos (OOP)

  * **Herança:** `Despesa` é uma classe `abstract` da qual classes concretas como `DespesaAlimentacao` e `DespesaTransporte` herdam.
  * **Polimorfismo:** A `DespesaService` gerencia uma `List<Despesa>` que pode conter objetos de qualquer subtipo (Alimentação, Transporte, etc.). O polimorfismo também é usado na leitura e salvamento de arquivos (identificando a classe).
  * **Interfaces:** A interface `Pagavel` define um contrato que a classe `Despesa` deve implementar (`pagar()`, `estaPaga()`).
  * **Encapsulamento:** O uso de serviços (`UsuarioService`, `DespesaService`) para encapsular a lógica de negócios e o acesso aos dados.
  * **Sobrecarga e Sobrescrita:**
      * **Sobrecarga (Overloading):** Construtores sobrecarregados na classe `Despesa` (um para criar novas despesas, outro para carregar do arquivo).
      * **Sobrescrita (Overriding):** O método `toString()` é sobrescrito nas subclasses para personalizar a exibição.
  * **Métodos e Atributos Estáticos:** Utilizados para contadores globais (`contadorGlobalId`) e em classes utilitárias (`CriptografiaUtil`).

### 2\. Persistência de Dados

  * O sistema não utiliza um banco de dados SQL. Em vez disso, todos os dados são serializados e persistidos em arquivos de texto locais, simulando uma base de dados.
  * `despesas.txt`: Armazena todas as despesas registradas.
  * `tipos_despesa.txt`: Armazena as categorias.
  * `usuarios.txt`: Armazena os logins e senhas.

### 3\. Segurança

  * **Hashing de Senhas:** As senhas dos usuários **não** são salvas em texto puro. Elas passam por um processo de hash usando **SHA-256** (via `CriptografiaUtil`) antes de serem armazenadas no arquivo `usuarios.txt`. O login é feito comparando o hash da senha digitada com o hash salvo.

-----

## 🏃 Como Executar

O projeto pode ser compilado e executado diretamente via linha de comando.

1.  **Pré-requisitos:**

      * Ter o JDK (Java Development Kit) 11 ou superior instalado e configurado no PATH.

2.  **Compilação:**
    Navegue até o diretório raiz do projeto (onde o `README.md` está) e execute:

    ```bash
    # Se você organizou em pacotes (main, models, services, utils)
    javac main/Main.java models/*.java services/*.java utils/*.java

    # Se todos os arquivos .java estão no mesmo diretório (sem pacotes)
    javac *.java
    ```

3.  **Execução:**

    ```bash
    # Se estiver usando pacotes
    java main.Main

    # Se não estiver usando pacotes
    java Main
    ```

-----

## 🔒 Credenciais Padrão

Ao executar o sistema pela primeira vez, os arquivos `.txt` não existirão. O sistema criará automaticamente um usuário administrador padrão para o primeiro acesso:

  * **Login:** `admin`
  * **Senha:** `admin`

Também serão criadas algumas categorias padrão de despesa (Alimentação, Transporte, etc.).