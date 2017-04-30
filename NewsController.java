import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.util.LinkedHashMap;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFileChooser;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;

/**
 * Project 4, CS 2334, Section 010, April 23, 2017
 * <P>
 * This class provides the controller for the MVC part of project 4. This involves
 * having methods on saving, loading, importing, editing, deleting, etc. As well as having
 * inner classes for listener objects, that will decide what actions need to be performed.
 * </P>
 * <P>
 * Note that the this class contains: fields for the model, view, and other dialog/list fields.
 * </P>
 * 
 * @author Dean Hougen, Jered Little, Vishnupriya Parasaram, Jessica Horner, and Zakary Koskovich 
 * @version 1.0
 * 
 */
//Jered Little created the stub code for this class.
public class NewsController {

	
	private NewsDataBaseModel newsDataBaseModel;
	private SelectionView selectionView;
	private EditNewsMakerView editNewsMakerView;
	private JDialog viewDialog;
	private AddEditNewsStoryView addEditNewsStoryView;
	private NewsStory editedNewsStory;
	private MediaTypeSelectionView mediaTypeSelectionView;
	private List<NewsMedia> selectedMediaTypes;
	
	/**
	 * <P>
	 *TODO
	 * This is the constructor for the news controller. I have no idea what it does.
	 * </P>
	 */
	public NewsController() {
		//TODO delete this. This will be in nooz main file
		newsDataBaseModel  = new NewsDataBaseModel();
		selectionView = new SelectionView();
		setSelectionView(this.selectionView);
		
	}
	/**
	 * <P>
	 * This method sets the news data base model field.
	 * </P>
	 * @param newsDataBaseModel The news data base model to set.
	 */
	public void setNewsDataBaseModel(NewsDataBaseModel newsDataBaseModel) {
		this.newsDataBaseModel = newsDataBaseModel;
		System.out.println("Success! NewsDataBaseModel has been set! Good work!");
	}
	/**
	 * <P>
	 * This method sets the selection view.
	 * </P>
	 * @param selectionView The selection view to set.
	 */
	public void setSelectionView(SelectionView selectionView) {
		selectionView.registerNewsStoryMenuListener(new NewsStoryMenuListener());
		selectionView.registerNewsMakerMenuListener(new NewsMakerMenuListener());
		selectionView.registerFileMenuListener(new FileMenuListener());
		selectionView.registerDisplayMenuListener(new DisplayMenuListener());
		
		
	}
	/**
	 * <P>
	 * This method loads all the news data in BINARY FORMAT.
	 * </P>
	 * @throws IOException 
	 * @throws ClassNotFoundException 
	 */
	public void loadNewsData() throws ClassNotFoundException, IOException {
		JFileChooser fc = new JFileChooser();
		fc.setDialogTitle("Open Binary Data File");
		int returnValue = fc.showOpenDialog(selectionView);
		if(returnValue == JFileChooser.APPROVE_OPTION) {
			System.out.println("Load Binary Data File");
			//TODO load data in
			String outputFileName = fc.getSelectedFile().getPath();
			FileInputStream fileInputStream = new FileInputStream(outputFileName);
			ObjectInputStream objectInputStream = new ObjectInputStream(fileInputStream);
			newsDataBaseModel = (NewsDataBaseModel) objectInputStream.readObject();
			objectInputStream.close();
			
		}
	}
	/**
	 * <P>
	 * This method saves all the news data.
	 * </P> 
	 */
	private void saveNewsData() {
		//No data is in the model, so we cant save.
		if(newsDataBaseModel.newsMakerListIsEmpty() && newsDataBaseModel.newsStoryListIsEmpty()) {
			return;
		}
		JFileChooser fc = new JFileChooser();
		fc.setDialogTitle("Save Binary Data File");
		int returnValue = fc.showSaveDialog(selectionView);
		if(returnValue == JFileChooser.APPROVE_OPTION) {
			System.out.println("Save Binary File");
		}
	}
	/**
	 * <P>
	 * This method imports news stories.
	 * </P>
	 */
	private void importNoozStories() {
		
		//file chooser
		JFileChooser fc = new JFileChooser();
		fc.setDialogTitle("Open Text File(s)");

		//multiple files selection DISABLED. This causes problems with the for loop
		fc.setMultiSelectionEnabled(false);
		int returnValue = JFileChooser.APPROVE_OPTION;
		String newsStoryFilePath = "";
		String answer = "";
		String path = "";
		while(returnValue == JFileChooser.APPROVE_OPTION) {
			//input dialog question
			returnValue = fc.showOpenDialog(selectionView);
			
			if(returnValue == JFileChooser.CANCEL_OPTION) {
				break;
			}
			//ask for what type of file
			answer = JOptionPane.showInputDialog(fc,"Does this file contain source codes, topic codes, subject codes, or news stories?",
					fc.getSelectedFile().getName(),JOptionPane.WARNING_MESSAGE);
			
			//create the maps based on user answer
			if(answer.equalsIgnoreCase("source codes")) {
				try {
					path = fc.getSelectedFile().getPath();
					newsDataBaseModel.setNewsSourceMap(CodeFileProcessor.readCodeFile(path));
				} catch (IOException e) {
					e.printStackTrace();
				}

			}
			if(answer.equalsIgnoreCase("topic codes")) {

				try {
					path = fc.getSelectedFile().getPath();
					newsDataBaseModel.setNewsTopicMap(CodeFileProcessor.readCodeFile(path));
				} catch (IOException e) {
					e.printStackTrace();
				}

			}
			if(answer.equalsIgnoreCase("subject codes")) {

				try {
					path = fc.getSelectedFile().getPath();
					newsDataBaseModel.setNewsSubjectMap(CodeFileProcessor.readCodeFile(path));
				} catch (IOException e) {
					e.printStackTrace();
				}

			}
			if(answer.equalsIgnoreCase("news stories")) {
					newsStoryFilePath = fc.getSelectedFile().getPath();
			}
			while(!answer.equals("topic codes") && !answer.equalsIgnoreCase("subject codes") && !answer.equalsIgnoreCase("news stories") &&
					!answer.equalsIgnoreCase("source codes")) {

				answer = JOptionPane.showInputDialog(selectionView,"Not a valid answer. "
						+ "Does this file contain source codes, topic codes, subject codes, or news stories?",fc.getSelectedFile().getName(),
							JOptionPane.WARNING_MESSAGE);
				if(answer.equalsIgnoreCase("source codes")) {

					try {
						newsDataBaseModel.setNewsSourceMap(CodeFileProcessor.readCodeFile(fc.getSelectedFile().getPath()));
					} catch (IOException e) {
						e.printStackTrace();
					}

				}
				if(answer.equalsIgnoreCase("topic codes")) {

					try {
						newsDataBaseModel.setNewsTopicMap(CodeFileProcessor.readCodeFile(fc.getSelectedFile().getPath()));
					} catch (IOException e) {

						e.printStackTrace();
					}

				}
				if(answer.equalsIgnoreCase("subject codes")) {

					try {
						newsDataBaseModel.setNewsSubjectMap(CodeFileProcessor.readCodeFile(fc.getSelectedFile().getPath()));
					} catch (IOException e) {
						e.printStackTrace();
					}

				}
			}//end while incorrect input

		} // end while answer is approve
		try {
			 setNewsDataBaseModel(NoozFileProcessor.readNoozFile(newsStoryFilePath, 
					newsDataBaseModel.getNewsSourceMap(), newsDataBaseModel.getNewsTopicMap(), newsDataBaseModel.getNewsSubjectMap()));
		} catch (IOException e) {
			e.printStackTrace();
		}
		
	}
	
