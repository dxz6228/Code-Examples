using System.ComponentModel;
using System.IO;
using Project_3.Model;

namespace Project_3.ViewModel
{
    ///
    /// Author: Denis Zhenilov
    /// Email: dxz6228@rit.edu
    /// CSCI-541 Project 3 - ViewModel
    /// <summary>
    /// This class implements the ViewModel for the graphical Sudoku puzzle. It handles processing the data a bit to
    /// make it friendlier for the WPF to interact with; and it also contains the logic necessary for validating the
    /// puzzle, manipulating the data based on the input from the WPF side of things, and converting the puzzle to a
    /// string for the saving function to save.
    /// When run, defaults to an Easy puzzle.
    /// </summary>
    public class MainViewModel : INotifyPropertyChanged
    {
        /// <summary>
        /// Stores the (initial) model containing the logic necessary to create a puzzle. Methods from the model are
        /// called to create the puzzle.
        /// </summary>
        SudokuModel sudoku { get; set; }
        /// <summary>
        /// A "flattened" representation of the initial solved board, an array of arrays; an array of columns,
        /// addressable as flattenedBoardArray[x_coordinate][y_coordinate]. Used for the sake of simplicity of
        /// addressing cells on WPF side.
        /// </summary>
        public int[][] flattenedBoardArray { get; set; }
        /// <summary>
        /// A "flattened" representation of the puzzle, an array of arrays; an array of columns,
        /// addressable as flattenedBoardArray[x_coordinate][y_coordinate]. Used for the sake of simplicity of
        /// addressing cells on WPF side. This is what gets modified by the user when attempting to solve the puzzle,
        /// or when choosing to reveal a cell.
        /// </summary>
        public string[][] flattenedPuzzleArray { get; set; }
        /// <summary>
        /// A "flattened" representation of the puzzle, an array of arrays; an array of columns,
        /// addressable as flattenedBoardArray[x_coordinate][y_coordinate]. Used for the sake of simplicity of
        /// addressing cells on WPF side. Contains boolean markers for whether or not a value for a given cell is
        /// known from the start (or has been revealed by the user). Functions as a show toggle for the cell's Label in
        /// XAML.
        /// </summary>
        public bool[][] flattenedIsKnownArray { get; set; }
        /// <summary>
        /// A "flattened" representation of the puzzle, an array of arrays; an array of columns,
        /// addressable as flattenedBoardArray[x_coordinate][y_coordinate]. Used for the sake of simplicity of
        /// addressing cells on WPF side. Contains boolean markers for whether or not a value for a given cell is
        /// unknown from the start (and has not been revealed by the user). Functions as a show toggle for the cell's
        /// ComboBox in XAML.
        /// </summary>
        public bool[][] flattenedIsUnknownArray { get; set; }
        /// <summary>
        /// A "flattened" representation of the puzzle, an array of arrays; an array of columns,
        /// addressable as flattenedBoardArray[x_coordinate][y_coordinate]. Used for the sake of simplicity of
        /// addressing cells on WPF side. Starts out all False; when Reveal is toggled, cells that are still Unknown
        /// have this set to True. Functions as a show toggle for the cell's Show button in XAML.
        /// </summary>
        public bool[][] flattenedShowRevealArray { get; set; }
        /// <summary>
        /// A "flattened" representation of the puzzle, an array of arrays; an array of columns,
        /// addressable as flattenedBoardArray[x_coordinate][y_coordinate]. Used for the sake of simplicity of
        /// addressing cells on WPF side. Starts out all False; when puzzle is Validated, cells that do not match the
        /// initial solved puzzleboard have this set to True. Functions as a show toggle for the cell's red-colored
        /// label, signifying error. Set back to all False after the user clicks OK on the validation popup.
        /// </summary>
        public bool[][] flattenedIsWrongArray { get; set; }
        /// <summary>
        /// An unflattened, 2D array copy of the solved board initially created by the Model.
        /// </summary>
        public int[,] initialBoardState { get; set; }
        /// <summary>
        /// An unflattened, 2D array copy of the initial unsolved puzzle created by the Model.
        /// </summary>
        public int[,] initialPuzzle { get; set; }
        private bool revealToggled = false;
        /// <summary>
        /// Self-explanatory.
        /// </summary>
        public int difficulty { get; set; } // same as SudokuModel, 0 = easy, 1 = medium, 2 = hard
        /// <summary>
        /// A default public constructor for the viewmodel, it defaults it to an easy puzzle.
        /// </summary>
        public MainViewModel()
        {
            this.createEasyPuzzle();
            this.flattenedShowRevealArray = new bool[9][];
            int xCoor = 0;
            while (xCoor < 9)
            {
                var subArray = new bool[9];
                int yCoor = 0;
                while (yCoor < 9)
                {
                    subArray[yCoor] = false;
                    yCoor = yCoor + 1;
                }
                this.flattenedShowRevealArray[xCoor] = subArray;
                xCoor = xCoor + 1;
            }
            NotifyPropertyChanged("flattenedShowRevealArray");
        }
        /// <summary>
        /// A function that "reveals" a previously unknown cell, setting its true value in the flattened Puzzle Array,
        /// marking it as Known, no longer Unknown, and no longer needing to have a Show button revealed. It then
        /// notifies of changes to those properties so when you click on a cell, the button disappears, and you get
        /// its correct value!
        /// </summary>
        /// <param name="xCoord">X coordinate of the cell</param>
        /// <param name="yCoord">Y coordinate of the cell</param>
        public void revealAndHideButton(int xCoord, int yCoord)
        {
            this.flattenedPuzzleArray[xCoord][yCoord] = this.flattenedBoardArray[xCoord][yCoord].ToString();
            this.flattenedIsUnknownArray[xCoord][yCoord] = false;
            this.flattenedIsKnownArray[xCoord][yCoord] = true;
            this.flattenedShowRevealArray[xCoord][yCoord] = false;
            this.revealToggled = false;
            this.untoggleReveal();
            NotifyPropertyChanged("flattenedPuzzleArray");
            NotifyPropertyChanged("flattenedIsUnknownArray");
            NotifyPropertyChanged("flattenedIsKnownArray");
            NotifyPropertyChanged("flattenedShowRevealArray");
        }
        /// <summary>
        /// A function that toggles the reveal mode "off", blanking out the flattenedShowRevealArray array with
        /// all False values, hiding the Show buttons.
        /// </summary>
        public void untoggleReveal()
        {
            this.flattenedShowRevealArray = new bool[9][];
            int xCoor = 0;
            while (xCoor < 9)
            {
                var subArray = new bool[9];
                int yCoor = 0;
                while (yCoor < 9)
                {
                    subArray[yCoor] = false;
                    yCoor = yCoor + 1;
                }
                this.flattenedShowRevealArray[xCoor] = subArray;
                xCoor = xCoor + 1;
            }
            NotifyPropertyChanged("flattenedShowRevealArray");
        }
        /// <summary>
        /// A function that initializes the arrays telling us which cells are Known and which are Unknown so they can
        /// be displayed properly.
        /// </summary>
        private void initializeKnownArrays()
        {
            this.flattenedIsKnownArray = new bool[9][];
            this.flattenedIsUnknownArray = new bool[9][];
            int xCoor = 0;
            while (xCoor < 9)
            {
                var subArray = new bool[9];
                var isUnknownSubArray = new bool[9];
                int yCoor = 0;
                while (yCoor < 9)
                {
                    if (this.initialPuzzle[xCoor, yCoor] == 0)
                    {
                        subArray[yCoor] = false;
                        isUnknownSubArray[yCoor] = true;
                    }
                    else
                    {
                        subArray[yCoor] = true;
                        isUnknownSubArray[yCoor] = false;
                    }
                    yCoor = yCoor + 1;
                }
                this.flattenedIsKnownArray[xCoor] = subArray;
                this.flattenedIsUnknownArray[xCoor] = isUnknownSubArray;
                xCoor = xCoor + 1;
            }
        }
        /// <summary>
        /// A function that creates a new Sudoku model to get a new board, then creates a new Easy puzzle based on
        /// that board, and initializes the Unknown and Known cell arrays.
        /// </summary>
        public void createEasyPuzzle()
        {
            this.sudoku = new SudokuModel(9);
            this.saveInitialBoard();
            this.flattenedBoardArray = new int[9][]; //array of columns, so that [0][0] is 0th col (x=0), 0th elem in col (y=0), or [x][y]
            int xCoor = 0;
            while (xCoor < 9)
            {
                var subArray = new int[9];
                int yCoor = 0;
                while (yCoor < 9)
                {
                    subArray[yCoor] = this.initialBoardState[xCoor, yCoor];
                    yCoor = yCoor + 1;
                }
                flattenedBoardArray[xCoor] = subArray;
                xCoor = xCoor + 1;
            }
            this.difficulty = 0;
            this.sudoku.makePuzzle(this.difficulty);
            this.saveInitialPuzzle();
            this.flattenedPuzzleArray = new string[9][]; //array of columns, so that [0][0] is 0th col (x=0), 0th elem in col (y=0), or [x][y]
            xCoor = 0;
            while (xCoor < 9)
            {
                var subArray = new string[9];
                int yCoor = 0;
                while (yCoor < 9)
                {
                    subArray[yCoor] = this.initialPuzzle[xCoor, yCoor].ToString();
                    yCoor = yCoor + 1;
                }
                flattenedPuzzleArray[xCoor] = subArray;
                xCoor = xCoor + 1;
            }
            initializeKnownArrays();
            this.flattenedIsWrongArray = new bool[9][];
            xCoor = 0;
            while (xCoor < 9)
            {
                var subArray = new bool[9];
                int yCoor = 0;
                while (yCoor < 9)
                {
                    subArray[yCoor] = false;
                    yCoor = yCoor + 1;
                }
                this.flattenedIsWrongArray[xCoor] = subArray;
                xCoor = xCoor + 1;
            }
            NotifyPropertyChanged("flattenedBoardArray");
            NotifyPropertyChanged("flattenedPuzzleArray");
            NotifyPropertyChanged("flattenedIsKnownArray");
            NotifyPropertyChanged("flattenedIsUnknownArray");
            NotifyPropertyChanged("flattenedIsWrongArray");
        }
        /// <summary>
        /// A function that creates a new Sudoku model to get a new board, then creates a new Medium puzzle based on
        /// that board, and initializes the Unknown and Known cell arrays.
        /// </summary>
        public void createMediumPuzzle()
        {
            this.sudoku = new SudokuModel(9);
            this.saveInitialBoard();
            this.flattenedBoardArray = new int[9][]; //array of columns, so that [0][0] is 0th col (x=0), 0th elem in col (y=0), or [x][y]
            int xCoor = 0;
            while (xCoor < 9)
            {
                var subArray = new int[9];
                int yCoor = 0;
                while (yCoor < 9)
                {
                    subArray[yCoor] = this.initialBoardState[xCoor, yCoor];
                    yCoor = yCoor + 1;
                }
                flattenedBoardArray[xCoor] = subArray;
                xCoor = xCoor + 1;
            }
            this.difficulty = 1;
            this.sudoku.makePuzzle(this.difficulty);
            this.saveInitialPuzzle();
            this.flattenedPuzzleArray = new string[9][]; //array of columns, so that [0][0] is 0th col (x=0), 0th elem in col (y=0), or [x][y]
            xCoor = 0;
            while (xCoor < 9)
            {
                var subArray = new string[9];
                int yCoor = 0;
                while (yCoor < 9)
                {
                    subArray[yCoor] = this.initialPuzzle[xCoor, yCoor].ToString();
                    yCoor = yCoor + 1;
                }
                flattenedPuzzleArray[xCoor] = subArray;
                xCoor = xCoor + 1;
            }
            initializeKnownArrays();
            this.flattenedIsWrongArray = new bool[9][];
            xCoor = 0;
            while (xCoor < 9)
            {
                var subArray = new bool[9];
                int yCoor = 0;
                while (yCoor < 9)
                {
                    subArray[yCoor] = false;
                    yCoor = yCoor + 1;
                }
                this.flattenedIsWrongArray[xCoor] = subArray;
                xCoor = xCoor + 1;
            }
            NotifyPropertyChanged("flattenedBoardArray");
            NotifyPropertyChanged("flattenedPuzzleArray");
            NotifyPropertyChanged("flattenedIsKnownArray");
            NotifyPropertyChanged("flattenedIsUnknownArray");
            NotifyPropertyChanged("flattenedIsWrongArray");
        }
        /// <summary>
        /// A function that creates a new Sudoku model to get a new board, then creates a new Hard puzzle based on
        /// that board, and initializes the Unknown and Known cell arrays.
        /// </summary>
        public void createHardPuzzle()
        {
            this.sudoku = new SudokuModel(9);
            this.saveInitialBoard();
            this.flattenedBoardArray = new int[9][]; //array of columns, so that [0][0] is 0th col (x=0), 0th elem in col (y=0), or [x][y]
            int xCoor = 0;
            while (xCoor < 9)
            {
                var subArray = new int[9];
                int yCoor = 0;
                while (yCoor < 9)
                {
                    subArray[yCoor] = this.initialBoardState[xCoor, yCoor];
                    yCoor = yCoor + 1;
                }
                flattenedBoardArray[xCoor] = subArray;
                xCoor = xCoor + 1;
            }
            this.difficulty = 2;
            this.sudoku.makePuzzle(this.difficulty);
            this.saveInitialPuzzle();
            this.flattenedPuzzleArray = new string[9][]; //array of columns, so that [0][0] is 0th col (x=0), 0th elem in col (y=0), or [x][y]
            xCoor = 0;
            while (xCoor < 9)
            {
                var subArray = new string[9];
                int yCoor = 0;
                while (yCoor < 9)
                {
                    subArray[yCoor] = this.initialPuzzle[xCoor, yCoor].ToString();
                    yCoor = yCoor + 1;
                }
                flattenedPuzzleArray[xCoor] = subArray;
                xCoor = xCoor + 1;
            }
            initializeKnownArrays();
            this.flattenedIsWrongArray = new bool[9][];
            xCoor = 0;
            while (xCoor < 9)
            {
                var subArray = new bool[9];
                int yCoor = 0;
                while (yCoor < 9)
                {
                    subArray[yCoor] = false;
                    yCoor = yCoor + 1;
                }
                this.flattenedIsWrongArray[xCoor] = subArray;
                xCoor = xCoor + 1;
            }
            NotifyPropertyChanged("flattenedBoardArray");
            NotifyPropertyChanged("flattenedPuzzleArray");
            NotifyPropertyChanged("flattenedIsKnownArray");
            NotifyPropertyChanged("flattenedIsUnknownArray");
            NotifyPropertyChanged("flattenedIsWrongArray");
        }
        /// <summary>
        /// A function that resets the flattenedIsWrongArray to all False values to hide the error highlighting when
        /// validation messagebox is clicked away by the user.
        /// </summary>
        public void resetValidationArray()
        {
            this.flattenedIsWrongArray = new bool[9][];
            int xCoor = 0;
            while (xCoor < 9)
            {
                var subArray = new bool[9];
                int yCoor = 0;
                while (yCoor < 9)
                {
                    subArray[yCoor] = false;
                    yCoor = yCoor + 1;
                }
                this.flattenedIsWrongArray[xCoor] = subArray;
                xCoor = xCoor + 1;
            }
            NotifyPropertyChanged("flattenedIsWrongArray");
        }
        /// <summary>
        /// A function that validates the puzzle against its initial solved form only; whenever it finds a mismatch,
        /// it will toggle the highlighting for it by setting its corresponding value to True in flattenedIsWrongArray.
        /// It also counts the total number of mistakes in the puzzle. It uses those to generate a string for the
        /// messagebox to display, which is then returned.
        /// </summary>
        /// <returns>A string for the messagebox informing the user of the number of errors in the puzzle,
        /// if any.</returns>
        public string validatePuzzle()
        {
            int yCoor = 0;
            int invalid_cells = 0;
            while (yCoor < 9)
            {
                int xCoor = 0;
                while (xCoor < 9)
                {
                    if (this.flattenedPuzzleArray[xCoor][yCoor] != this.initialBoardState[xCoor, yCoor].ToString())
                    {
                        invalid_cells = invalid_cells + 1;
                        this.flattenedIsWrongArray[xCoor][yCoor] = true;
                    }
                    xCoor = xCoor + 1;
                }
                yCoor = yCoor + 1;
            }
            NotifyPropertyChanged("flattenedIsWrongArray");
            if (invalid_cells > 1)
            {
                return "The puzzle is incorrect. There are " + invalid_cells.ToString() + " cells with errors.";
            }
            else if (invalid_cells == 1)
            {
                return "The puzzle is incorrect. There is 1 cell with an error.";
            }

            return "The puzzle is solved! There were no errors.";
        }
        /// <summary>
        /// A "deep copy" function that saves the initial solved board to a separate 2D array.
        /// </summary>
        private void saveInitialBoard()
        {
            this.initialBoardState = new int[9, 9];
            var yCoor = 0;
            while (yCoor < 9)
            {
                var xCoor = 0;
                while (xCoor < 9)
                {
                    this.initialBoardState[xCoor, yCoor] = this.sudoku.board[xCoor, yCoor];
                    xCoor = xCoor + 1;
                }
                yCoor = yCoor + 1;
            }
        }
        /// <summary>
        /// A function that checks whether or not the board is currently in the reveal mode. If it's not, if will toggle
        /// it, and populate the flattenedShowRevealArray with True values so that all Unknown cells have a Show
        /// button. Otherwise, it will toggle that mode off, and call untoggleReveal to blank out
        /// flattenedShowRevealArray with False values to hide the Show buttons.
        /// </summary>
        public void revealToggle()
        {
            if (!this.revealToggled)
            {
                int xCoor = 0;
                while (xCoor < 9)
                {
                    int yCoor = 0;
                    while (yCoor < 9)
                    {
                        if (this.flattenedIsUnknownArray[xCoor][yCoor])
                        {
                            this.flattenedShowRevealArray[xCoor][yCoor] = true;
                        }
                        yCoor = yCoor + 1;
                    }
                    xCoor = xCoor + 1;
                }
                NotifyPropertyChanged("flattenedShowRevealArray");
                this.revealToggled = true;
            }
            else
            {
                this.revealToggled = false;
                this.untoggleReveal();
            }
        }
        /// <summary>
        /// A deep copy function that saves the initially created unsolved puzzle into a separate 2D array.
        /// </summary>
        private void saveInitialPuzzle()
        {
            this.initialPuzzle = new int[9, 9];
            var yCoor = 0;
            while (yCoor < 9)
            {
                var xCoor = 0;
                while (xCoor < 9)
                {
                    this.initialPuzzle[xCoor, yCoor] = this.sudoku.board[xCoor, yCoor];
                    xCoor = xCoor + 1;
                }
                yCoor = yCoor + 1;
            }
        }
        /// <summary>
        /// A function that converts the initial unsolved puzzle into a simple string representation, with unknown
        /// cells marked with X'es instead of numbers, and saves it to a text file called Puzzle.txt in the current
        /// working directory.
        /// </summary>
        public void savePuzzleToFile()
        {
            string puzzle_string = "";
            int yCoor = 0;
            while (yCoor < 9)
            {
                int xCoor = 0;
                while (xCoor < 9)
                {
                    if (this.initialPuzzle[xCoor, yCoor] != 0)
                    {
                        puzzle_string = puzzle_string + this.initialPuzzle[xCoor, yCoor];
                    }
                    else
                    {
                        puzzle_string = puzzle_string + "X";
                    }
                    xCoor = xCoor + 1;
                }
                if (yCoor < 8)
                {
                    puzzle_string = puzzle_string + "\n";
                }
                yCoor = yCoor + 1;
            }

            File.WriteAllText("Puzzle.txt", puzzle_string);
        }
        protected void NotifyPropertyChanged(string propertyName)
        {
            if (PropertyChanged != null)
            {
                PropertyChangedEventArgs args = new PropertyChangedEventArgs(propertyName);
                this.PropertyChanged(this, args);
            }
        }

        public event PropertyChangedEventHandler PropertyChanged;
    }
}