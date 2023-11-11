package com.myproject.gwt.client;

import com.google.gwt.user.client.Window;
import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.DecoratorPanel;
import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.HasVerticalAlignment;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.VerticalPanel;

import java.util.Random;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Entry point for the GWT application. Initializes the application and sets up the intro screen
 */

public class MyWebApp implements EntryPoint {
	private static final int MAX_THRESHOLD_VALUE_FOR_RANDOM_NUMBERS_ELEMENT = 31;
	private static final int MAX_NUMBERS_PER_COLUMN = 10;
	private static final int MAX_NUMBERS_SIZE = 100;
	private static final int MIN_NUMBERS_SIZE = 1;
	private static final int MAX_GENERATED_NUMBER_VALUE = 1001;
	
    private final DecoratorPanel introDecoratorPanel;
    private final VerticalPanel introPanel;
    private final VerticalPanel sideSortPanel;
    private final HorizontalPanel sortPanel;
    private final FlexTable numbersTable;
    private final TextBox inputBox;
    private final Button enterButton;
    private final Button sortButton;
    private final Button resetButton;
    private final Label questionLabel;
    private final List<Integer> numbers;
	private final Random random;
    private boolean descendingSort;

    public MyWebApp() {
        introDecoratorPanel = new DecoratorPanel();
        introPanel = new VerticalPanel();
        sideSortPanel = new VerticalPanel();
        sortPanel = new HorizontalPanel();
        numbersTable = new FlexTable();
        inputBox = new TextBox();
        enterButton = new Button("Enter");
        sortButton = new Button("Sort");
        resetButton = new Button("Reset");
        questionLabel = new Label("How many numbers to display?");
        numbers = new ArrayList<>();
		random = new Random();
        descendingSort = true;
    }
    
    /**
     * Initializes the application and sets up click handlers for buttons
     */
	    public void onModuleLoad() {
	    	initClickHandlers();
	    	showIntroScreen();
	    }
	    
	    /**
	     * Initializes click handlers for the enter, sort and reset buttons
	     */
	    private void initClickHandlers() {
	    	enterButton.addClickHandler(event -> {
				int numbersSize = Integer.parseInt(inputBox.getText());
				if (numbersSize >= MIN_NUMBERS_SIZE && numbersSize <= MAX_NUMBERS_SIZE) {
					showSortScreen(numbersSize);
				} else {
					Window.alert("Please enter a number between 1 and 100.");
				}
			});
	    	sortButton.addClickHandler(event -> sortNumbers());
	        resetButton.addClickHandler(event -> showIntroScreen());
	    }
	    
	    /**
	     * Displays the intro screen.
	     */
	    private void showIntroScreen() {
			RootPanel.get().clear();
            initIntroScreen();
            RootPanel.get().add(introDecoratorPanel);
		}
	    
