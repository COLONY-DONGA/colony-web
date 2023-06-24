//백엔드 코드 아직 안읽고 임의로 데이터만 생성했지용.
var data = [
  {
    question: {
      content: '질문 1',
      answers: [
        {
          content: '답변 1-1',
          date: '2023-05-01',
        },
        {
          content: '답변 1-2',
          date: '2023-05-02',
        },
      ],
    },
  },
  {
    question: {
      content: '질문 2',
      answers: [
        {
          content: '답변 2-1',
          date: '2023-05-03',
        },
      ],
    },
  },
  {
    question: {
      content: '질문 3',
      answers: [],
    },
  },
];

// qa-container 요소 선택
var container = document.getElementById('qa-container');

// 데이터 배열을 순회하며 각각의 question과 answer를 조합하여 요소 생성
data.forEach(function (item, questionIndex) {
  // question 요소 생성
  var questionContentElement = document.createElement('p');
  questionContentElement.className = 'qa-question';
  questionContentElement.id = 'question-' + (questionIndex + 1) + '-1';
  questionContentElement.textContent = item.question.content;

  // answer 요소 생성
  var answerGroupElement = document.createElement('div');
  answerGroupElement.className = 'qa-answer-group';
  answerGroupElement.id = 'answer-group-' + (questionIndex + 1) + '-1';

  item.question.answers.forEach(function (answer, answerIndex) {
    var answerElement = document.createElement('p');
    answerElement.className = 'qa-answer';
    answerElement.id = 'answer-' + (questionIndex + 1) + '-' + (answerIndex + 1);
    answerElement.textContent = answer.content;

    var dateElement = document.createElement('p');
    dateElement.className = 'qa-date';
    dateElement.id = 'question-date-' + (questionIndex + 1) + '-' + (answerIndex + 1);
    dateElement.textContent = '질문 일자: ' + answer.date;

    answerGroupElement.appendChild(answerElement);
    answerGroupElement.appendChild(dateElement);
  });

  // question과 answer를 포함하는 요소 생성
  var qaItemContentElement = document.createElement('div');
  qaItemContentElement.className = 'qa-item-content';
  qaItemContentElement.id = 'question-content-' + (questionIndex + 1);
  qaItemContentElement.appendChild(questionContentElement);
  qaItemContentElement.appendChild(answerGroupElement);

  container.appendChild(qaItemContentElement);
});
