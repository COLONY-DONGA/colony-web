const header = `
  <header>
    <nav>
      <ul>
        <li><a href="#">Home</a></li>
        <li><a href="#">About</a></li>
        <li><a href="#">Services</a></li>
        <li><a href="#">Contact</a></li>
      </ul>
    </nav>
  </header>
`;

const footer = `
  <footer>
    <div class="container">
      <p>&copy; 2023 My Website. All rights reserved.</p>
      <ul>
        <li><a href="#">Privacy Policy</a></li>
        <li><a href="#">Terms of Service</a></li>
        <li><a href="#">Sitemap</a></li>
      </ul>
    </div>
  </footer>
`;

export { header, footer };

// // headerFooter.js
// // 헤더를 로드하고 페이지에 삽입
// fetch('header.html')
//   .then((response) => response.text())
//   .then((data) => {
//     document.getElementById('header-placeholder').innerHTML = data;
//   });

// // 푸터를 로드하고 페이지에 삽입
// fetch('footer.html')
//  xw .then((response) => response.text())
//   .then((data) => {
//     document.getElementById('footer-placeholder').innerHTML = data;
//   });
//
// 헤더,푸터 css적용 안해서 주석처리 해놓았음
