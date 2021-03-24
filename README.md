# OpenSrcSW

### kuir 
자바 클래스들을 실행해주는 main 클래스

<br/><br/><br/>

### makeCollection 
html 파일들을 사용자가 원하는 경로에서 가져와 하나의 xml 파일로 묶어주는 프로그램

##### 실행 후 생성되는 파일
- collection.xml

##### 사용법
-  java kuir -c [path]

<br/><br/><br/>

### makeKeyword

지정 경로에서 makeCollection에 의해 만들어진 collection.xml 파일 내용의 형태소를 분석하는 프로그램. kkma 라이브러리 사용

##### 실행 후 생성되는 파일
- index.xml


##### 사용법
- java kuir -k [path/collection.xml]

<br/><br/><br/>

### makeInvertedFile
makeKeyword로 생성된 index.xml을 가져와 키워드 별 문서 당 가중치를 계산하는 프로그램. 가중치는 아래의 식에 의해 계산함. 생성 된 파일은 역파일 (Invertedd File) 형태로 저장됨.

$$ _{x \ : \ 단어} \ _{y \ : \ 문서} \\\\
_{tf_{x,y} \ : \ 문서 \ y에서 \ 단어 \ x가 \ 등장한 \ 횟수} \\\\
_{df_{x} \ : \ 단어 \ x가 \ 몇 \ 개의 \ 문서에서 \ 등장하는지} \\\\
_{N \ : \ 전체 \ 문서의 \ 수} \ _{일 \ 때,} $$

$$ W_{x,y}  = tf_{x,y} \times \log{ \left(N \over df_x \right) } $$

##### 실행 후 생성되는 파일
- index.post


##### 사용법
- java kuir -i [path/index.xml]