	/**
	 * <P>
	 * This method exports news stories.
	 * </P>
	 */
	private void exportNewsStories() {
		//No data is in the model, so we cant save.
		if(newsDataBaseModel == null) {
			return;
		}
		JFileChooser fc = new JFileChooser();
		fc.setDialogTitle("Save Text File");
		int returnValue = fc.showSaveDialog(selectionView);
		if(returnValue == JFileChooser.APPROVE_OPTION) {
			System.out.println("Save Text File");
			//TODO save data
			fc.getSelectedFile();
		}
	}
	/**
	 * <P>
	 * This method allow you to add a news maker to the list.
	 * </P>
	 */
	private void addNewsMaker() {
		String input = "";
		
		input = JOptionPane.showInputDialog(selectionView,"Enter News Maker Name: ",
			"Add News Maker",JOptionPane.INFORMATION_MESSAGE);
		

		//if user didnt click cancel
		if(!input.equals("")) {
			//if the data base has the newsmaker already
			if(newsDataBaseModel.containsNewsMakerModel(new NewsMakerModel(input))) {
				//ask to replace
				int replaceNewsMaker = JOptionPane.showConfirmDialog(selectionView,"Replace News Maker: " + input,
						"Replace News Maker",JOptionPane.YES_NO_CANCEL_OPTION);
				//if they pressed yes
				if(replaceNewsMaker == JOptionPane.YES_OPTION) {
					//replace
					//TODO replace
					System.out.println("Testing: We will now REPLACE the newsmakermodel.... Uncomment line below this when the newsdatabasemodel is finished.");
					//newsDataBaseModel.replaceNewsMakerModel(new NewsMakerModel(input));
				}
			}
			//otherwise, if the database doesnt contain the news maker
			else {
				//add him
				//TODO add (uncomment line below)
				System.out.println("Testing: We will now add the newsmakermodel.... Uncomment line below this when the newsdatabasemodel is finished.");
				//newsDataBaseModel.addNewsMakerModel(new NewsMakerModel(input));
			}	
		} // end if
		
		
	}
	/**
	 * <P>
	 * This method allows you to edit the news maker.
	 * </P>
	 * 
	 */
	private void editNewsMakers() {
		//create the edit news maker view based on the first news maker selected TODO, when you get the selection view loaded with data, use this line and
		//delete the line at the bottom
		//editNewsMakerView = new EditNewsMakerView(newsDataBaseModel.getNewsMakerListModel().get(selectionView.getSelectedNewsMakers()[0]),newsDataBaseModel);
		editNewsMakerView = new EditNewsMakerView(new NewsMakerModel(),newsDataBaseModel);
		editNewsMakerView.jtfName.addActionListener(new EditNewsMakerNameListener());
		//TODO Test delete
		editNewsMakerView.jbtRemoveFromStory.addActionListener(new RemoveNewsMakerFromNewsStoriesListener());
		
		
	}
	/**
	 * <P>
	 * This method allows you to delete the news makers. 
	 * </P>
	 */
	private void deleteNewsMakers() {
		
	}
	/**
	 * <P>
	 * This method deletes the news maker list.
	 * </P>
	 */
	private void deleteNewsMakerList() {
		
	}
	/**
	 * <P>
	 * This method allows you to add a news story to the list.
	 * </P>
	 */
	private void addNewsStory() {
	}
	/**
	 * <P>
	 * This method allows you to edit the news stories.
	 * </P>
	 */
	private void editNewsStories() {
		
	}
	/**
	 * <P>
	 * This method allows you to sort the news stories.
	 * </P>
	 */
	private void sortNewsStories() {
		
	}
	/**
	 * <P>
	 * This method allows you to delete the news stories.
	 * </P>
	 */
	private void deleteNewsStories() {
		
	}
	/**
	 * <P>
	 * This method allows you to delete all news stories.
	 * </P>
	 */
	private void deleteAllNewsStories() {
		
	}
	/**
	 * <P>
	 * This method allows you to display pie charts.
	 * </P>
	 */
	private void displayPieCharts() {
		
	}
	/**
	 * <P>
	 * This method allows you to display text views.
	 * </P>
	 */
	private void displayTextViews() {
		
	}
	
	
	/**
	 * <P>
	 * This class allows you to add a new file menu listener, and decide what actions
	 * should be taken on that object.
	 * </P>
	 *
	 */
	class FileMenuListener implements ActionListener {

