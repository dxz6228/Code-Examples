using System;
using System.Collections.Generic;
using System.ComponentModel;
using System.IO;

namespace Project_3.Model
{
    ///
    /// Author: Denis Zhenilov
    /// Email: dxz6228@rit.edu
    /// CSCI-541 Project 3 - Model
    /// 
    /// <summary>
    /// An implementation for the business logic of a sudoku puzzle generator, that will first generate a valid solution
    /// and will then generate a puzzle from that solution. Contains the methods necessary to gain access to the
    /// board as a 2d array of integers
    /// </summary>
    class SudokuModel : INotifyPropertyChanged
    {
        private int trueDimension;
        public int[,] board { get; set; }
        public int[,] initialBoardState { get; set; }
        public int[,] initialPuzzle { get; set; }
        /// <summary>
        /// A public constructor for a puzzle of given dimension (9x9 or 4x4). It will immediately call the
        /// function necessary to generate the solution, but the function to generate a puzzle from that solution
        /// must be called manually.
        /// </summary>
        /// <param name="dim">Dimension of the square sudoku puzzle, either 9 or 4 for 9x9 or 4x4 respepctively</param>
        public SudokuModel(int dim)
        {
            this.trueDimension = dim;
            this.board = new int[this.trueDimension,this.trueDimension];
            var yCoor = 0;
            while (yCoor < this.trueDimension)
            {
                var xCoor = 0;
                while (xCoor < this.trueDimension)
                {
                    this.board[xCoor, yCoor] = 0; //stub value never encountered in real sudoku.
                    xCoor = xCoor + 1;
                }
                yCoor = yCoor + 1;
            }
            bool puzzleMade = false;
            while (!puzzleMade)
            {
                puzzleMade=this.generateSolutionWithBacktrack(0,0);
            }
            NotifyPropertyChanged("board");
        }
        /// <summary>
        /// A function to check whether there are any duplicates for the offered value in its column, upwards.
        /// </summary>
        /// <param name="xCoord">cell's x coordinate</param>
        /// <param name="yCoord">cell's y coordinate</param>
        /// <param name="toTry">proposed value to insert</param>
        /// <returns>false if duplicates exist, true otherwise</returns>
        private bool backtrackUp(int xCoord, int yCoord, int toTry)
        {
            var backtrackY = yCoord - 1;
            while (backtrackY >= 0)
            {
                if (this.board[xCoord,backtrackY] == toTry)
                {
                    return false;
                }
                backtrackY = backtrackY - 1;
            }
            return true;
        }
        /// <summary>
        /// A function to check whether there are any duplicates for the offered value in its column, downwards.
        /// </summary>
        /// <param name="xCoord">cell's x coordinate</param>
        /// <param name="yCoord">cell's y coordinate</param>
        /// <param name="toTry">proposed value to insert</param>
        /// <returns>false if duplicates exist, true otherwise</returns>
        private bool backtrackDown(int xCoord, int yCoord, int toTry)
        {
            var backtrackY = yCoord + 1;
            while (backtrackY < this.trueDimension)
            {
                if (this.board[xCoord,backtrackY] == toTry)
                {
                    return false;
                }
                backtrackY = backtrackY + 1;
            }
            return true;
        }
        /// <summary>
        /// A function to check whether there are any duplicates for the offered value in its row, to the right.
        /// </summary>
        /// <param name="xCoord">cell's x coordinate</param>
        /// <param name="yCoord">cell's y coordinate</param>
        /// <param name="toTry">proposed value to insert</param>
        /// <returns>false if duplicates exist, true otherwise</returns>
        private bool backtrackRight(int xCoord, int yCoord, int toTry)
        {
            var backtrackX = xCoord + 1;
            while (backtrackX < this.trueDimension)
            {
                if (this.board[backtrackX,yCoord] == toTry)
                {
                    return false;
                }
                backtrackX = backtrackX + 1;
            }
            return true;
        }
        /// <summary>
        /// A function to check whether there are any duplicates for the offered value in its row, to the left.
        /// </summary>
        /// <param name="xCoord">cell's x coordinate</param>
        /// <param name="yCoord">cell's y coordinate</param>
        /// <param name="toTry">proposed value to insert</param>
        /// <returns>false if duplicates exist, true otherwise</returns>
        private bool backtrackLeft(int xCoord, int yCoord, int toTry)
        {
            var backtrackX = xCoord - 1;
            while (backtrackX >= 0)
            {
                if (this.board[backtrackX,yCoord] == toTry)
                {
                    return false;
                }
                backtrackX = backtrackX - 1;
            }
            return true;
        }
        /// <summary>
        /// A function that checks whether or not a sudoku sub-array would contain duplicates given the offered value
        /// to insert. It has "sane" in the name because initially this was a massive block of hardcoded coordinates
        /// with conditions for where to look, before I rewrote it into its current form.
        /// As it stands, it will compute the size of the sub-array and use it to locate the sub-array's local
        /// equivalent of origin (i.e. top-left value), it will then step through the sub-array looking for duplicates.
        /// </summary>
        /// <param name="xCoord">cell's x coordinate</param>
        /// <param name="yCoord">cell's y coordinate</param>
        /// <param name="toTry">proposed value to insert</param>
        /// <returns>false if duplicates exist, true otherwise</returns>
        private bool checkValidSubArraySane(int xCoord, int yCoord, int toTry)
        {
            int lookupX = xCoord;
            int maxIncrements = Convert.ToInt32(Math.Sqrt(this.trueDimension));
            while ((lookupX % maxIncrements) != 0)
            {
                lookupX = lookupX - 1; //find leftmost column in a sub-array
            }
            int lookupY = yCoord;
            while ((lookupY % maxIncrements) != 0)
            {
                lookupY = lookupY - 1; //find topmost row in a sub-array
            }
            int maxX = lookupX + maxIncrements;
            int maxY = lookupY + maxIncrements;
            while (lookupY < maxY)
            {
                while (lookupX < maxX)
                {
                    if (!(lookupX == xCoord && lookupY == yCoord))
                    {
                        if (this.board[lookupX, lookupY] == toTry)
                        {
                            return false;
                        }
                    }
                    lookupX = lookupX + 1;
                }
                lookupX = lookupX - maxIncrements;
                lookupY = lookupY + 1;
            }
            return true;
        }

