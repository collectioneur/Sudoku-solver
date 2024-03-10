document.addEventListener('DOMContentLoaded', (event) => {
  const board = document.getElementById('sudoku-board');
  for (let i = 0; i < 81; i++) {
    const input = document.createElement('input');
    input.type = 'text';
    input.maxLength = 1;
    input.classList.add('sudoku-cell');
    input.id = 'cell-' + i;
    input.addEventListener('input', function(e) {
      if (e.target.value !== '' && !e.target.value.match(/^[1-9]$/)) {
        e.target.value = '';
        alert('Incorrect symbol');
      }
      else {
        const cells = document.querySelectorAll('.sudoku-cell');
        const row = Math.floor(i / 9);
        const col = i % 9;
        const blockRow = Math.floor(row / 3) * 3;
        const blockCol = Math.floor(col / 3) * 3;

        for (let k = 0; k < 9; k++) {
          if (((cells[row * 9 + k].value === e.target.value && k !== col) ||
            (cells[k * 9 + col].value === e.target.value && k !== row)) && e.target.value !== '') {
            e.target.value = '';
            alert('Duplicate number in row or column');
            return;
          }


          const r = blockRow + Math.floor(k / 3);
          const c = blockCol + k % 3;
          if (cells[r * 9 + c].value === e.target.value && (r !== row || c !== col) && e.target.value !== '') {
            e.target.value = '';
            alert('Duplicate number in block');
            return;
          }
        }
      }
    });

    if ((i + 1) % 3 === 0) input.classList.add('right-border');
    if (Math.floor(i / 9) % 3 === 2) input.classList.add('bottom-border');
    if(Math.floor(i / 9) % 3 === 0) input.classList.add('top-border');
    if(i % 3 === 0) input.classList.add('left-border');
    board.appendChild(input);
  }
});

document.getElementById('solve').addEventListener('click', solveSudoku);

document.getElementById('clear').addEventListener('click', clearBoard);

function clearBoard() {
  const cells = document.querySelectorAll('.sudoku-cell');
  for(let i = 0; i <  cells.length; i++) {
    cells[i].value = '';
  }
}

function solveSudoku(event) {
  event.preventDefault();

  const cells = document.querySelectorAll('.sudoku-cell');
  let sudokuData = [];
  cells.forEach(cell => {
    sudokuData.push(cell.value || "0");
  });


  fetch('http://localhost:8080/solve', {
    method: 'POST',
    headers: {
      'Content-Type': 'application/json'
    },
    body: JSON.stringify(sudokuData)
  }).then(response => response.json())
    .then(data => {
      data.forEach((value, index) => {
        cells[index].value = value;
      });
    });
}
