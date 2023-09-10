# Dong-A Colony Web

## 목차

1. [**웹 서비스 소개**](#1)
1. [**주요 기능**](#2)
1. [**주요 화면**](#3)
1. [**기술 스택**](#4)
1. [**멤버**](#5)
1. [**Software Architecture**](#6)
1. [**ERD**](#7)

<br/>

<div id="1"></div>

## 🖥️ 서비스 소개
colony-web은 동아대학교 정보보안동아리 colony의 자체제작 웹 서비스입니다.   
colony-web은 동아리원들 사이 정보 교류등의 다목적 커뮤니티입니다.   
재학생 뿐만 아니라 졸업생과의 연결부 역할을 하여 더 높은 수준의 정보를 제공 받을 수 있습니다.   
#### <a href="http://bit.ly/dau-colony" target="_blank">배포된 서비스로 이동</a>

<br/>

<div id="2"></div>

## 🎯 주요 기능

#### 1. 글 작성 및 답변 기능
유저들은 웹 서비스 내에서 특정 주제나 관심사에 대한 글을 작성할 수 있습니다.  
다른 유저들은 이러한 글에 답변을 제공하거나, 답변에 대한 추가 질문이 있을 시 댓글을 이용할 수 있습니다.
원활한 소통을 위해 댓글과 대댓글로 구현하였습니다. 
정보 및 지식 공유가 서비스의 가장 큰 목적이기에 부모 댓글이 사라져도 자식 댓글은 사라지지 않도록 구성했습니다.  
답변에 도움이 되었다면 좋아요를 누를 수 있고 이는 계정당 1회로 제한됩니다.

<p align="center">
 <img src="https://github.com/COLONY-DONGA/colony-web/assets/69447192/8c5b9bb0-9cf8-4970-b583-31340753d708">
</p>


#### 2. 다양한 카테고리별 게시판


*  **공지사항** : 동아리의 중요한 소식이나 안내사항을 공유하는 곳입니다.  
* **Q&A** : 졸업생들에게 질문을 하고 그에 대한 답변을 얻을 수 있는 게시판입니다.  
* **스터디** : 스터디 관련 질문이나 정보를 공유하는 게시판입니다.  
* **기타** : 동아리와 관련된 다양한 주제나, 그 외의 일반적인 토론을 위한 게시판입니다.

 

#### 3. 알림 구독 서비스
* **실시간 알림** : 회원들은 웹 사이트에 로그인 하면서 실시간 알림 서비스를 받을 수 있습니다.
* **이메일 알림** : 로그인을 하지 않고도 언제 어디서나 이메일을 통한 알림을 받을 수 있습니다.

<p align="center">
 <img src="https://github.com/COLONY-DONGA/colony-web/assets/69447192/dc9ba668-d9b3-4378-976c-5f7cb11f30e9">
</p>



#### 4. 마이 페이지
* 회원은 자신이 작성했던 질문, 답변, 댓글 등을 볼 수 있고 관리할 수 있습니다.
* 본인이 작성한 답변에 눌린 좋아요 수를 조회할 수 있습니다.
* 개인정보 변경 및 이메일 푸쉬 알림 동의를 변경할 수 있습니다. 


<br/>

<div id="3"></div>

## 🔍 주요 화면

|        |        |
| ------ | ------ |
|<img src="https://github.com/COLONY-DONGA/colony-web/assets/69447192/0a991798-82bc-4c91-ab19-26e81327ecdf">  |    <img src="https://github.com/COLONY-DONGA/colony-web/assets/69447192/852bf12e-22e8-47e6-847e-2429b553c38a">     |
|    <div align=center> `메인 화면` </div>    |    <div align=center> `질문 등록` </div>    |
|    <img src="https://github.com/COLONY-DONGA/colony-web/assets/69447192/60b18a65-e943-4f9a-b458-3e359764c98b">    |    <img src="https://github.com/COLONY-DONGA/colony-web/assets/69447192/59b31654-01d7-42d7-9fc8-195cc378391f">    |
|    <div align=center> `질문 상세` </div>    |    <div align=center> `마이페이지` </div>    |




<br/>

<div id="4"></div>

## 📚 STACKS
<div> 
  <img src="https://img.shields.io/badge/java-007396?style=for-the-badge&logo=java&logoColor=white"> 
  <img src="https://img.shields.io/badge/spring-6DB33F?style=for-the-badge&logo=spring&logoColor=white">
  <img src="https://img.shields.io/badge/Spring Boot-6DB33F?style=for-the-badge&logo=spring boot&logoColor=white">
  <img src="https://img.shields.io/badge/mysql-4479A1?style=for-the-badge&logo=mysql&logoColor=white">
  <img src="https://img.shields.io/badge/Spring Security-6DB33F?style=for-the-badge&logo=springsecurity&logoColor=white">
  <img src="https://img.shields.io/badge/swagger-85EA2D?style=for-the-badge&logo=swagger&logoColor=white">
  <p></p>
  
  <img src="https://img.shields.io/badge/GitHub Actions-2088FF?style=for-the-badge&logo=GitHub Actions&logoColor=white">
  <img src="https://img.shields.io/badge/EC2-FF9900?style=for-the-badge&logo=Amazon EC2&logoColor=white">
  <img src="https://img.shields.io/badge/RDS-527FFF?style=for-the-badge&logo=Amazon RDS&logoColor=white">
  <img src="https://img.shields.io/badge/S3-569A31?style=for-the-badge&logo=Amazon S3&logoColor=white">
  <img src="https://img.shields.io/badge/figma-F24E1E?style=for-the-badge&logo=figma&logoColor=white">
  <p></p>  
   
  <img src="https://img.shields.io/badge/github-181717?style=for-the-badge&logo=github&logoColor=white">
  <img src="https://img.shields.io/badge/git-F05032?style=for-the-badge&logo=git&logoColor=white">
  <img src="https://img.shields.io/badge/Notion-000000?style=for-the-badge&logo=Notion&logoColor=white">
  

  <p></p>
  <img src="https://img.shields.io/badge/html5-E34F26?style=for-the-badge&logo=html5&logoColor=white">
  <img src="https://img.shields.io/badge/css-1572B6?style=for-the-badge&logo=css3&logoColor=white">
  <img src="https://img.shields.io/badge/javascript-F7DF1E?style=for-the-badge&logo=javascript&logoColor=black">
  <img src="https://img.shields.io/badge/jQuery-0769AD?style=for-the-badge&logo=jQuery&logoColor=white">
</div>
<br/>

<br/>

<div id="5"></div>

## 👥 멤버
* Backend: [김진수(팀장)](https://github.com/dgjinsu) [채승지](https://github.com/ChaeSeungJi)
* Frontend: [박유진](https://github.com/yujinn00) [최유현](https://github.com/Choiyuhyeon)
* Project Manager: [박태민](https://github.com/DLLegs)

<br/>

<div id="6"></div>

## 🛠 Software Architecture
![image](https://github.com/dgjinsu/shop-1/assets/97269799/07dc46f6-4cc0-40a6-84a5-c4970202f5ab)  

<br/>
<div id="7"></div>

## 🛠 ERD
![image](https://github.com/COLONY-DONGA/colony-web/assets/97269799/b4a3843f-7359-4561-ac4d-a286fcf32800)


## 트러블 슈팅
