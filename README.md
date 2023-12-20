# TDD 연습

[참고 사이트](https://mangkyu.tistory.com/182)

위의 사이트를 참고하여 TDD를 연습해보자.

[TDD 구현 예제 3](https://mangkyu.tistory.com/184)
까지는 보고 따라했으며,
[TDD 구현 예제 4](https://mangkyu.tistory.com/185) 부터는 직접 구현해보았다.

1. 멤버십 전체 조회 API 구현

기능

- 내가 가진 모든 멤버십을 조회한다.
- 요청: 사용자 식별값
- 응답: {멤버십 ID, 멤버십 이름, 포인트, 가입 일시}의 멤버십 리스트

## TODO List

- [x] **_userId로 내가 가진 모든 멤버십을 조회하는 repository의 메서드를 구현한다._**
- [x] _**조회했을 때 아무것도 없는 경우와 하나 이상 있는 경우를 구분하여 테스트한다.**_
- [ ] userId로 내가 가진 모든 멤버십을 조회하는 service의 메서드를 구현한다.
- [ ] userId로 멤버십 전체 조회 API를 구현한다.