Documentação inicial

A arquitetura pensada inicialmente era uma mistura de “mestre e escravos” com dutos e filtros e modelo de atores do akka.

A arquitetura implementada foi a de “mestre e escravos” com modelo de atores do akka, pois foi observado que dutos e filtros dividiria o processo em pedaços sequenciais, fato que não ajudaria na otimização do sistema.

O nosso ponto de erosão foi no quesito de leitura, tentamos usar a função charAt() mas não obtivemos sucesso, mas depois de muito pensar usamos a função split() em conjunto com Source.fromFile().getLines e conseguimos um resultado satisfatório.

Componentes: Main; Master; Worker; Listener; using; Matriz; readTextFile.
Conectores: Calculate; Work; Result; Show.

Diario

26/09/2018
Inicia-se o desenvolvimento com uma serie de testes sobre manipulação de arquivos.Criou-se
arquivos de teste ("t1", "t2", "t3" e "t_int") para simular matrizes 3x3 onde t1 representa
a matriz A, t2 a B, t3 a C e t_int a seperação dos inteiros pasitivos e negativos. Os testes
foram feitos atravéz do método charAt(), que não funcionou como esperado pois não soube-se
lidar com as quebras de linha existentes nos arquivos de matrizes.

30/09/2018
Após diversos outros testes, avança-se no quesito "manipulação de arquivos".

Adicionou-se uma nova linha no arquivo build.sbt para que fosse possivel o uso da biblioteca
ARM que, por sua vez, possibilitou o uso da cláusula try / finally para garantir o fechamento
do arquivo após o seu uso.

Descartou-se o uso do método charAt() e, como substituição, passou-se a usar o método split
com auxilio de um vetor para prencher uma matriz de strings para, posteriormante, converter
para uma matriz de int.

A exibição das matrizes (dos arquivos "t1" e "t2") foi feita usando-se o padrão "for dentro
for".

Foram adicionados mais 4 arquivos de teste ("t1.1", "t2.1", "t3.1" e "t4.1_int") para
representar matrizes 3x4 à fim de testes mais precisos (obs: o arquivo "t_int" foi renomeado
para "t4_int").

O teste de gravação foi realizado gravando-se o conteúdo do arquivo "t1" para o arquivo "t3"
(substituindo-se os espaços por barras verticais) além da quantidade de inteiros positivos,
inteiros negativos e zeros (obtida através de respectivos contadores) para o arquivo
"t4_int".

01/10/2018
Avança-se no desenvolvimento com a adição das operações para resolver a fórmula em questão e
do akka com a arquitetura RoundRobinRouter com 4 divisões para os arquivos de teste, com
tempo de execução de 42 milissegundos e 12 para os arquivos oficiais, com tempo de execução
de 1203498 milissegundos (equivalente a 20 minutos, 3 segundos e 498 milissegundos ou 20,0583
minutos). Em relação a matriz C, constou-se 0 inteiros negativos, 1000000 inteiros positivos
e 0 zeros.

02/10/2018
Iniciam-se testes para determinar o número ideal de divisões para leitura dos arquivos
oficiais.

Executando-se com 11 ou 13 divisões, trava-se em determinado instante durante a execução,
enquanto que com 12 divisões a execução ocorre normalmente, indicando que 12 seja o número
ideal de divisões. Entretanto, ao se iniciar novamente o teste com 12 divisões, ocorre uma
variação significativa no tempo de execução, que reduz para 497211 milissegundos (equivalente
a 8 minutos, 17 segundos e 211 milissegundos ou 8,28685 minutos), sendo 706287 milissegundos
(equivalente a 11 minutos, 46 segundos e 287 milissegundos ou 11,77145 minutos) a menos,
indicando um comportamento consideravelmente volátil em relação ao tempo de execução.