		/**
		 * This method allows decides what actions should be taken when an event occurs.
		 */
		@Override
		public void actionPerformed(ActionEvent e) {
			String eventSourceText = ((JMenuItem) e.getSource()).getText();
			if(eventSourceText.equals("Import")) {
				importNoozStories();
			}
			else if(eventSourceText.equals("Export")) {
				exportNewsStories();
			}
			else if(eventSourceText.equals("Save")) {
					saveNewsData();

			}
			else if(eventSourceText.equals("Load")) {
				
				try {
					loadNewsData();
				} catch (ClassNotFoundException e1) {
					e1.printStackTrace();
				} catch (IOException e1) {
					e1.printStackTrace();
				}
				
			}
			
		}
		
	}
	
	/**
	 * <P>
	 * This class allows you to add a new news maker menu listener, and decide what actions
	 * should be taken on that object.
	 * </P>
	 *
	 */
	public class NewsMakerMenuListener implements ActionListener {

		/**
		 * This method allows decides what actions should be taken when an event occurs.
		 */
		@Override
		public void actionPerformed(ActionEvent e) {
			String eventSourceText = ((JMenuItem) e.getSource()).getText();
			if(eventSourceText.equals("Add Newsmaker")) {
				addNewsMaker();
				
			}
			else if(eventSourceText.equals("Edit Newsmaker")) {
				editNewsMakers();
			}
			else if(eventSourceText.equals("Delete Newsmaker")) {
				System.out.println("delete newsmaker");
			}
			else if(eventSourceText.equals("Delete Newsmaker List")) {
				System.out.println("delete newsmaker list");
			}
		}
		
	}
	/**
	 * <P>
	 * This class allows you to add a new news story menu listener, and decide what actions
	 * should be taken on that object.
	 * </P>
	 *
	 */
	public class NewsStoryMenuListener implements ActionListener {

