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


<small>x : 단어, y : 문서, tf : 문서 y에서 단어 x가 등장한 횟수
df : 단어 x가 몇개의 문서에서 등장하는 지, N : 전체 문서의 수 일 때, </small>

<img src="https://latex.codecogs.com/svg.latex?W_{x,y}%20%20=%20tf_{x,y}%20\times%20\log{%20\left(N%20\over%20df_x%20\right)%20}" title="https://latex.codecogs.com/svg.latex?W_{x,y}%20%20=%20tf_{x,y}%20\times%20\log{%20\left(N%20\over%20df_x%20\right)%20}" /></img>

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


##### 실행 후 생성되는 파일
- 생성되는 파일 없음 (System.out.println으로 결과 출력)


##### 사용법
- java kuir -s [path/index.post] -q "입력할 쿼리문"