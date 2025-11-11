# Subscription Manager App

정기 구독 서비스를 한 곳에서 관리하는 **Android 구독 관리 앱**입니다.  
Netflix, YouTube Premium, Spotify 등 다양한 구독을 추가하고, 다음 결제일과 월간 총 지출을 쉽게 확인할 수 있습니다.

본 프로젝트는 **클린 아키텍처 + MVVM + Hilt + Coroutine Flow** 기반으로 작성되며, 실무형 구조를 목표로 합니다.

---

1. 프로젝트 목표

- 정기 구독 서비스를 효율적으로 관리할 수 있는 앱을 만든다.
- Clean Architecture를 적용하여 레이어 간 역할을 명확히 분리한다.
- Local(Room) + Remote(Mock API) 동기화를 구현한다.
- Hilt 기반 의존성 주입(DI)을 적용하여 확장성과 테스트 용이성을 높인다.



2. Features (예정)

- 구독 추가 / 삭제 / 수정
- 다음 결제일 자동 계산
- 월간 총 지출 계산
- 구독 카테고리 표시
- Local(Room) 저장소
- Remote(Mock API) 데이터 동기화
- Subscription 관리 UseCase
- StateFlow 기반 UI 상태 관리



3. Architecture

본 프로젝트는 아래 아키텍처를 따릅니다

    3-1. Clean Architecture
        - **Domain** — 앱의 비즈니스 로직 (UseCase, Entity, Repository Interface)
        - **Data** — Repository 구현체, Local/Remote 데이터소스
        - **Presentation** — UI(View) + ViewModel (MVVM 구조)

    3-2. 기술 스택
        - Kotlin
        - MVVM
        - Hilt (DI)
        - Retrofit2 + OkHttp
        - Room
        - Coroutine + StateFlow
        - Jetpack Lifecycle
