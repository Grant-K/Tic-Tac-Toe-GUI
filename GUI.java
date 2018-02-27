// ============================================================================
//     Taken From: http://programmingnotes.org/
// ============================================================================
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

public class GUI extends JFrame implements ActionListener
{
    // setting up ALL the variables
    JFrame window = new JFrame("Grant's Tic Tac Toe Game");

    JMenuBar mnuMain = new JMenuBar();
    JMenuItem   mnuNewGame = new JMenuItem("  New Game"), 
    mnuBoardSize = new JMenuItem("  Board Size"),
    mnuGameTitle = new JMenuItem("|Tic Tac Toe|  "),
    mnuStartingPlayer = new JMenuItem(" Starting Player"),
    mnuExit = new JMenuItem("    Quit");
    int boardSize = 3;

    JButton btnEmpty[] = new JButton[10];// Edit number to change board size

    JPanel  pnlNewGame = new JPanel(),
    pnlNorth = new JPanel(),
    pnlSouth = new JPanel(),
    pnlTop = new JPanel(),
    pnlBottom = new JPanel(),
    pnlPlayingField = new JPanel();
    JPanel radioPanel = new JPanel();

    private JRadioButton SelectX = new JRadioButton("User Plays $", false);
    private  JRadioButton SelectO = new JRadioButton("User Plays &", false);
    private ButtonGroup radioGroup;
    private  String startingPlayer= "";
    final int X = 1000, Y = 680, colorR = 100, colorG = 100, colorB = 100; // size of the game window
    private boolean inGame = false;
    private boolean win = false;
    private boolean btnEmptyClicked = false;
    private boolean setTableEnabled = false;
    private String message;
    private Font font = new Font("Rufscript", Font.BOLD, 100);
    private int remainingMoves = 1;

    //===============================  GUI  ========================================//
    public GUI() //This is the constructor
    {
        //Setting window properties:
        window.setSize(X, Y);
        window.setLocation(300, 180);
        window.setResizable(true);
        window.setLayout(new BorderLayout());
        window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);  

        //------------  Sets up Panels and text fields  ------------------------//
        // setting Panel layouts and properties
        pnlNorth.setLayout(new FlowLayout(FlowLayout.CENTER));
        pnlSouth.setLayout(new FlowLayout(FlowLayout.CENTER));

        pnlNorth.setBackground(new Color(40, 40, 40));
        pnlSouth.setBackground(new Color(colorR, colorG, colorB));

        pnlTop.setBackground(new Color(colorR, colorG, colorB));
        pnlBottom.setBackground(new Color(colorR, colorG, colorB));

        pnlTop.setLayout(new FlowLayout(FlowLayout.CENTER));
        pnlBottom.setLayout(new FlowLayout(FlowLayout.CENTER));

        radioPanel.setBackground(new Color(colorR, colorG, colorB));
        pnlBottom.setBackground(new Color(colorR, colorG, colorB));
        radioPanel.setBorder(BorderFactory.createTitledBorder(
                BorderFactory.createEtchedBorder(), "Who Goes First?"));

        // adding menu items to menu bar
        mnuMain.add(mnuGameTitle);
        mnuGameTitle.setEnabled(false);
        mnuGameTitle.setFont(new Font("Purisa",Font.BOLD,18));
        mnuMain.add(mnuNewGame);
        mnuNewGame.setFont(new Font("Purisa",Font.BOLD,18));
        mnuMain.add(mnuBoardSize);
        mnuBoardSize.setFont(new Font("Purisa",Font.BOLD,18));
        mnuMain.add(mnuStartingPlayer);
        mnuStartingPlayer.setFont(new Font("Purisa",Font.BOLD,18));
        mnuMain.add(mnuExit);
        mnuExit.setFont(new Font("Purisa",Font.BOLD,18));//---->Menu Bar Complete

