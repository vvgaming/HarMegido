Har Megido
==========


A batalha entre o bem e o mal está se espalhando pelo planeta, e é hora de você escolher por qual lado lutar.

Os homens que desejarem ser guerreiros da luz serão transformados em Anjos, e devem lutar pela justiça, verdade e virtude.
A eles é dado o poder de abençoar objetos, para espalharem o máximo possível a coragem, a esperança e a luz.

Os homens que desejarem ser guerreiros das trevas serão transformados em Demônios, e devem espalhar o caos, o medo, o sofrimento e a dor.
A eles é dado o poder de amaldiçoar objetos, para espalharem o máximo possível o desespero, a desolação e as trevas.

Os Anjos se reúnem em grupos chamados de Falanges, e os Demônios em Legiões.

A batalha final, o Har Megido, começou, e as suas escolhas irão influenciar se o futuro será de luz ou escuridão.  

<br/>

**Estética**

O jogo utilizará gráficos 2D, principalmente interpolados sobre a imagem da câmera. 
A interação com o jogo visa misturar a realidade com o mundo do jogo onde ele poderá interagir (através da câmera) com objetos reais. 
Efeitos sonoros serão explorados como parte integrante da mecânica, e também para auxiliar na imersão.


**História**

O Har Megido (Armageddon) é a batalha final entre o bem e o mal, os seres de luz e de trevas. 
O palco de batalha é o planeta Terra, e as forças já começaram a se enfrentar. 
Para aumentar o seu poder de exército, ambos os lados estão buscando guerreiros, que compartilhem a visão de sua causa. 

Os Anjos querem guerreiros para transformar o planeta em uma fonte perpétua de luz e expurgar de 
uma vez por todas a mácula das trevas.

Já os Demônios querem guerreiros para transformar o mundo na completa escuridão, onde apenas o caos e o desespero
imperam. Eles estão aguardando a hora de sua vingança desde que Lúcifer foi expulso do paraíso.


O conflito entre essas forças no nosso mundo perturba o equilíbrio, transformando a realidade.   


**Tecnologia**

O jogo utilizará o middleware uOS para, mas não limitado a:
- Promover conectividade entre os dispositivos através de rede Wi-Fi, usando um controlador central;
- Possibilitar o controle de tela de dispositivos dentro da rede onde o jogo acontece (PC, Celular, etc.);
- Possibilitar o controle de áudio de dispostivos dentro da rede do middleware.

O jogo utilizará visão computacional como parte fundamental de sua mecânica, permitindo a interação com objetos reais [1].

Sendo n jogadores, com n > 1, são necessários no mínimo:
- n dispositivos onde o uOS possa ser executado, com recurso de câmera, tela e áudio (um dispositivo para cada jogador);
- 1 dispositivo onde o uOS possa ser executado, para servir como servidor central.

Havendo mais dispositivos disponíveis do que jogadores, os dispositivos restantes serão utilizados para prover maior integração e recursos de realidade aumentada para o jogo. Os recursos neutros de áudio e vídeo serão utilizados para prover ambiência, reproduzindo sons e imagens que te remetem a atual situação da partida.

O SmartSpace do jogo pode ser qualquer local com rede e objetos reais espalhados (esses objetos podem ser quaisquer: canetas, computadores, sapato, cadeira e etc), ou seja, o jogo busca ser o mais ubíquo possível utilizando os recursos disponíveis quando estiverem disponível sem prejuízo à experiência mínima.

As aplicações executadas nos dispositivos pessoais proverão serviços equivalente às ações do jogo: encantar, desencantar e etc.


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