        /// <summary>
        /// A simple function that will reset the board and generate a new solution just as if you newed the up
        /// another model.
        /// </summary>
        public void newBoard()
        {
            this.board = new int[this.trueDimension, this.trueDimension];
            bool puzzleMade = false;
            while (!puzzleMade)
            {
                puzzleMade=this.generateSolutionWithBacktrack(0,0);
            }
            NotifyPropertyChanged("board");
        }

        /// <summary>
        /// A recursive function that will try all possible values (chosen at random) at a given step and then pass the
        /// generation on to the next step, carrying forward the return.
        /// </summary>
        /// <param name="startingY">the y coordinate of the value to generate</param>
        /// <param name="startingX">the x coordinate of the value to generate</param>
        /// <returns>True if and only if the last cell was successfully generated and verified, and therefore
        /// the puzzle is done. False if at any point no more valid variants have remained for the puzzle,
        /// i.e., all possible descendants for all possible values of this point have been exhausted and
        /// none are valid.</returns>
        private bool generateSolutionWithBacktrack(int startingY, int startingX)
        {
            List<int> tried = new List<int>();
            Random myRandomNumberGen = new Random();
            while (tried.Count < this.trueDimension)
            {
                var toTry = myRandomNumberGen.Next(1, this.trueDimension + 1);
                if (!tried.Contains(toTry))
                {
                    tried.Add(toTry);
                    if (backtrackUp(startingX, startingY, toTry) && backtrackDown(startingX, startingY, toTry) && 
                        backtrackRight(startingX, startingY, toTry) && backtrackLeft(startingX, startingY, toTry) 
                        && checkValidSubArraySane(startingX, startingY, toTry))
                    {
                        this.board[startingX, startingY] = toTry;
                        //Console.WriteLine("Fit the value "+toTry.ToString()+" at coordinates "+xCoord.ToString()+":"
                        //                +yCoord.ToString());
                        if (startingY + 1 == this.trueDimension && startingX + 1 == this.trueDimension)
                        {
                            return true;
                        }
                        else
                        {
                            if (startingX + 1 == this.trueDimension)
                            {
                                if (generateSolutionWithBacktrack(startingY + 1, 0))
                                {
                                    return true;
                                }
                            }
                            else
                            {
                                if (generateSolutionWithBacktrack(startingY, startingX + 1))
                                {
                                    return true;
                                }
                            }
                        }
                    }
                }
            }
            return false;
        }
        //0 = easy, 1 = medium, 2 = hard
        /// <summary>
        /// A function that calculates the number of spaces on the board that must be blanked out (replaced with 0's)
        /// for the given difficulty of the puzzle, taking the difficulty as a parameter. It then steps through the
        /// board, using a random number generator to blank out values until it has blanked out enough.
        /// </summary>
        /// <param name="difficulty">integer code for the difficulty: 0 for easy, 1 for medium and
        /// 2 for hard.</param>
        public void makePuzzle(int difficulty)
        {
            int spacesBlanked = 0;
            int spacesToBlank = 0;
            if (difficulty == 0)
            {
                spacesToBlank = Convert.ToInt32(Math.Round(this.trueDimension * this.trueDimension * 0.40));
            }
            else if (difficulty == 1)
            {
                spacesToBlank = Convert.ToInt32(Math.Round(this.trueDimension * this.trueDimension * 0.55));
            }
            else if (difficulty == 2)
            {
                spacesToBlank = Convert.ToInt32(Math.Round(this.trueDimension * this.trueDimension * 0.75));
            }
            Random myRandomNumberGen = new Random();
            int yCoord = 0;
            int xCoord = 0;
            while (spacesBlanked < spacesToBlank)
            {
                var chance = myRandomNumberGen.Next(1, 101);
                if (difficulty == 0)
                {
                    if (chance <= 40)
                    {
                        if (this.board[xCoord, yCoord] != 0)
                        {
                            this.board[xCoord, yCoord] = 0;
                            spacesBlanked = spacesBlanked + 1;
                        }
                    }
                }
                else if (difficulty == 1)
                {
                    if (chance <= 55)
                    {
                        if (this.board[xCoord, yCoord] != 0)
                        {
                            this.board[xCoord, yCoord] = 0;
                            spacesBlanked = spacesBlanked + 1;
                        }
                    }
                }
                else if (difficulty == 2)
                {
                    if (chance <= 75)
                    {
                        if (this.board[xCoord, yCoord] != 0)
                        {
                            this.board[xCoord, yCoord] = 0;
                            spacesBlanked = spacesBlanked + 1;
                        }
                    }
                }
                xCoord = xCoord + 1;
                if (xCoord == this.trueDimension)
                {
                    xCoord = 0;
                    yCoord = yCoord + 1;
                }

                if (yCoord == this.trueDimension)
                {
                    yCoord = 0;
                }
            }
            NotifyPropertyChanged("board");
        }
        protected void NotifyPropertyChanged(string name)
        {
            PropertyChanged?.Invoke(this, new PropertyChangedEventArgs(name));
        }
        public event PropertyChangedEventHandler PropertyChanged;
    }
}