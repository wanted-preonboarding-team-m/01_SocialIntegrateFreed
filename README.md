# 소셜 미디어 통합 feed 서비스

## 목차
1. [개발 기간](#개발-기간)
2. [기술 스택](#기술-스택)
3. [프로젝트 개요](#프로젝트-개요)
4. [구현 기능 목록](#구현-기능-목록)
5. [ERD](#ERD)
6. [구현 과정](#구현-과정)
7. [담당한 역할](#담당한-역할)
8. [테스트](#테스트)

## 개발 기간
2023-10-25 ~ 2023-10-30

## 기술 스택
<img src="https://img.shields.io/badge/spring-6DB33F?style=for-the-badge&logo=spring&logoColor=white" alt="spring"/> <img src="https://img.shields.io/badge/spring data jpa-6DB33F?style=for-the-badge&logo=spring&logoColor=white" alt="spring data jpa"/> <img src="https://img.shields.io/badge/querydsl-6DB33F?style=for-the-badge&logo=spring&logoColor=white" alt="spring"/> <img src="https://img.shields.io/badge/spring security-6DB33F?style=for-the-badge&logo=springSecurity&logoColor=white" alt="spring security"/> <img src="https://img.shields.io/badge/mysql-4479A1?style=for-the-badge&logo=mysql&logoColor=white" alt="mysql"/>

## 프로젝트 개요
여러 SNS의 게시물들을 한번에 관리하는 서비스입니다.

검색한 해시태그가 포함된 게시물들을 하나의 서비스에서 확인할 수 있습니다.

이를 통해 본 서비스의 고객은 하나의 채널로 유저, 브랜드 등 SNS 노출 게시물 및 통계를 확인할 수 있습니다.

좋아요, 공유 기능도 SNS를 각각 이용하지 않고 한번에 수행할 수 있습니다.

## 구현 기능 목록
* 유저
    * 회원가입
    * 가입승인
    * 로그인

* 게시물
    * 게시물 목록
    * 게시물 상세
    * 게시물 좋아요 (실제 연동은 구현되지 않았습니다.)
    * 게시물 공유 (실제 연동은 구현되지 않았습니다.)

* 통계
    * 해시태그를 날짜 혹은 시간으로 조회

## ERD
![erd](https://github.com/wanted-preonboarding-team-m/.github/assets/142835195/656e203d-677d-41a6-8109-b12db38047f9)

## 구현 과정
1. [프로젝트 환경 설정](https://github.com/wanted-preonboarding-team-m/01_SocialIntegrateFreed/issues/1)
    * application.yml 설정
    * P6Spy 설정
    * RestDocs 설정
    * Response Api Format 설정
    * [공통 예외 처리](https://github.com/wanted-preonboarding-team-m/01_SocialIntegrateFreed/issues/5)


2. 유저 기능 구현
    * [유저 엔티티 생성](https://github.com/wanted-preonboarding-team-m/01_SocialIntegrateFreed/issues/2)
    * [유저 등록 구현](https://github.com/wanted-preonboarding-team-m/01_SocialIntegrateFreed/issues/6)


3. 게시물 기능 구현
    * [게시물 엔티티 생성](https://github.com/wanted-preonboarding-team-m/01_SocialIntegrateFreed/issues/7)
    * [게시물 CRUD 구현](https://github.com/wanted-preonboarding-team-m/01_SocialIntegrateFreed/issues/11)
    * [좋아요 및 공유 기능](https://github.com/wanted-preonboarding-team-m/01_SocialIntegrateFreed/issues/33)
  
4. 통계 기능 구현
   * [쿼리 파라미터로 검색하여 통계 결과 생성](https://github.com/wanted-preonboarding-team-m/01_SocialIntegrateFreed/issues/22)

## 담당한 역할
* [김정훈](https://github.com/jhva): 유저 인증/인가
* [곽민성](https://github.com/kawkmin): 프로젝트 환경 설정
* [김선재](https://github.com/mizuirohoshi7): 통계, 좋아요 및 공유 기능
* [최소영](https://github.com/soyeong125): 게시물 CRUD
* 코드 리뷰 및 리팩토링은 조원 모두가 함께 했습니다.

## 테스트
<img width="347" alt="2" src="https://github.com/wanted-preonboarding-team-m/01_SocialIntegrateFreed/assets/142835195/a9488331-001e-486c-bf7c-67872496d36e">
<img width="343" alt="1" src="https://github.com/wanted-preonboarding-team-m/01_SocialIntegrateFreed/assets/142835195/7db6ce75-5b88-4718-835f-81c7d25e0a0c">

단위 테스트로 각 계층을 분리했습니다.
