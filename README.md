# OpenSrcSW

###### 건국대학교 오픈소스 SW 입문 (2021 1학기) 수업을 위한 repository 입니다. 

##### 개발환경
mac OS - terimnal, Source Tree (git), Visual Studio Code  
Widows - cmd, Eclipse

에서 실습 및 테스트 진행 중 입니다.

<br/><br/>

## kuir 
자바 클래스들을 실행해주는 main 클래스 <b>java kuir 옵션</b>으로 다른 클래스를 실행해준다. 각 클래스를 실행하기 위한 옵션과 경로 등은 아래에서 기술. 

<br/><br/><br/>

## makeCollection 
html 파일들을 사용자가 원하는 경로에서 가져와 하나의 xml 파일로 묶어주는 프로그램

##### 실행 후 생성되는 파일
- collection.xml

##### 사용법
-  java kuir -c [path]

<br/><br/><br/>

## makeKeyword

지정 경로에서 makeCollection에 의해 만들어진 collection.xml 파일 내용의 형태소를 분석하는 프로그램. kkma 라이브러리 사용

##### 실행 후 생성되는 파일
- index.xml


##### 사용법
- java kuir -k [path/collection.xml]

<br/><br/><br/>

## indexer
makeKeyword로 생성된 index.xml을 가져와 키워드 별 문서 당 가중치를 계산하는 프로그램. 가중치는 아래의 식에 의해 계산함. 생성 된 파일은 역파일 (Invertedd File) 형태로 저장됨.
<br/>
##### 공식 
<small>x : 단어, y : 문서, tf : 문서 y에서 단어 x가 등장한 횟수
df : 단어 x가 몇개의 문서에서 등장하는 지, N : 전체 문서의 수 일 때, </small>

<img src="https://latex.codecogs.com/svg.latex?W_{x,y}%20%20=%20tf_{x,y}%20\times%20\log{%20\left(N%20\over%20df_x%20\right)%20}" title="https://latex.codecogs.com/svg.latex?W_{x,y}%20%20=%20tf_{x,y}%20\times%20\log{%20\left(N%20\over%20df_x%20\right)%20}" /></img>

<br/>
##### 실행 후 생성되는 파일
- index.post
- result.txt : index.post를 확인하기 위해 index.post의 hashmap을 result.txt에 출력함 (start 함수에서 주석처리 된 부분을 주석 해제하였을 때 생성)


##### 사용법
- java kuir -i [path/index.xml]

<br/><br/><br/>
## searcher
- index.post를 지정한 경로에서 가져와 -q 뒤에 query (검색할 문장)을 입력하여 문서 간의 유사도를 계산하여 입력한 query와 가장 유사한 문서 중 상위 3개의 문서의 title을 출력. 이 때 유사도가 동일할 경우 문서의 id가 빠른 순서대로 출력하게 됨.
- 문서의 title은 collection.xml 파일을 가져와 (kuir 함수의 collectionPath 변수 안에 경로가 저장되어 searcher.start로 해당 변수를 보냄) 파일의 title과 id를 매칭.
- System.out.println으로 출력.

<br/>
##### 공식 
공식은 내적을 이용한 방법과 코사인을 이용한 방법이 있다.

<br/>
######<b>- 내적 기반 유사도</b>

<small>query문에서의 각 키워드에 대한 weight(TF)를 아래 첨자가 q인 w로 나타내고, 문서에서 키워드에 대한 weight을 아래 첨자가 문서의 id인 방식으로 나타낸다면 내적을 구하는 방법은 아래와 같다. </small>

