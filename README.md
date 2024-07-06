# 지하철 노선도 미션
[ATDD 강의](https://edu.nextstep.camp/c/R89PYi5H) 실습을 위한 지하철 노선도 애플리케이션

---
# 세부 스토리 정리

### 스토리 1: 나는 관리자로서 지하철 노선을 생성하여 새로운 노선을 추가하고 싶다.
1. [x] 노선 등록시 노선명, 노선 색, 상행종점역 id, 하행종점역 id, 거리를 함께 등록하고 생성된 결과를 응답받는다.
2. [ ] 노선 등록시 필수 요청 프로퍼티가 없으면 에러를 발생시킨다. 
3. [ ] 노선 등록시 이미 등록된 노선을 등록하면 에러를 발생 시킨다.
4. [ ] 노선 등록시 이미 등록된 컬러를 등록하면 에러를 발생 시킨다. 
5. [ ] 노선 등록시 거리가 0 이하면 에러를 발생시킨다.

### 스토리 2: 나는 관리자로서 지하철 노선 목록을 조회하여 모든 노선을 관리하고 싶다.
1. [x] 모든 노선과 해당하는 지하철역을 모두 반환한다.

### 스토리 3: 나는 관리자로서 특정 지하철 노선을 조회하여 해당 노선의 정보를 확인하고 싶다.
1. [x] 특정 노선에 해당하는 정보와 역을 반환한다.
2. [ ] 노선이 존재하지 않으면 빈값을 반환한다.

### 스토리 4: 나는 관리자로서 지하철 노선을 수정하여 변경된 정보를 반영하고 싶다.
1. [ ] 수정하려는 노선이 존재하지 않으면 에러를 발생시킨다
2. [ ] 수정시 이미 등록된 노선이름을 등록하면 에러를 발생시킨다.
3. [ ] 수정시 이미 등록된 컬러를 등록하면 에러를 발생시킨다.
4. [ ] 수정시 거리가 0 이하면 에러를 발생시킨다.
5. [x] 노선을 수정한후 조회시 수정된 정보가 반환된다.

### 스토리 5: 나는 관리자로서 특정 지하철 노선을 삭제하여 불필요한 노선을 제거하고 싶다.
1. [ ] 삭제하려는 노선이 존재하지 않으면 에러를 발생시킨다.
2. [ ] 삭제하려는 노선이 존재하면 삭제한다.


# 애그리게이트 정리
## 역


## 노선

## 노선역