        // adding X & O options to menu
        SelectX.setFont(new Font("Purisa",Font.BOLD,18));
        SelectO.setFont(new Font("Purisa",Font.BOLD,18));
        radioGroup = new ButtonGroup(); // create ButtonGroup
        radioGroup.add(SelectX); // add plain to group
        radioGroup.add(SelectO);
        radioPanel.add(SelectX);
        radioPanel.add(SelectO);

        // adding Action Listener to all the Buttons and Menu Items
        mnuNewGame.addActionListener(this);
        mnuExit.addActionListener(this);
        mnuBoardSize.addActionListener(this);
        mnuStartingPlayer.addActionListener(this);

        // setting up the playing field
        pnlPlayingField.setLayout(new GridLayout(3, 3, 2, 2));// Edit first two numbers to change board size
        pnlPlayingField.setBackground(Color.black);
        for(int x=1; x <= 9; ++x)   // Edit the x <= number to change board size
        {
            btnEmpty[x] = new JButton();
            btnEmpty[x].setBackground(new Color(colorR, colorG, colorB));
            btnEmpty[x].addActionListener(this);
            pnlPlayingField.add(btnEmpty[x]);
            btnEmpty[x].setEnabled(setTableEnabled);
        }

        // adding everything needed to pnlNorth and pnlSouth
        pnlNorth.add(mnuMain);
        BusinessLogic.ShowGame(pnlSouth,pnlPlayingField);