<img src="https://latex.codecogs.com/svg.latex?Q\cdot id _{0} = w _{q}^{1} w _{0}^{1} + w _{q}^{2} w _{0}^{2} + w _{q}^{3} w _{0}^{3} + w _{q}^{4} w _{0}^{4}" title="https://latex.codecogs.com/svg.latex?Q\cdot id _{0} = w _{q}^{1} w _{0}^{1} + w _{q}^{2} w _{0}^{2} + w _{q}^{3} w _{0}^{3} + w _{q}^{4} w _{0}^{4}" /></img>
<img src="https://latex.codecogs.com/svg.latex?Q\cdot id _{1} = w _{q}^{1} w _{1}^{1} + w _{q}^{2} w _{1}^{2} + w _{q}^{3} w _{1}^{3} + w _{q}^{4} w _{1}^{4}" title="https://latex.codecogs.com/svg.latex?Q\cdot id _{1} = w _{q}^{1} w _{1}^{1} + w _{q}^{2} w _{1}^{2} + w _{q}^{3} w _{1}^{3} + w _{q}^{4} w _{1}^{4}" /></img>
<img src="https://latex.codecogs.com/svg.latex?Q\cdot id _{2} = w _{q}^{1} w _{2}^{1} + w _{q}^{2} w _{2}^{2} + w _{q}^{3} w _{2}^{3} + w _{q}^{4} w _{2}^{4}" title="https://latex.codecogs.com/svg.latex?Q\cdot id _{2} = w _{q}^{1} w _{2}^{1} + w _{q}^{2} w _{2}^{2} + w _{q}^{3} w _{2}^{3} + w _{q}^{4} w _{2}^{4}" /></img>
<img src="https://latex.codecogs.com/svg.latex?Q\cdot id _{3} = w _{q}^{1} w _{3}^{1} + w _{q}^{2} w _{3}^{2} + w _{q}^{3} w _{3}^{3} + w _{q}^{4} w _{3}^{4}" title="https://latex.codecogs.com/svg.latex?Q\cdot id _{3} = w _{q}^{1} w _{3}^{1} + w _{q}^{2} w _{3}^{2} + w _{q}^{3} w _{3}^{3} + w _{q}^{4} w _{3}^{4}" /></img>


<br/>
###### <b>- 코사인 기반 유사도</b>

<small>query문에서의 각 키워드에 대한 weight(TF)를 아래 첨자가 q인 w로 나타내고, 문서에서 키워드에 대한 weight을 아래 첨자가 문서의 id인 방식으로 나타낸다면 위에서 구한 내적과 코사인 유사도 공식을 이용하여 새로운 유사도를 구할 수 있다. 코사인 유사도 공식이 아래와 같을 때,</small>

<img src="https://latex.codecogs.com/svg.latex?cos(\theta ) = \frac{A \cdot B}{\left\| A\right\| \left\| B\right\|} = \frac{\left| A \cdot B\right|}{\sqrt{A}\sqrt{B}}" title="https://latex.codecogs.com/svg.latex?cos(\theta ) = \frac{A \cdot B}{\left\| A\right\| \left\| B\right\|} = \frac{\left| A \cdot B\right|}{\sqrt{A}\sqrt{B}}" />
<br/><br/>
<small>이 때, Qid는 위의 내적 유사도의 값이다. 따라서 코사인 유사도 공식과 내적의 값을 알면 모든 문서의 유사도를 구할 수 있다. 아래는 0번 문서에 대한 유사도만 구한 것이며, 다른 문서도 같은 방식으로 구해준다. </small> 

<img src="https://latex.codecogs.com/svg.latex?Sim (Q, id _{0}) =\frac{Q \cdot id _{0}}{\sqrt{(w _{q}^{1}) ^{2} + (w _{q}^{2}) ^{2} + (w _{q}^{3}) ^{2} + (w _{q}^{4}) ^{2}} \sqrt{(w _{0}^{1}) ^{2} + (w _{0}^{2}) ^{2} + (w _{0}^{3}) ^{2} + (w _{0}^{4}) ^{2}}} 
" title="https://latex.codecogs.com/svg.latex?Sim (Q, id _{0}) =\frac{Q \cdot id _{0}}{\sqrt{(w _{q}^{1}) ^{2} + (w _{q}^{2}) ^{2} + (w _{q}^{3}) ^{2} + (w _{q}^{4}) ^{2}} \sqrt{(w _{0}^{1}) ^{2} + (w _{0}^{2}) ^{2} + (w _{0}^{3}) ^{2} + (w _{0}^{4}) ^{2}}} 
" />

<br/>

##### 실행 후 생성되는 파일
- 생성되는 파일 없음 (System.out.println으로 결과 출력)


##### 사용법
- java kuir -s [path/index.post] -q "입력할 쿼리문"