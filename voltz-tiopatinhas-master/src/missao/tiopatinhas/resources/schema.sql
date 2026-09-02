spring.datasource.url=jdbc:oracle:thin:@oracle.fiap.com.br:1521:orcl
 /*enderco da conexao com o BD, qual driver do Oracle vai usar, o servidos que está hospedado,
a porta padrão do Oracle Database e o nome do servico*/
spring.datasource.username=RM566546 /*define o usuario*/
spring.datasource.passaword=fiap26 /*define a senha*/
spring.datasource.driver-class-name=oracle.jdc.OracleDriver /*diz ao Spring boot qual driver jdbc usar*/


spring.sql.init.mode=always /*diz para o SB executar scripts sql sempre que o projeto iniciar*/
spring.sql.init-locations=classpath.sq/schema.sql /*indica onde o sql deve ser executado*/