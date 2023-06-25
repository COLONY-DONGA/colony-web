const header = `
<header>
<!-- 전체박스 -->
<div class="header-box">
  <!-- 메뉴박스 -->
  <div class="menu-box">
    <!-- 그림자 -->
    <div class="shadow"></div>
    <!-- 본체박스 -->
    <div class="big-box"></div>
    <!-- 이미지 -->
    <img class="mini-logo" src="../static/img/colony.jpg" />

    <!-- 이름님 안녕하세요 타임리프!!-->
    <div class="welcome-box" th:text="${question.content}">이름 님 안녕하세요</div>
    <div class="mypage-box">
      <a href="mypage.html">마이페이지</a>
    </div>

      <div class="login-box">
        <a href="login.html">로그인</a>
      </div>
    </div>
  </div>
</div>
</header>


`;

export default header;

// .qa-container {
//   background-color: #ffffff;
//   height: 102.4rem;
//   overflow: hidden;
//   position: relative;
//   width: 100%;
// }
