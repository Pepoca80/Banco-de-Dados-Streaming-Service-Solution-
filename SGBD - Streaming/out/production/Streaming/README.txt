Instruções de Compilação e Execução (PROJETO BANCO DE DADOS I - STREAMING DE FILMES)

Este documento fornece as instruções para compilar e executar o projeto em Java, que interage com um banco de dados PostgreSQL.
-------------------------------------------------------------------------------------------------------------------------------


Requisitos:
Antes de começar, verifique se você tem os seguintes requisitos instalados:

- Java JDK (versão 8 ou superior)
- PostgreSQL (Certifique-se de que o PostgreSQL está instalado e em funcionamento.)
-------------------------------------------------------------------------------------------------------------------------------

Através do script em SQL enviado pelo grupo, crie o esquema do banco, já com a inserção dos dados.

Após isso, entre no projeto, no arquivo DatabaseConnection e altere a URL, o USER e a PASSWORD, com as informações do seu PostgreSQL, para fazer a conexão com o projeto.

COMPILAÇÃO: 

Para compilar o projeto, no terminal do seu computador, direcione para o diretório do projeto.
Exemplo: cd  C:\Users\nome\Downloads\StreamingSGBD

Já no diretório, compile o projeto da seguinte forma: 

javac -cp <caminho para o arquivo JAR do itext contido no projeto>;<caminho para o arquivo JAR do postgres contido na pasta do projeto>;. *.java
Exemplo: javac -cp "C:\caminho\para\libs\itext-pdfa-5.5.13.3.jar;C:\caminho\para\libs\postgresql-42.5.0.jar;." *.java

Em seguida, execute o projeto de forma semelhante:

java -cp <caminho para o arquivo JAR do itext contido no projeto>;<caminho para o arquivo JAR do postgres contido na pasta do projeto>;. SSS 
Exemplo: java -cp "C:\caminho\para\libs\itext-pdfa-5.5.13.3.jar;C:\caminho\para\libs\postgresql-42.5.0.jar;." SSS


