package com.myproject.gwt.client;

import com.google.gwt.user.client.Window;
import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.ui.DecoratorPanel;
import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.HasVerticalAlignment;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.VerticalPanel;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class MyWebApp implements EntryPoint {
	
	private DecoratorPanel introDecoratorPanel;
    private VerticalPanel introPanel;
    private VerticalPanel sideSortPanel;
    private HorizontalPanel sortPanel;
    private FlexTable numbersTable;
    private TextBox inputBox;
    private Button enterButton;
    private Button sortButton;
    private Button resetButton;
    private Label questionLabel;
    private List<Integer> numbers;
    private int maxNumbersPerColumn;
    private int maxNumbersSize;
    private int maxGeneratedNumberValue;
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
        maxNumbersPerColumn = 10;
        maxNumbersSize = 100;
        maxGeneratedNumberValue = 1001;
        descendingSort = true;
    }
    
	    public void onModuleLoad() {
	    	initClickHandlers();
	    	showIntroScreen();
	    }
	    
	    private void initClickHandlers() {
	    	enterButton.addClickHandler(new ClickHandler() {
	            public void onClick(ClickEvent event) {
	                int numbersSize = Integer.parseInt(inputBox.getText());
	                if (numbersSize > 0 && numbersSize <= maxNumbersSize) {
	                	showSortScreen(numbersSize);
	                } else {
	                    Window.alert("Please enter a number between 1 and 100.");
	                }
	            }
	        });
	    	sortButton.addClickHandler(new ClickHandler() {
	            public void onClick(ClickEvent event) {
	                sortNumbers();
	            }
	        });
	        resetButton.addClickHandler(new ClickHandler() {
	            public void onClick(ClickEvent event) {
	            	showIntroScreen();
	            }
	        });
	    }
	    
	    private void showIntroScreen() {
			RootPanel.get().clear();
            initIntroScreen();
            RootPanel.get().add(introDecoratorPanel);
		}
	    
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
	    
	    private void showSortScreen(int numNumbers) {
		    RootPanel.get().clear();
    	    generateRandomNumbers(numNumbers);
    	    descendingSort = true;
            initSortScreen();
            RootPanel.get().add(sortPanel);
		}
	    
	    private void initSortScreen() {
	    	 updateNumbersTable();
	    	 sortButton.setStyleName("sortScreenButton");
	    	 sideSortPanel.add(sortButton);
	    	 resetButton.setStyleName("sortScreenButton");
	    	 sideSortPanel.add(resetButton);
	    	 sortPanel.add(numbersTable);
	    	 sortPanel.add(sideSortPanel);
	    }
	    
	    private void generateRandomNumbers(int numNumbers) {
	    	numbers.clear(); 
    	    boolean containsNumberThreshold = false;
    	    for (int i = 0; i < numNumbers; i++) {
    	        int randomNumber = (int) (Math.random() * maxGeneratedNumberValue); 
    	        numbers.add(randomNumber);
    	        if (randomNumber <= 30) {
    	        	containsNumberThreshold = true;
    	        }
    	    }
    	    if (!containsNumberThreshold) {
    	        numbers.set((int) (Math.random() * numNumbers), (int) (Math.random() * 31));
    	    }
	    }	
	    
	    private void updateNumbersTable() {
	        numbersTable.clear();
	        for (int i = 0; i < numbers.size(); i++) {
	            int column = i / maxNumbersPerColumn;
	            int row = i % maxNumbersPerColumn;
	            Button numberButton = new Button(numbers.get(i).toString());
	            numberButton.setStyleName("numberButton");
	            numberButton.addClickHandler(new ClickHandler() {
		            public void onClick(ClickEvent event) {
		                    int clickedValue = Integer.parseInt(numberButton.getText());
		                    if(clickedValue == 0) {
		                    	showIntroScreen();
		                    } else if (clickedValue > 0 && clickedValue <= 30) {
		                    	showSortScreen(clickedValue);
		                    } else {
		                        Window.alert("Please select a value smaller or equal to 30.");
		                    }
		            }
		        });
	            numbersTable.setWidget(row, column, numberButton);
	        }
	    }
	    
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
