package org.headroyce.lross.tictactoe;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.*;

/**
 * JavaFX View of TicTacToe
 */
public class TicTacToeGUI extends BorderPane {

    // The brains of Tic Tac Toe
    private TicTacToeLogic logic;

    // Graphical elements of the GUI
    private Button[] board;
    private Label turn;

    /******************
     *
     * The buttons are laid out from top to bottom, left to right
     *
     * -------------
     * | 0 | 1 | 2 |
     * -------------
     * | 3 | 4 | 5 |
     * -------------
     * | 6 | 7 | 8 |
     * -------------
     */


    public TicTacToeGUI() {
        logic = new TicTacToeLogic();

        int numRows = logic.numRows();
        board = new Button[numRows * numRows];
        GridPane boardGUI = layoutBoard();
        HBox status = layoutStatus();

        this.setTop(status);
        this.setCenter(boardGUI);
    }

    /**
     * Graphically arranges the status bar
     *
     * @return the status bar
     */
    private HBox layoutStatus() {
        HBox rtn = new HBox();

        turn = new Label("X's Turn");
        turn.setPadding(new Insets(10, 10, 10, 10));
        rtn.getChildren().add(turn);

        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);
        rtn.getChildren().add(spacer);

        Button reset = new Button("Reset");
        reset.setPadding(new Insets(10, 10, 10, 10));
        rtn.getChildren().add(reset);
        reset.setOnAction(new ResetButton());

        return rtn;
    }

    /**
     * Graphically arranges the playing grid
     *
     * @return the graphical Tic Tac Toe board
     */
    private GridPane layoutBoard() {
        /* Setup the board layout */
        GridPane boardGUI = new GridPane();

        int numRows = logic.numRows();

        // Add row and column constraining values
        for (int i = 0; i < numRows; i++) {
            RowConstraints row = new RowConstraints(50, 100, Double.MAX_VALUE);
            row.setVgrow(Priority.ALWAYS);

            ColumnConstraints col = new ColumnConstraints(50, 100, Double.MAX_VALUE);
            col.setHgrow(Priority.ALWAYS);

            boardGUI.getRowConstraints().add(row);
            boardGUI.getColumnConstraints().add(col);
        }

        // Add the buttons
        for (int row = 0; row < numRows; row++) {
            for (int col = 0; col < numRows; col++) {
                int spot = row * numRows + col;

                board[spot] = new Button();
                board[spot].setMaxSize(Double.MAX_VALUE, Double.MAX_VALUE);
                GridPane.setFillHeight(board[spot], true);
                GridPane.setFillWidth(board[spot], true);

                // Connect the Button to its Controller
                board[spot].setOnAction(new ButtonHandler());
                boardGUI.add(board[spot], col, row);
            }
        }
        return boardGUI;
    }

    /**
     * Syncs the View with the Model
     */
    public void updateGraphics() {

        for (int i = 0; i < board.length; i++) {
            String who = logic.getPlayer(i);
            board[i].setText(who);
        }

        String turn = logic.whoseTurn();
        TicTacToeGUI.this.turn.setText(turn + "'s Turn");

        if (logic.checkWinner() == "X" || (logic.checkWinner() == "O")) {
            TicTacToeGUI.this.turn.setText("Winner: " + logic.checkWinner());
        }
        else if (logic.checkWinner() == "TIE") {
            TicTacToeGUI.this.turn.setText("Winner: " + logic.checkWinner());
        }

        //TODO: Replace the console output from the Controller with graphics AFTER your logic works
    }

    /**
     * Controller -- handles interaction between the View and the Model
     */
    private class ButtonHandler implements EventHandler<ActionEvent> {
        public void handle(ActionEvent e) {
            Button source = (Button) e.getSource();

            // Figure out which button got pushed
            for (int i = 0; i < board.length; i++) {
                if (source == board[i]) {
                    // Button i was pressed

                    // Make a move and check for a winner
                    logic.makeMove(i);
                    String winner = logic.checkWinner();



                    // Cause the View to update later so that we don't freeze the graphics
                    Platform.runLater(new Runnable() {
                        public void run() {
                            updateGraphics();
                        }
                    });
                    break;
                }
            }
        }


    }
    /**
     * Reset button handler --- owo
     */
    private class ResetButton implements EventHandler<ActionEvent> {
        public void handle(ActionEvent e) {
            Button source = (Button) e.getSource();
            // calls other method
            logic.reset();

            // Cause the View to update later so that we don't freeze the graphics
            Platform.runLater(new Runnable() {
                public void run() {
                    updateGraphics();
                }

            });
        }
    }

}
