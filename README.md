#### Considerações Gerais
API de votação feita em Scala utilizando [Finagle](https://finagle.github.io/) que é um framework para construção de sistemas distribuídos extensível e configurável para a JVM. Segue lista de depêndencias utilizadas:
- Joda-time - Iniciei utilizando `SimpleDateFormat` porém o mesmo não é
  thread-safe, o que causou alguns problemas. Devido a isso, resolvi utilizar Joda, pois não possui esse problema e possui uma API excelente.
- Mockito-core - Biblioteca para mocks.
- Scalatest - Biblioteca excelente para testes em Scala, a qual já possuo
  experiência.
- Redis - `Data structure server` do tipo key-value.

=====================
#### Porquês
- Finagle - Queria um Framework que me disponibilizasse uma boa velocidade de
  desenvolvimento, e que escalasse facilmente. E o finagle se encaixou muito bem já que ele é uma
  camada bem fina em cima do [Netty](http://netty.io/) o que torna a aplicação
  muito performática, pois o código roda em pouquíssimas camadas e com
  baixo nível de abstração.
- Scala - Com a escolha do Finagle fiquei com duas opções, Java ou Scala. Além
  de já trabalhar com Scala o que tornou a escolha fácil, deixo aqui alguns
  motivos pelo qual escolhi Scala: Inferência de tipos muito boa,
  Firs-class functions, Traits e Pattern Matching.
- Finagle-Redis - Pensei em utilizar o Jedis, porém essa versão do Redis é
  non-blocking, logo foi a escolhida.

#### Design
- Com alguma leitura vi fazia mais sentido colocar todas as informações
da entidade buscada na chave, já que os dados de cada entidade não eram muitos e o custo da operação
seria muito menor `O(1)`.

=====================

#### Benchmark

#### Rotas

##### `/contests/`

- `GET /contests` -
![get contests](https://github.com/leonmaia/vote-api/blob/master/benchmark-images/contest-get.png)

- `POST /contests` - cria um `Contest`
![post contests](https://github.com/leonmaia/vote-api/blob/master/benchmark-images/contest-post.png)

* * *

##### `/candidates/`

- `GET /contest/:contest_slug/candidates` -
![get candidates](https://github.com/leonmaia/vote-api/blob/master/benchmark-images/candidates-get.png)

- `POST /contest/:contest_slug/candidates` - cria um `Candidate` para um `Contest`
![post candidates](https://github.com/leonmaia/vote-api/blob/master/benchmark-images/candidate-post.png)

* * *

##### `/votes/`

- `GET /votes/:contest_slug` -
- `GET /votes/:contest_slug/:id_candidate` -
- `POST /votes/:contest_slug/:id_candidate` -
![post votes](https://github.com/leonmaia/vote-api/blob/master/benchmark-images/vote-post-2.png)
![post votes withou user token](https://github.com/leonmaia/vote-api/blob/master/benchmark-images/vote-post-user-token-blocked.png)

===============================================
#### Como rodar

##### Com Vagrant
* $ git clone git@github.com:leonmaia/vote-api.git
* $ vagrant up --provision

##### Local
* $ git clone git@github.com:leonmaia/vote-api.git
* $ cd /{{ pasta do projeto }}
* $ ./sbt assembly
* $ java -Dfile.encoding=UTF-8 -jar target/scala-2.11/mystique-api-assembly-1.0.jar

##### Local - Dev Mode
* $ git clone git@github.com:leonmaia/vote-api.git
* $ cd /{{ pasta do projeto }}
* $ ./sbt run

#### Considerações finais sobre automação
Utilizei Vagrant juntamente com [Ansible](http://www.ansible.com/) para fazer o provisionamento da máquina e [Upstart](http://upstart.ubuntu.com/).
