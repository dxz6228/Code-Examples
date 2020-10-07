using System;
using System.Collections.Generic;
using System.IO;
using System.Linq;
using System.Text;
using System.Threading.Tasks;
using System.Windows;
using System.Windows.Controls;
using System.Windows.Data;
using System.Windows.Documents;
using System.Windows.Input;
using System.Windows.Media;
using System.Windows.Media.Imaging;
using System.Windows.Navigation;
using System.Windows.Shapes;
using Microsoft.Win32;
using Project_3.ViewModel;
using WPFCustomMessageBox;

namespace Project_3
{
    ///
    /// Author: Denis Zhenilov
    /// Email: dxz6228@rit.edu
    /// CSCI-541 Project 3 - View
    /// <summary>
    /// Implements interaction logic for MainWindow.xaml
    /// Primarily deals with handling the various button clicks and handing them off to functions in the ViewModel.
    /// </summary>
    public partial class MainWindow : Window
    {
        public MainViewModel myViewModel = new MainViewModel();
        public MainWindow()
        {
            this.DataContext = myViewModel;
            InitializeComponent();
        }

        /// <summary>
        /// Calls the savePuzzleToFile, saving the initial unsolved puzzle to a file called Puzzle.txt
        /// </summary>
        /// <param name="sender"></param>
        /// <param name="e"></param>
        private void save_puzzle_Click(object sender, RoutedEventArgs e)
        {
            MainViewModel myViewModel = (MainViewModel) this.DataContext;
            myViewModel.savePuzzleToFile();
        }
        /// <summary>
        /// Calls the validatePuzzle function in the ViewModel, and displays a messagebox to the user, informing them
        /// if there are any errors in the puzzle. Whilst the messagebox is on screen, cells with errors will be
        /// highlighted in red. When the user clicks OK, highlighting will disappear.
        /// </summary>
        /// <param name="sender"></param>
        /// <param name="e"></param>
        private void validate_puzzle_Click(object sender, RoutedEventArgs e)
        {
            MainViewModel myViewModel = (MainViewModel) this.DataContext;
            MessageBoxResult readyToReturn = MessageBox.Show(myViewModel.validatePuzzle(), "Puzzle validataion", MessageBoxButton.OK, MessageBoxImage.Information);
            myViewModel.resetValidationArray();
        }
        /// <summary>
        /// This function is a bit more involved. It is called by Show buttons on unknown cells. It uses the way names
        /// of each of the cell are encoded to parse cell's X and Y coordinates. It will then call revealAndHideButton
        /// function in the model with those coordiantes as arguments, to reveal the true value of the cell, set it
        /// in the puzzle, and to hide the button.
        /// </summary>
        /// <param name="sender"></param>
        /// <param name="e"></param>
        private void on_click_Show(object sender, RoutedEventArgs e)
        {
            System.Windows.Controls.Button pressedButton = (System.Windows.Controls.Button) sender;
            string[] buttonCoords = pressedButton.Name.Split("_");
            int xCoord = Convert.ToInt32(buttonCoords[2]);
            int yCoord = Convert.ToInt32(buttonCoords[1]);
            MainViewModel myViewModel = (MainViewModel) this.DataContext;
            myViewModel.revealAndHideButton(xCoord, yCoord);
        }
        /// <summary>
        /// This function toggles the Reveal mode in the ViewModel, changing whether or not SHow buttons are seen on
        /// unknown cells. It is called by the Reveal button.
        /// </summary>
        /// <param name="sender"></param>
        /// <param name="e"></param>
        private void on_click_Reveal_Toggle(object sender, RoutedEventArgs e)
        {
            MainViewModel myViewModel = (MainViewModel) this.DataContext;
            myViewModel.revealToggle();
        }
        /// <summary>
        /// This function produces a custom messagebox giving the user an option to create a new easy, medium or
        /// hard puzzle. It is called by the New Puzzle button.
        /// </summary>
        /// <param name="sender"></param>
        /// <param name="e"></param>
        private void new_puzzle_Click(object sender, RoutedEventArgs e)
        {
            MessageBoxResult difficulty_choice = CustomMessageBox.ShowYesNoCancel("What puzzle difficulty would you like?", "Choose puzzle difficulty",
                "Easy", "Medium", "Hard", MessageBoxImage.Question);
            MainViewModel myViewModel = (MainViewModel) this.DataContext;
            switch (difficulty_choice)
            {
                case MessageBoxResult.Yes:
                    myViewModel.createEasyPuzzle();
                    break;
                case MessageBoxResult.No:
                    myViewModel.createMediumPuzzle();
                    break;
                case MessageBoxResult.Cancel:
                    myViewModel.createHardPuzzle();
                    break;
            }
        }
    }
}