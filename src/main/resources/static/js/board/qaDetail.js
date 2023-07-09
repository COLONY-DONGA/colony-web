var userName = 'hyeon'; // 유저네임!!!(백에서 던져주는 유저네임 변수
// [[${userName}]] 이런식으로 타임리프 식으로 사용 가능

function addReply(event) {
  var comment = event.target.parentElement;
  var reply = document.createElement('div');
  reply.classList.add('reply');

  // 대댓글 작성자와 내용 표시
  var replyAuthorSpan = document.createElement('span');
  replyAuthorSpan.classList.add('name');
  replyAuthorSpan.innerText = '↳(' + userName + ') ';
  var replyContentInput = document.createElement('input');
  replyContentInput.placeholder = '대댓글 내용';

  // 대댓글 작성 버튼 추가
  var replySubmitButton = document.createElement('button');
  replySubmitButton.innerText = '작성';
  replySubmitButton.addEventListener('click', function () {
    var name = replyAuthorSpan.innerText; // 대댓글 작성자를 가져옴
    var text = replyContentInput.value;

    // 대댓글 생성 및 추가
    var replyContent = document.createElement('span');
    replyContent.classList.add('text');
    replyContent.innerText = text;
    var replyAuthor = document.createElement('span');
    replyAuthor.classList.add('name');
    replyAuthor.innerText = name;
    reply.appendChild(replyAuthor);
    reply.appendChild(replyContent);
    comment.getElementsByClassName('replies')[0].appendChild(reply);

    // 입력 폼 삭제
    reply.removeChild(replyAuthorSpan);
    reply.removeChild(replyContentInput);
    reply.removeChild(replySubmitButton);

    // 대댓글을 서버로 전송하여 저장
    var commentId = comment.dataset.commentId;
    saveReply(commentId, name, text);
  });

  // 대댓글 작성 폼 추가
  reply.appendChild(replyAuthorSpan);
  reply.appendChild(replyContentInput);
  reply.appendChild(replySubmitButton);
  comment.getElementsByClassName('replies')[0].appendChild(reply);
}

function saveComment(name, text) {
  // 새 댓글을 서버로 전송하여 저장
  $.ajax({
    type: 'POST',
    url: '/save-comment',
    contentType: 'application/json',
    data: JSON.stringify({
      name: name, //오른쪽이 js변수 새댓글 작성자 name
      text: text, //오른쪽이 js변수 새댓글 댓글텍스트
    }),
    success: function (response) {
      console.log(response);
    },
    error: function (xhr, status, error) {
      console.error(error);
    },
  });
}

function saveReply(commentId, name, text) {
  // 대댓글을 서버로 전송하여 저장
  $.ajax({
    type: 'POST',
    url: '/save-reply',
    contentType: 'application/json',
    dataType: 'json',
    data: JSON.stringify({
      commentId: commentId, //오른쪽 부모댓글!!! name,text가 속해있는 댓글의 식별자
      name: name, //오른쪽이 js변수 대댓글 작성자 name
      text: text, //오른쪽이 js변수 대댓글 텍스트
    }),
    success: function (response) {
      console.log(response);
    },
    error: function (xhr, status, error) {
      console.error(error);
    },
  });
}

// 대댓글 버튼 클릭 이벤트 처리
function addReplyButtonListeners() {
  var replyButtons = document.getElementsByClassName('reply-button');
  for (var i = 0; i < replyButtons.length; i++) {
    replyButtons[i].addEventListener('click', addReply);
  }
}

