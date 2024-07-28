# VocaList

단어장 웹/앱 사이트 만들기

프로젝트 설명
- 스프링 부트를 이용하여 단어장 웹/앱 사이트 만들기
- 유저들이 직접 만든 단어장을 공유하여 다른 유저들도 해당 단어장을 이용할 수 있다.

개발 환경
- 운영체제 : Windows 10
- IDE : IntellJ
- JDK : JDK 21
- DB : MySql
- Build : Gradle

Dependencies
- Spring-data-jpa
- OAuth2-client
- Spring Security
- Spring Web
- Lombok
- MySql Driver

기술 스택
- 백엔드
  SpingBoot, Spring Security, Spring Data Jpa
- 데이터베이스
  MySqL

 DB설계
 - UserEntity
 - UserVocaListEntity
 - VocaContentEntity
 - VocaListEntity

구현 기능
- OAuth2 구글 소셜로그인
- 단어장 조회/생성/수정/삭제/ , 단어장 공개/비공개
- 단어 조회/생성/수정/삭제
- 유저 단어장 불러오기
- 퀴즈 랜덤
