# Calculadora RMI Distribuída

Projeto desenvolvido para a disciplina de **Sistemas Distribuídos** (Nota de Aula 15 — Objetos Distribuídos com RMI), do curso de Engenharia de Computação — IFSul Minas Campus Machado.

---

## Visão Geral

A aplicação implementa uma **calculadora remota** utilizando Java RMI (*Remote Method Invocation*), permitindo que um cliente chame métodos matemáticos que são executados em um servidor remoto de forma transparente.

```
┌──────────────────┐        RMI / TCP         ┌──────────────────────┐
│  Cliente (GUI)   │  ──────────────────────▶  │  Servidor            │
│  CalculatorGUI   │  ◀──────────────────────  │  Calculator (impl.)  │
└──────────────────┘        resultado           └──────────────────────┘
                                                         │
                                                  rmiregistry :1099
```

---

## Estrutura do Projeto

```
src/
└── br/edu/ifsuldeminas/mch/sd/rmi/
    ├── remote/
    │   └── Operations.java        # Interface remota (contrato RMI)
    ├── server/
    │   ├── Calculator.java        # Implementação das operações
    │   └── Server.java            # Inicializa o rmiregistry e exporta o objeto
    └── client/
        ├── Client.java            # Cliente de linha de comando (original)
        └── CalculatorGUI.java     # Cliente com interface gráfica Swing
```

---

## Operações Disponíveis

### Básicas
| Operação       | Método RMI              |
|----------------|-------------------------|
| Soma           | `sum(x, y)`             |
| Subtração      | `sub(x, y)`             |
| Multiplicação  | `mul(x, y)`             |
| Divisão        | `div(x, y)`             |

### Avançadas
| Operação              | Método RMI                  | Observações                                |
|-----------------------|-----------------------------|--------------------------------------------|
| Raiz quadrada         | `sqrt(x)`                   |                                            |
| Raiz N-ésima          | `nthRoot(x, n)`             | n = índice da raiz (2, 3, …)               |
| Potência              | `pow(base, exp)`            | base^exp                                   |
| Porcentagem           | `percentage(valor, pct)`    | Retorna `pct% de valor`                    |
| Módulo                | `mod(x, y)`                 | Resto da divisão inteira                   |
| Fatorial              | `factorial(n)`              | Válido para 0 ≤ n ≤ 20 (limite do `long`) |

### Conversão de Bases
| Conversão                 | Método RMI                  |
|---------------------------|-----------------------------|
| Decimal → Binário         | `decimalToBinary(n)`        |
| Decimal → Hexadecimal     | `decimalToHex(n)`           |
| Binário → Decimal         | `binaryToDecimal(s)`        |
| Binário → Hexadecimal     | `binaryToHex(s)`            |
| Hexadecimal → Decimal     | `hexToDecimal(s)`           |
| Hexadecimal → Binário     | `hexToBinary(s)`            |

---

## Pré-requisitos

- Java JDK 8 ou superior
- Eclipse IDE (ou qualquer IDE Java com suporte a projetos simples)

---

## Como Executar

### 1. Compilar o projeto

No Eclipse, basta importar o projeto e compilar normalmente (`Ctrl+B`).  
Pela linha de comando, a partir de `src/`:

```bash
javac -d ../bin $(find . -name "*.java")
```

### 2. Iniciar o Servidor

Execute a classe `Server` como aplicação Java:

```
br.edu.ifsuldeminas.mch.sd.rmi.server.Server
```

Saída esperada no console:
```
Serviço da calculadora rodando...
```

O servidor cria automaticamente o `rmiregistry` na porta **1099** e registra o serviço com o nome `CalculatorService`.

### 3. Iniciar o Cliente (GUI)

Execute a classe `CalculatorGUI` como aplicação Java:

```
br.edu.ifsuldeminas.mch.sd.rmi.client.CalculatorGUI
```

Na interface:
1. Informe o **host** do servidor (padrão: `localhost`)
2. Clique em **Conectar**
3. Navegue pelas abas e use as operações

---

## Interface Gráfica

A GUI é dividida em 4 abas:

| Aba         | Conteúdo                                                      |
|-------------|---------------------------------------------------------------|
| **Básico**  | Soma, Subtração, Multiplicação, Divisão                       |
| **Avançado**| Raiz, Potência, Porcentagem, Módulo, Fatorial                 |
| **Conversão**| Conversão entre Decimal, Binário e Hexadecimal              |
| **Histórico**| Lista de todas as operações realizadas no servidor           |

---

## Tratamento de Erros

| Situação                         | Comportamento                                          |
|----------------------------------|--------------------------------------------------------|
| Divisão por zero                 | Retorna `NaN`                                          |
| Módulo por zero                  | Retorna `NaN`                                          |
| Fatorial de número negativo      | Lança `ArithmeticException` com mensagem descritiva    |
| Fatorial de n > 20               | Lança `ArithmeticException` (estouro de `long`)        |
| Servidor não iniciado            | Exibe mensagem de erro no cliente                      |
| Entrada inválida na conversão    | Exibe mensagem de erro indicando a base esperada       |

---

## Conceitos RMI Utilizados

- **`Remote`** — interface marcadora que indica objeto remoto
- **`RemoteException`** — exceção obrigatória em todos os métodos remotos
- **`UnicastRemoteObject.exportObject`** — exporta o objeto como stub remoto
- **`LocateRegistry.createRegistry`** — cria o registro RMI localmente
- **`Naming.lookup`** — localiza o stub remoto pelo nome de serviço

---

## Autores

Desenvolvido por Arielce Junior, Guilherme Tassinari e Rafael Rabelo como atividade prática da disciplina de Sistemas Distribuídos — IF Sul de Minas Campus Machado.