// 새 댓글 추가 버튼 클릭 이벤트 처리
var addCommentButton = document.getElementById('add-comment-button');
addCommentButton.addEventListener('click', function () {
  var commentsDiv = document.getElementById('comments');
  var newComment = document.createElement('div');
  newComment.classList.add('comment');

  // 댓글 작성자와 내용 표시
  var commentAuthorSpan = document.createElement('span');
  commentAuthorSpan.classList.add('name');
  commentAuthorSpan.innerText = userName + ' : ';
  var commentContentInput = document.createElement('input');
  commentContentInput.placeholder = '댓글 내용';

  // 입력 버튼 추가
  var commentSubmitButton = document.createElement('button');
  commentSubmitButton.innerText = '입력';

  // 대댓글 버튼 추가
  var replyButton = document.createElement('button');
  replyButton.classList.add('reply-button');
  replyButton.innerText = '대댓글';

  // 대댓글 목록 추가
  var repliesDiv = document.createElement('div');
  repliesDiv.classList.add('replies');

  newComment.appendChild(commentAuthorSpan);
  newComment.appendChild(commentContentInput);
  newComment.appendChild(commentSubmitButton);
  newComment.appendChild(replyButton);
  newComment.appendChild(repliesDiv);

  commentsDiv.appendChild(newComment);

  // 입력 버튼 클릭 이벤트 처리
  commentSubmitButton.addEventListener('click', function () {
    var name = commentAuthorSpan.innerText; // 댓글 작성자를 가져옴
    var text = commentContentInput.value;

    // 댓글 생성 및 추가
    var commentContent = document.createElement('span');
    commentContent.classList.add('text');
    commentContent.innerText = text;
    var commentAuthor = document.createElement('span');
    commentAuthor.classList.add('name');
    commentAuthor.innerText = name;
    newComment.insertBefore(commentAuthor, commentContentInput);
    newComment.insertBefore(commentContent, commentContentInput);

    // 입력 폼 삭제
    newComment.removeChild(commentAuthorSpan);
    newComment.removeChild(commentContentInput);
    newComment.removeChild(commentSubmitButton);

    // 댓글을 서버로 전송하여 저장
    saveComment(name, text);

    // 대댓글 버튼 활성화
    replyButton.disabled = false;
  });

  // 대댓글 버튼 클릭 이벤트 처리
  replyButton.addEventListener('click', addReply);

  // 댓글이 추가되었으므로 대댓글 버튼을 비활성화
  replyButton.disabled = true;
});

// 페이지 로드 시에 댓글이 없으므로 대댓글 버튼 비활성화
var replyButtons = document.getElementsByClassName('reply-button');
for (var i = 0; i < replyButtons.length; i++) {
  replyButtons[i].disabled = true;
}

// 페이지 로드 시에 기존 댓글과 대댓글을 가져와서 표시하는 함수
function loadComments() {
  // 서버에서 댓글 데이터를 가져오는 Ajax 요청
  $.ajax({
    type: 'GET',
    url: '/get-comments',
    success: function (response) {
      // 가져온 댓글 데이터를 반복하여 표시
      for (var i = 0; i < response.comments.length; i++) {
        var commentData = response.comments[i];

        // 댓글 요소 생성
        var newComment = document.createElement('div');
        newComment.classList.add('comment');

        // 댓글 작성자와 내용 표시
        var commentAuthor = document.createElement('span');
        commentAuthor.classList.add('name');
        commentAuthor.innerText = commentData.name + ' : ';
        var commentContent = document.createElement('span');
        commentContent.classList.add('text');
        commentContent.innerText = commentData.text;

        newComment.appendChild(commentAuthor);
        newComment.appendChild(commentContent);

        // 대댓글 목록 추가
        var repliesDiv = document.createElement('div');
        repliesDiv.classList.add('replies');
        newComment.appendChild(repliesDiv);

        // 대댓글 작성 버튼 추가
        var replyButton = document.createElement('button');
        replyButton.classList.add('reply-button');
        replyButton.innerText = '대댓글';
        newComment.appendChild(replyButton);

        // 댓글에 대댓글 버튼 클릭 이벤트 처리
        replyButton.addEventListener('click', addReply);

        // 댓글을 댓글 목록에 추가
        var commentsDiv = document.getElementById('comments');
        commentsDiv.appendChild(newComment);

        // 해당 댓글의 대댓글 가져와서 표시
        for (var j = 0; j < commentData.replies.length; j++) {
          var replyData = commentData.replies[j];
          var reply = document.createElement('div');
          reply.classList.add('reply');

          // 대댓글 작성자와 내용 표시
          var replyAuthor = document.createElement('span');
          replyAuthor.classList.add('name');
          replyAuthor.innerText = '↳(' + replyData.name + ') ';
          var replyContent = document.createElement('span');
          replyContent.classList.add('text');
          replyContent.innerText = replyData.text;

          reply.appendChild(replyAuthor);
          reply.appendChild(replyContent);
          repliesDiv.appendChild(reply);
        }
      }

      // 대댓글 버튼 활성화
      addReplyButtonListeners();
    },
    error: function (xhr, status, error) {
      console.error(error);
    },
  });
}

// 페이지 로드 시에 기존 댓글 가져오기
loadComments();