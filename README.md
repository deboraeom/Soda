"# Soda" <br>
Esse é uma api para controle de refrigerantes, nele é possível registrar as informações do refrigerante, assim como atualizar a quantidade em estoque, sendo elas:<br>
<ul>
<li>name</li>
<li>flavor</li>
<li>type</li>
<li>quantity</li>
<li>max</li>
</ul><br>
<br>

Será necesserário criar um banco de dandos Mysql com nome soda, e um usuário Maria com as permissões de criar, alterar, deletar tabelas, assim como as permissões necessárias para manipular os dados da tabela, no mysql faça: <br> 
<ul>
<li>create user soda;</li>
<li>create database soda;</li>
<li>GRANT ALL PRIVILEGES ON soda. * TO 'maria'@'localhost';</li>
</ul><br>
Ou reconfigurar o aquivo ApplicationProperties de acordo com seu banco e nomes de preferência<br>

Para listar, criar, e atualizar  "Soda" use a url:<br>
http://localhost:8080/api/v1/soda<br>

Ao criar envie no corpo da mensagem os dados do refrigerante sem id(caso envie será ignorado).<br>
E ao atualizar envie no corpo da mensagem os dados do refrigerante com id.<br>

Para excluir use a url com id do refrigerante:<br>
http://localhost:8080/api/v1/soda/id<br>

Para procurar um refrigerante específico use a url com o nome do refrigerante:<br>
http://localhost:8080/api/v1/soda/name<br>

Exemplo: refrigerante com o nome mate couro:<br>
http://localhost:8080/api/v1/soda/mate couro<br>

Para aumentar a quantidade de refrigerante use (patch):<br>
http://localhost:8080/api/v1/soda/{id}/increment<br>

exemplo url para soda com o id 1:<br>
http://localhost:8080/api/v1/soda/1/increment<br>

Para diminuir a quantidade de refrigerante use (patch):<br>
http://localhost:8080/api/v1/soda/{id}/decrement<br>

exemplo url para soda com o id 1:
http://localhost:8080/api/v1/soda/1/decrement<br>

Para aumentar ou diminuir envie no corpo da mensagem a quantidade("quantity").

O retorno das exceptions são padronizadas:<br>

Quando se envia com preenchimento errado, retorna-se:<br>

{<br>
    "title": "Bad Request Exception, Invalid Fields",<br>
    "status": 400,<br>
    "details": "Check the field(s) error",<br>
    "developerMessage": "org.springframework.web.bind.MethodArgumentNotValidException",<br>
    "timestamp": "[Hora que ocorreu]",<br>
    "fields": "[campos que ocorreram",<br>
    "fieldsMessage": "[o que faltou]"<br>
}<br>

Quando o refrigerante requisitada não existe retorn-se<br>:

{<br>
    "title": "NOT FOUND",<br>
    "status": 404,<br>
    "details": "Soda with name [name] not found!",<br>
    "developerMessage": "com.example.SodaApi.Exception.SodaNotFound",<br>
    "timestamp": "[Hora que ocorreu]"<br>
}<br>


Essa api tem testes unitários desenvolvidos com Junit<br>
