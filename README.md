💻 PetShield Frontend By android studio

***처음 프로젝트 시작하기***

1. **android studio Git 설정**
    - 먼저, IntelliJ에서 프로젝트를 엽니다.
    - **`File`** > **`Settings`** > **`Version Control`** > **`Git`**로 이동하여 Git이 올바르게 설치되었는지 확인하세요.
2. **GitHub 리포지토리 추가**
    - android studiod의 **`VCS`** 메뉴에서 **`Import into Version Control`** > **`Create Git Repository`**를 선택하여 로컬 Git 리포지토리를 생성합니다.
    - 터미널에서 프로젝트 디렉토리로 이동하여 다음 명령어를 실행합니다:
        
        ```bash
        git init
        ```
        
3. **GitHub 원격 리포지토리 설정**
    - 원격 리포지토리를 추가하려면 터미널에서 다음 명령어를 사용하세요:
        
        ```bash
        git remote add origin https://github.com/PetShield2024/FrontEnd.git
        ```
        
4. **새 브랜치 생성 및 이동**
    - 새로운 브랜치를 생성하고 해당 브랜치로 이동합니다:
        
        ```bash
        git checkout -b seungeun/first
        ```
        
5. **변경 사항 커밋**
    - 모든 변경 사항을 스테이징하고 커밋합니다:
        
        ```bash
        git add .
        git commit -m "[Feat]: 새로운 기능 추가"
        ```
        
6. **브랜치 푸시**
    - 로컬 브랜치를 원격 리포지토리의 새로운 브랜치로 푸시합니다:
        
        ```bash
        git push -u origin seungeun/first
        ```
        
7. 오류 발생시
    
    ```bash
    git pull origin seungeun/first --allow-unrelated-histories
    ```

***두번째 이후 프로젝트 실행 시***
1. 메인에 있는 내용 받기
    
    ```bash
    git pull origin dev
    ```
2. **새 브랜치 생성 및 이동**
    - 새로운 브랜치를 생성하고 해당 브랜치로 이동합니다:
        
        ```bash
        git checkout -b seungeun/first
        ```
        
3. **변경 사항 커밋**
    - 모든 변경 사항을 스테이징하고 커밋합니다:
        
        ```bash
        git add .
        git commit -m "[Feat]: 새로운 기능 추가"
        ```
        
4. **브랜치 푸시**
    - 로컬 브랜치를 원격 리포지토리의 새로운 브랜치로 푸시합니다:
        
        ```bash
        git push -u origin seungeun/first
        ```
        
### 📌 commit 하기

- 파트 별로 작업을 끝냈거나 더 작은 단위로 작업을 끝낼 때마다 add 후에 commit을 해 주는 게 좋아요
- 반드시 자신의 브랜치에서만 commit을 해주세요‼️
- commit 형식
    - [Feat]: 새로운 기능 추가
    - [Fix]: 버그 수정
    - [Docs]: 문서 수정
    - [Style]: 코드 포맷팅, 세미콜론 누락, 코드 변경이 없는 경우
    - [Refactor]: 코드 리펙토링
    - [Test]: 테스트 코드, 리펙토링 테스트 코드 추가
    - [Chore]: 빌드 업무 수정, 패키지 매니저 수정
    
    🖥️ `git add 자신이 수정한 파일명`
    
    🖥️ `git commit -m “[Feat(이름)] ㅇㅇ기능 추가”`
    

### 📤 Github 레파지토리에 적용하기

작업을 모두 끝냈다면, 프로젝트 변경 사항을 remote repository(github repository)에 push 합니다.

🖥️ git push origin 본인의 브랜치명(깃허브 닉네임)

push를 완료했다면 스터디 repository에서 pull request를 진행합니다

(자신의 브랜치에 push 후에 github 페이지 들어오면 아마 뜰거에요!)

PR(Pull Request)시 메세지 제목은 다음과 같이 [이름] oo기능 완료. 라고 적은 후 create pull request 버튼을 눌러주세요

PR : base: [master] <- compare: [본인 github 아이디]


❤️ 깃 사용법 참고) https://umc-smwu.notion.site/Git-2aea3e9a04724a08881b8e61e306e5b1