	    /**
	     * Initializes the components (input box, enter button and question for user) for the intro screen
	     */
	    private void initIntroScreen() {
	    	introPanel.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_CENTER);
	    	introPanel.setVerticalAlignment(HasVerticalAlignment.ALIGN_MIDDLE);
	    	questionLabel.setStyleName("question");
	    	introPanel.add(questionLabel);
	    	inputBox.setStyleName("inputBox");
	    	introPanel.add(inputBox);
	    	enterButton.setStyleName("enterButton");
	    	introPanel.add(enterButton);
	    	introDecoratorPanel.setStyleName("centered-panel");
	    	introDecoratorPanel.setWidget(introPanel);
	    }
	    
	    /**
	     * Displays the sort screen with a specified number of random numbers
	     *
	     * @param numNumbers The number of random numbers to display.
	     */
	    private void showSortScreen(int numNumbers) {
		    RootPanel.get().clear();
    	    generateRandomNumbers(numNumbers);
    	    descendingSort = true;
            initSortScreen();
            RootPanel.get().add(sortPanel);
		}
	    
	    /**
	     * Initializes the components (numbers table, sort and reset buttons) for the sort screen
	     */
	    private void initSortScreen() {
	    	 updateNumbersTable();
	    	 sortButton.setStyleName("sortScreenButton");
	    	 sideSortPanel.add(sortButton);
	    	 resetButton.setStyleName("sortScreenButton");
	    	 sideSortPanel.add(resetButton);
	    	 sortPanel.add(numbersTable);
	    	 sortPanel.add(sideSortPanel);
	    }
	    
	    /**
	     * Generates list of random numbers based on input user's info
	     */
	    private void generateRandomNumbers(int numNumbers) {
	    	numbers.clear(); 
    	    boolean containsNumberThreshold = false;
    	    for (int i = 0; i < numNumbers; i++) {
    	        int randomNumber = random.nextInt(MAX_GENERATED_NUMBER_VALUE);
    	        numbers.add(randomNumber);
    	        if (randomNumber <= 30) {
    	        	containsNumberThreshold = true;
    	        }
    	    }
    	    if (!containsNumberThreshold) {
    	        numbers.set(random.nextInt(numNumbers), random.nextInt(MAX_THRESHOLD_VALUE_FOR_RANDOM_NUMBERS_ELEMENT));
    	    }
	    }	
	    
	    /**
	     * Changes order of numbers on the screen
	     */
	    private void updateNumbersTable() {
	        numbersTable.clear();
	        for (int i = 0; i < numbers.size(); i++) {
	            int column = i / MAX_NUMBERS_PER_COLUMN;
	            int row = i % MAX_NUMBERS_PER_COLUMN;
	            Button numberButton = new Button(numbers.get(i).toString());
	            numberButton.setStyleName("numberButton");
	            numberButton.addClickHandler(event -> {
						int clickedValue = Integer.parseInt(numberButton.getText());
						if(clickedValue == 0) {
							showIntroScreen();
						} else if (clickedValue > 0 && clickedValue <= 30) {
							showSortScreen(clickedValue);
						} else {
							Window.alert("Please select a value smaller or equal to 30.");
						}
				});
	            numbersTable.setWidget(row, column, numberButton);
	        }
	    }
	    
	    /**
	     * Sorts numbers in the numbers list
	     */
	    private void sortNumbers() {
	    	if (descendingSort) {
	            quickSortDescending(numbers, 0, numbers.size() - 1);
	        } else {
	        	quickSort(numbers, 0, numbers.size() - 1);
	        }
	    	descendingSort = !descendingSort;
	        updateNumbersTable();
	    }
	    
	    private void quickSort(List<Integer> list, int low, int high) {
	        if (low < high) {
	            int pivotIndex = partition(list, low, high);
	            quickSort(list, low, pivotIndex - 1);
	            quickSort(list, pivotIndex + 1, high);
	        }
	    }
	    
	    private int partition(List<Integer> list, int low, int high) {
	        int pivot = list.get(high);
	        int i = low - 1;
	        for (int j = low; j < high; j++) {
	            if (list.get(j) <= pivot) {
	                i++;
	                Collections.swap(list, i, j);
	            }
	        }
	        Collections.swap(list, i + 1, high);
	        return i + 1;
	    }
	    
	    private void quickSortDescending(List<Integer> list, int low, int high) {
	        if (low < high) {
	            int pivotIndex = partitionDescending(list, low, high);
	            quickSortDescending(list, low, pivotIndex - 1);
	            quickSortDescending(list, pivotIndex + 1, high);
	        }
	    }
	    
	    private int partitionDescending(List<Integer> list, int low, int high) {
	        int pivot = list.get(high);
	        int i = low - 1;
	        for (int j = low; j < high; j++) {
	            if (list.get(j) >= pivot) {
	                i++;
	                Collections.swap(list, i, j);
	            }
	        }
	        Collections.swap(list, i + 1, high);
	        return i + 1;
	    }
}
