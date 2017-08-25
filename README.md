# desafiojava

Desafio em Java baseado nos requisitos de https://github.com/concretesolutions/desafio-java

## URL da aplicação
https://desafiojava-gdartibale.herokuapp.com

## Endpoints da aplicação

**Cadatrar Usuário**
----
  Cadastra um usuário recebendo um JSON

* **URL**

  /user

* **Method:**

  `POST`
  
*  **URL Params**

   **Required:**
 
   `name=[string]`
   `email=[string]`
   `password=[string]`
   `phones=[array string]`

* **Data Params**

   `name=[string]`
   `email=[string]`
   `password=[string]`
   `phones=[array string]`
   
* **Sample Data:**
    **Content:** `{"name": "Gustavo D.","email": "gustavo@gustavo.com.br","password": "123456789","phones": [{"number": "999999999","ddd": "99"},{"number": "111111111","ddd": "11"}]}`
 
* **Success Response:**

  * **Code:** 200 <br />
    **Content:** `{ {
          "id": 1,
          "created": "Aug 25, 2017 1:34:09 AM",
          "modified": "Aug 25, 2017 1:34:09 AM",
          "last_login": "Aug 25, 2017 1:34:09 AM",
          "token": "eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJHdXN0YXZvIEQuIn0.pJuOmxl0VcsGqMmUd672clqxdemOfN15XaCfWRl6QByajAFLSgyedh6dQXCpCjA2yQvDan2IOBPoa4kbteste"
      } }`
 
* **Sample Error Response:**

  * **Code:** 401 UNAUTHORIZED <br />
    **Content:** `{ mensagem : "E-mail já existente." }`

**Login Usuário**
----
  Realiza o login do usuário

* **URL**

  /login

* **Method:**

  `POST`
  
*  **URL Params**

   **Required:**
 
   `email=[string]`
   `password=[string]`

* **Data Params**

   `email=[string]`
   `password=[string]`
   
* **Sample Data:**
    **Content:** ` {"email": "gustavo@gustavo.com.br", "password": "123456789"}`
 
* **Success Response:**

  * **Code:** 200 <br />
    **Content:** `{ {
          "id": 1,
          "created": "Aug 25, 2017 1:34:09 AM",
          "modified": "Aug 25, 2017 1:34:09 AM",
          "last_login": "Aug 25, 2017 1:34:09 AM",
          "token": "eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJHdXN0YXZvIEQuIn0.pJuOmxl0VcsGqMmUd672clqxdemOfN15XaCfWRl6QByajAFLSgyedh6dQXCpCjA2yQvDan2IOBPoa4kbteste"
      } }`
 
* **Sample Error Response:**

  * **Code:** 401 UNAUTHORIZED <br />
    **Content:** `{ mensagem : "Usuário e/ou senha inválidos." }`
    
 **Consume Usuário**
----
  Realiza consulta do usuário a partir do token

* **URL**

  /consume

* **Method:**

  `POST`
  
*  **URL Params**

   **Required:**
 
   `id=[string]`
   `token=[string]`

* **Data Params**

   `id=[string]`
   `token=[string]`
   
* **Sample Data:**
    **Content:** `  {"id": 1,"token": "eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJHdXN0YXZvIEQuIn0.pJuOmxl0VcsGqMmUd672clqxdemOfN15XaCfWRl6QByajAFLSgyedh6dQXCpCjA2yQvDan2IOBPoa4kbteste"}`
 
* **Success Response:**

  * **Code:** 200 <br />
    **Content:** {
        "id": 1,
        "name": "Gustavo D.",
        "email": "gustavo@gustavo.com.br",
        "phones": [
            {
                "number": "999999999",
                "ddd": "99"
            },
            {
                "number": "111111111",
                "ddd": "11"
            }
        ],
        "created": "Aug 24, 2017 9:56:52 PM",
        "modified": "Aug 24, 2017 9:56:52 PM",
        "last_login": "Aug 25, 2017 1:40:47 AM"
    }`
 
* **Sample Error Response:**

  * **Code:** 401 UNAUTHORIZED <br />
    **Content:** `{ mensagem : "Sessão inválida." }`
