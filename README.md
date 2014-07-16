Har Megido
==========

 *"E vi três espíritos malignos semelhantes a rãs; eles saíram da boca do dragão, da boca da besta, e da boca do falso profeta. Eles são espíritos de demônios, que fazem prodígios; os quais vão à procura dos reis da terra e de todo o mundo, para os congregar para a batalha, naquele grande dia do Deus Todo-Poderoso. [...]    
   Eles então se congregaram num lugar que em hebreu chama-se Har Megido."*     
   Apocalise 16:13-16


A batalha entre o bem e o mal está se espalhando pelo planeta, e é hora de você escolher por qual lado lutar.

Os homens que desejarem ser guerreiros da luz serão transformados em Anjos, e devem lutar pela justiça, verdade e virtude.
A eles é dado o poder de abençoar objetos, para espalharem o máximo possível a coragem, a esperança e a luz.

Os homens que desejarem ser guerreiros das trevas serão transformados em Demônios, e devem espalhar o caos, o medo, o sofrimento e a dor.
A eles é dado o poder de amaldiçoar objetos, para espalharem o máximo possível o desespero, a desolação e as trevas.

Os Anjos se reúnem em grupos chamados de Falanges, e os Demônios em Legiões.

A batalha final, o Har Megido, começou, e as suas escolhas irão influenciar se o futuro será de luz ou escuridão.  

<br/>

**História**

O Har Megido (Armageddon) é a batalha final entre o bem e o mal, os seres de luz e de trevas. 
O palco de batalha é o planeta Terra, e as forças já começaram a se enfrentar. 
Para aumentar o seu poder de exército, ambos os lados estão buscando guerreiros, que compartilhem a visão de sua causa. 

Os Anjos querem guerreiros para transformar o planeta em uma fonte perpétua de luz e expurgar de 
uma vez por todas a mácula das trevas.

Já os Demônios querem guerreiros para transformar o mundo na completa escuridão, onde apenas o caos e o desespero
imperam. Eles estão aguardando a hora de sua vingança desde que Lúcifer foi expulso do paraíso.

O conflito entre essas forças no nosso mundo perturba o equilíbrio, transformando a realidade.   


**Estética**

O jogo utilizará gráficos em duas dimensões (2D), interpolados com a imagem da câmera do dispositivo.   

A câmera do dispositivo cria uma simulação de realidade aumentada ao se "encantar" objetos do mundo real, tornando o meio da interação (o dispositivo) transparente nas ações efetuadas pelo jogador.  

A interação do jogo se dá através de indicadores na tela que indicam a ação dos dois grupos.  

Um retorno tátil (vibração) é utilizado para aprimorar a experiência e possibilitar maior imersão.  

Efeitos sonoros são explorados para auxiliar na imersão.  


**Tecnologia**

O jogo consiste em duas aplicações: uma rodando em um ambiente fixo (o servidor do jogo), e outra no dispositivo dos jogadores.  

O jogo utilizará visão computacional como parte fundamental de sua mecânica, permitindo a interação com objetos do mundo real [1].


Ambiente:

O SmartSpace do jogo pode ser qualquer local com acesso à internet e objetos reais espalhados (esses objetos podem ser quaisquer: canetas, computadores, sapato, cadeira e etc), ou seja, o jogo busca ser o mais ubíquo possível utilizando os recursos disponíveis sem prejuízo à experiência.  

Dispositivo:

- O jogo requer um computador (PC) com acesso à internet para ser o servidor do jogo
- Os jogadores necessitam de dispositivos com sistema operacional Android que tenham acesso à internet e possuam câmera.


Recursos e Serviços:

Os serviços criados do jogo são aqueles necessários para prover as ações do jogo:

Para o servidor, foram criados os seguintes serviços:

- Criar Partida: cria uma nova partida;
- Encontrar Partida: encontra uma partida a partir de seu nome;
- Retornar Pontuação: retorna a pontuação de uma partida a partir de seu nome;
- Executar Alteração de Estado: faz a alteração no estado de uma partida. Essa alteração engloba encantamentos, desencantamentos, inclusão de jogadores na partida, exclusão de jogadores na partida, e troca do time de um jogador;
- Listar Partidas: lista todas as partidas ativas no momento;
- Listar Jogadores: lista todos os jogadores conectados a uma partida específica;
- Recuperar a hora do servidor: Retorna o horário do servidor, para fins de sincronização de tempo entre o cliente e o servidor.

Para o cliente, foi criado o serviço de Executar Alteração de Estado, com as mesmas características do serviço do servidor.


**Mecânica**

Antes de iniciar a partida o grupo de jogadores se divide em dois grupos (de Anjos e de Demônios), que se enfrentarão.
O objetivo de ambos os grupos é *encantar* o maior número possível de objetos, ao mesmo tempo que evita o grupo inimigo de fazer o mesmo.

Para um jogador encantar um objeto, basta ele apontar seu *recurso câmera* ao objeto (essa ação exige um custo em tempo, variável de acordo com o tipo de encantamento[2]). Essa ação encanta não somente aquele objeto, como também objetos similares que possuem aquela mesma *forma*[3]. O objeto encantado deve ser estático/rígido, por exemplo, um monitor de computador que está passando imagens que mudam muito não poderá ser encantado.

Depois que um objeto for encantando, os jogadores da equipe inimiga terão uma "clarividência" do objeto que foi encantado, com uma visão distorcida daquilo que o jogador encantou.

Para um jogador desencantar um objeto, ele deve apontar a câmera para o objeto que foi previamente encantado (essa ação também custa tempo[2]).

Os objetos encantados acumulam pontos de acordo com o tipo de encantamento, e do tempo em que estão encantados[4].

Os objetos desencantados acumulam pontos para aqueles que os desencantarem. O número de pontos recebido desta forma é fixo, independente do tempo que o objeto está encantado.

O jogo termina quando o tempo limite expirar ou um grupo conseguir a pontuação necessária para vencer (o que ocorrer primeiro). É possível haver empate.

A pontuação de cada jogador será mostrada, como também das equipes como um todo.


[1] Uma simples prova de conceito foi desenvolvida para verificar se será possível. Vídeo de demonstração: [Demonstração de detecção de objetos](http://youtu.be/w9FDezrSpI8) 

[2] Inicialmente, o único encantamento possível será bênção ou maldição. O custo em tempo dele deve ser em torno de 5 segundos (tanto para desencantar como para encantar).

[3] Isso significa que a *forma* do objeto é capturada, e não o objeto especificamente. Em outras palavras, se uma cadeira foi encantada, então qualquer cadeira parecida, com aquela mesma forma, pode ser utilizada para desencantar. Isso incentivará os jogares a buscarem objetos únicos para encantar, visto que objetos comuns serão mais facilmente desencantados.

[4] Quanto mais tempo um objeto estiver encantado, mais "forte" aquele objeto ficará, e mais pontos ele concede por intervalo de tempo (até um limite máximo).


[Detalhamento da Mecânica](https://github.com/vvgaming/HarMegido/blob/master/docs/mecanica.md) 

[Ideias](https://github.com/vvgaming/HarMegido/blob/master/docs/ideias.md)
