# 소셜 미디어 통합 feed 서비스

## 목차
1. [개발 기간](#개발-기간)
2. [기술 스택](#기술-스택)
3. [프로젝트 개요](#프로젝트-개요)
4. [구현 기능 목록](#구현-기능-목록)
5. [ERD](#ERD)
6. [구현 과정](#구현-과정)
7. [담당한 역할](#담당한-역할)
8. [API 명세서](#API-명세서)
9. [테스트](#테스트)

## 개발 기간
2023-10-25 ~

## 기술 스택
<img src="https://img.shields.io/badge/spring-6DB33F?style=for-the-badge&logo=spring&logoColor=white" alt="spring"/> <img src="https://img.shields.io/badge/spring data jpa-6DB33F?style=for-the-badge&logo=spring&logoColor=white" alt="spring data jpa"/> <img src="https://img.shields.io/badge/spring security-6DB33F?style=for-the-badge&logo=springSecurity&logoColor=white" alt="spring security"/> <img src="https://img.shields.io/badge/mysql-4479A1?style=for-the-badge&logo=mysql&logoColor=white" alt="mysql"/>

## 프로젝트 개요
여러 SNS의 게시물들을 한번에 관리하는 애플리케이션입니다.
해시태그로 원하는 게시물을 조회할 수 있으며, 좋아요 및 공유도 가능합니다.

## 구현 기능 목록
* 유저
    * 회원가입
    * 가입승인
    * 로그인

* 게시물
    * 게시물 목록
    * 게시물 상세
    * 게시물 좋아요
    * 게시물 공유

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

## 담당한 역할
* [김정훈](https://github.com/jhva): 유저 인증/인가
* [곽민성](https://github.com/kawkmin): 프로젝트 환경 설정
* [김선재](https://github.com/mizuirohoshi7): docs 작성
* [송인규](https://github.com/IngyuSong): 게시물 CRUD
* [최소영](https://github.com/soyeong125): 게시물 CRUD

## API 명세서

## 테스트