		/**
		 * This method allows decides what actions should be taken when an event occurs.
		 */
		@Override
		public void actionPerformed(ActionEvent e) {
			String eventSourceText = ((JMenuItem) e.getSource()).getText();
			if(eventSourceText.equals("Add News Story")) {
				System.out.println("Add News Story");
			}
			else if(eventSourceText.equals("Edit News Story")) {
				System.out.println("edit News Story");
			}
			else if(eventSourceText.equals("Sort News Stories")) {
				System.out.println("Sort News Stories");
			}
			else if(eventSourceText.equals("Delete News Story")) {
				System.out.println("Delete News Story");
			}
			else if(eventSourceText.equals("Delete All News Stories")) {
				System.out.println("Delete All News Stories");
			}
			
		}
		
	}
	/**
	 * <P>
	 * This class allows you to add a new display menu listener, and decide what actions
	 * should be taken on that object.
	 * </P>
	 *
	 */
	public class DisplayMenuListener implements ActionListener {

		/**
		 * This method allows decides what actions should be taken when an event occurs.
		 */
		@Override
		public void actionPerformed(ActionEvent e) {
			String eventSourceText = ((JMenuItem) e.getSource()).getText();
			if(eventSourceText.equals("Text")) {
				System.out.println("Text");
			}
			else if(eventSourceText.equals("Pie Chart")) {
				System.out.println("Pie Chart");
			}
			
		}
		
	}
	/**
	 * <P>
	 * This class allows you to add a new edit news maker name listener, and decide what actions
	 * should be taken on that object.
	 * </P>
	 *
	 */
	public class EditNewsMakerNameListener implements ActionListener {

		/**
		 * This method allows decides what actions should be taken when an event occurs.
		 */
		@Override
		public void actionPerformed(ActionEvent e) {
			System.out.println("change the newsmakers name to: " +editNewsMakerView.jtfName);
		}
		
	}
	/**
	 * <P>
	 * This class allows you to add a new remove newsmaker from story listener, and decide what actions
	 * should be taken on that object.
	 * </P>
	 *
	 */
	public class RemoveNewsMakerFromNewsStoriesListener implements ActionListener {

		/**
		 * This method allows decides what actions should be taken when an event occurs.
		 */
		@Override
		public void actionPerformed(ActionEvent e) {
			//TODO add code to do stuff
			System.out.println("User click remove from story. Remove the newsmaker from the selected story!!! (using the variables in editnewsmakerview"
					+ " and the newsdatabasemodel variable.");
		}
		
	}
	/**
	 * <P>
	 * This class allows you to add a new add edit story listener, and decide what actions
	 * should be taken on that object.
	 * </P>
	 *
	 */
	public class  AddEditNewsStoryListener implements ActionListener{

		/**
		 * This method allows decides what actions should be taken when an event occurs.
		 */
		@Override
		public void actionPerformed(ActionEvent e) {
			// TODO Auto-generated method stub
			
		}
		
	}
	/**
	 * <P>
	 * This class allows you to add a new media type selection listener, and decide what actions
	 * should be taken on that object.
	 * </P>
	 *
	 */
	public class MediaTypeSelectionListener implements ActionListener {

		/**
		 * This method allows decides what actions should be taken when an event occurs.
		 */
		@Override
		public void actionPerformed(ActionEvent e) {
			// TODO Auto-generated method stub
			
		}
		
	}
	
	public static void main(String[] args) {
		new NewsController();
	}
	
	
}