        // adding to window and Showing window
        window.add(pnlNorth, BorderLayout.NORTH);
        window.add(pnlSouth, BorderLayout.CENTER);
        window.setVisible(true);
    }// End GUI

    // ===========  Start Action Performed  ===============//
    public void actionPerformed(ActionEvent click)  
    {
        // get the mouse click from the user
        Object source = click.getSource();

        // check if a button was clicked on the gameboard
        for(int currentMove=1; currentMove <= (boardSize*boardSize); ++currentMove) // Edit the currentMove <= number to change board size
        {
            if(source == btnEmpty[currentMove] && remainingMoves < (boardSize*boardSize+1)) // Edit the currentMove <= number to change board size
            {
                btnEmptyClicked = true;
                BusinessLogic.GetMove(currentMove, remainingMoves, font, 
                    btnEmpty, startingPlayer);              
                btnEmpty[currentMove].setEnabled(false);
                pnlPlayingField.requestFocus();
                ++remainingMoves;
            }
        }

        // if a button was clicked on the gameboard, check for a winner
        if(btnEmptyClicked) 
        {
            inGame = true;
            CheckWin();
            btnEmptyClicked = false;
            if(win == true)
            {
                inGame = false;
                startingPlayer = "";
                radioGroup.clearSelection();
                setTableEnabled = false;
                RedrawGameBoard();
                win = false;
            }
        }

        // check if the user clicks on a menu item
        if(source == mnuNewGame)    
        {
            //System.out.println(startingPlayer);
            BusinessLogic.ClearPanelSouth(pnlSouth,pnlTop,pnlNewGame,
                pnlPlayingField,pnlBottom,radioPanel);
            if(startingPlayer.equals(""))
            {
                JOptionPane.showMessageDialog(null, "Please Select a Starting Player", 
                    "Oops..", JOptionPane.ERROR_MESSAGE);
                BusinessLogic.ShowGame(pnlSouth,pnlPlayingField);
            }
            else
            {
                if(inGame)  
                {
                    int option = JOptionPane.showConfirmDialog(null, "If you start a new game," +
                            " your current game will be lost..." + "\n" +"Are you sure you want to continue?"
                        , "New Game?" ,JOptionPane.YES_NO_OPTION);
                    if(option == JOptionPane.YES_OPTION)    
                    {
                        inGame = false;
                        startingPlayer = "";
                        radioGroup.clearSelection();
                        setTableEnabled = false;
                    }
                    else
                    {
                        BusinessLogic.ShowGame(pnlSouth,pnlPlayingField);
                    }
                }
                // redraw the gameboard to its initial state
                if(!inGame) 
                {
                    RedrawGameBoard();
                }
            }       
        }       
        // exit button
        else if(source == mnuExit)  
        {
            int option = JOptionPane.showConfirmDialog(null, "Are you sure you want to quit?", 
                    "Quit" ,JOptionPane.YES_NO_OPTION);
            if(option == JOptionPane.YES_OPTION)
            {
                System.exit(0);
            }
        }
        // Change Board Size
        else if(source == mnuBoardSize)
        {   
            if(inGame)  
            {
                JOptionPane.showMessageDialog(null, "Cannot select a new Board "+
                    "Size at this time.\nFinish the current game, or select a New Game "+
                    "to continue", "Game In Session..", JOptionPane.INFORMATION_MESSAGE);
                BusinessLogic.ShowGame(pnlSouth,pnlPlayingField);
            }
            else
            {
                boolean badInput = true;
                while(badInput)
                {
                    badInput = false;
                    String boardSizeRaw = JOptionPane.showInputDialog(null, "Please input the desired size of the board (Must be more than 1). Ex. 3 would make a 3x3 board.", "Choose Board Size", JOptionPane.QUESTION_MESSAGE);
                    try {
                        boardSize = Integer.parseInt(boardSizeRaw);
                    } catch (NumberFormatException e) {
                        JOptionPane.showMessageDialog(null, "Board Size must contain only numbers and be more than 1! Please try again!", "Invalid Input", JOptionPane.ERROR_MESSAGE);
                        badInput = true;
                    }
                    if(badInput == false && boardSize < 3)
                        {
                            JOptionPane.showMessageDialog(null, "Board Size must contain only numbers and be more than 1! Please try again!", "Invalid Input", JOptionPane.ERROR_MESSAGE);
                            badInput = true;
                        }
                }
                JButton btnEmpty[] = new JButton[((boardSize*boardSize)+1)];
                changePlayingBoard();
            }
        }
        // select X or O player 
        else if(source == mnuStartingPlayer)  
        {
            if(inGame)  
            {
                JOptionPane.showMessageDialog(null, "Cannot select a new Starting "+
                    "Player at this time.\nFinish the current game, or select a New Game "+
                    "to continue", "Game In Session..", JOptionPane.INFORMATION_MESSAGE);
                BusinessLogic.ShowGame(pnlSouth,pnlPlayingField);
            }
            else
            {
                setTableEnabled = true;
                BusinessLogic.ClearPanelSouth(pnlSouth,pnlTop,pnlNewGame,
                    pnlPlayingField,pnlBottom,radioPanel);

                SelectX.addActionListener(new RadioListener());
                SelectO.addActionListener(new RadioListener());
                radioPanel.setLayout(new GridLayout(2,1));

                radioPanel.add(SelectX);
                radioPanel.add(SelectO);
                pnlSouth.setLayout(new GridLayout(2, 1, 2, 1));
                pnlSouth.add(radioPanel);
                pnlSouth.add(pnlBottom);
            }
        }
        pnlSouth.setVisible(false); 
        pnlSouth.setVisible(true);  
    }// End Action Performed

    // ===========  Start RadioListener  ===============//  
    private class RadioListener implements ActionListener 
    {
        public void actionPerformed(ActionEvent event) 
        {
            JRadioButton theButton = (JRadioButton)event.getSource();
            if(theButton.getText().equals("User Plays $")) 
            {
                startingPlayer = "$";
            }
            if(theButton.getText().equals("User Plays &"))
            {
                startingPlayer = "&";
            }

            // redisplay the gameboard to the screen
            pnlSouth.setVisible(false); 
            pnlSouth.setVisible(true);          
            RedrawGameBoard();
        }
    }// End RadioListener
    /*
    ----------------------------------
    Start of all the other methods. |
    ----------------------------------
     */
    private void RedrawGameBoard()  
    {
        BusinessLogic.ClearPanelSouth(pnlSouth,pnlTop,pnlNewGame,
            pnlPlayingField,pnlBottom,radioPanel);
        BusinessLogic.ShowGame(pnlSouth,pnlPlayingField);       

        remainingMoves = 1;

        for(int x=1; x <= (boardSize*boardSize); ++x) // Edit the x <= number to change board size
        {
            btnEmpty[x].setText("");
            btnEmpty[x].setEnabled(setTableEnabled);
        }

    }

    private void CheckWin() 
    {   
        int currentlyConnected = 0;
        int trueBoardSize = (boardSize*boardSize);
        for(int x = 1; x <= trueBoardSize; x ++) // Edit the x <= number to change board size
        {
            if(btnEmpty[x].getText().equals("$") || btnEmpty[x].getText().equals("&"))
            {
                for(int c = 1; c < boardSize; c++)
                {
                    if(x+c <= trueBoardSize)
                    {
                        if((btnEmpty[x].getText()).equals((btnEmpty[x+c].getText())))
                        {
                            currentlyConnected++;
                        }
                    }
                }
                if(currentlyConnected == (boardSize-1))
                {
                    JOptionPane.showMessageDialog(null, btnEmpty[x].getText() + "'s win!", "Game Won", JOptionPane.INFORMATION_MESSAGE);
                    win = true;
                }
                currentlyConnected = 0;
                for(int c = 1; c < boardSize; c++)
                {
                    if(x+(c*boardSize) <= trueBoardSize)
                    {
                        if((btnEmpty[x].getText()).equals((btnEmpty[x+(c*boardSize)].getText())))
                        {
                            currentlyConnected++;
                        }
                    }
                }
                if(currentlyConnected == (boardSize-1))
                {
                    JOptionPane.showMessageDialog(null, btnEmpty[x].getText() + "'s win!", "Game Won", JOptionPane.INFORMATION_MESSAGE);
                    win = true;
                }
                currentlyConnected = 0;
                for(int c = 1; c < boardSize; c++)
                {
                    if(x+(c*boardSize)+c <= trueBoardSize)
                    {
                        if((btnEmpty[x].getText()).equals((btnEmpty[x+(c*boardSize)+c].getText())))
                        {
                            currentlyConnected++;
                        }
                    }
                }
                if(currentlyConnected == boardSize-1)
                {
                    JOptionPane.showMessageDialog(null, btnEmpty[x].getText() + "'s win!", "Game Won", JOptionPane.INFORMATION_MESSAGE);
                    win = true;
                }
                currentlyConnected = 0;
                for(int c = 1; c < boardSize; c++)
                {
                    if(x+(c*boardSize)-c >= 1 && x+(c*boardSize)-c <= trueBoardSize)
                    {
                        if((btnEmpty[x].getText()).equals((btnEmpty[x+(c*boardSize)-c].getText())))
                        {
                            currentlyConnected++;
                        }
                    }
                }
                if(currentlyConnected == (boardSize-1))
                {
                    JOptionPane.showMessageDialog(null, btnEmpty[x].getText() + "'s win!", "Game Won", JOptionPane.INFORMATION_MESSAGE);
                    win = true;
                }
                currentlyConnected = 0;
            }
        }
    }
    
    private void changePlayingBoard()
    {
        pnlPlayingField.setLayout(new GridLayout(boardSize, boardSize, 2, 2));// Edit first two numbers to change board size
        pnlPlayingField.setBackground(Color.black);
        for(int x=1; x <= (boardSize*boardSize); ++x)   // Edit the x <= number to change board size
        {
            btnEmpty[x] = new JButton();
            btnEmpty[x].setBackground(new Color(colorR, colorG, colorB));
            btnEmpty[x].addActionListener(this);
            pnlPlayingField.add(btnEmpty[x]);
            btnEmpty[x].setEnabled(setTableEnabled);
        }
    }
}	