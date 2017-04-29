import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.Serializable;

import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;

/**
 * Project 4, CS 2334, Section 010, April 23, 2017
 * <P>
 * This class provides the view to see a newsmaker and edit them.
 * </P>
 * <P>
 * Note that the this class contains a news maker model and various methods used to construct stories
 *  and handle any actions performed by the user. This class also
 * contains a reference to the model, which will be used to pull and display data from. 
 * </P>
 * 
 * @author Dean Hougen, Jered Little, Vishnupriya Parasaram, Jessica Horner, and Zakary Koskovich 
 * @version 1.0
 * 
 */
//Jered Little created the stub code for this class.
public class EditNewsMakerView extends JPanel implements Serializable, ActionListener{

	/**
	 * The serial ID for the edit news maker view.
	 */
	private static final long serialVersionUID = 5965054206963370134L;
	/** The news maker model. */
	NewsMakerModel newsMakerModel; 
	/** The news data base model. */
	private NewsDataBaseModel newsDataBaseModel;
	/** The news story list as a string. */
	private DefaultListModel<String> newsStoryStringList;
	/** The news story list as a JList String. */
	private JList<String> jlNewsStoryList;
	/**  The news story list scroll pane. */
	private JScrollPane jspNewsStoryList;
	/** The panel for the story list. */
	private JPanel jpNewsStoryList;
	/** The text field for the name. */
	JTextField jtfName;
	/** The label for the name. */
	private JLabel jlbName;
	/** The button to remove from story. */
	JButton jbtRemoveFromStory;
	/** The panel for the name. */
	private JPanel jplName;
	
	/**
	 * <P>
	 * This constructor sets the models, and creates the view by initializing all the fields and adding
	 * them to panels.
	 * </P>
	 * @param newsMakerModel The model for the news maker.
	 * @param newsDataBaseModel The model for the data base.
	 */
	public EditNewsMakerView(NewsMakerModel newsMakerModel,  NewsDataBaseModel newsDataBaseModel) {
		this.newsDataBaseModel = newsDataBaseModel;
		
		//list of stories
		jlNewsStoryList = new JList<String>();
		jspNewsStoryList = new JScrollPane();
		jpNewsStoryList = new JPanel();
		jspNewsStoryList.add(jlNewsStoryList);
		
		jpNewsStoryList.add(jspNewsStoryList);
		
		//name & remove from story button.
		jtfName = new JTextField(newsMakerModel.getName());
		jbtRemoveFromStory = new JButton("Remove From Selected Story");
		jlbName = new JLabel(newsMakerModel.getName());
		
		jplName = new JPanel(new GridLayout(2,2));
		jplName.add(jlbName);
		jplName.add(new JLabel());
		jplName.add(jtfName);
		jplName.add(jbtRemoveFromStory);
		
		//add to the panel
		this.setLayout(new BorderLayout());
		this.add(jpNewsStoryList, BorderLayout.NORTH);
		this.add(jplName,BorderLayout.SOUTH);
		
		populateNewsStoryJList();
		
		JFrame frame = new JFrame("Edit Newsmaker");
		frame.add(this); 
		frame.setSize(500, 300);
		frame.setVisible(true);
		
		
	}
	/**
	 * <P>
	 * This method gets the news story indices. //TODO what?
	 * </P>
	 */
	public int[] getSelectedNewsStoryIndices() {
			return null;
			
	}
	/**
	 * <P>
	 * This method populates the news story jlist.
	 * </P>
	 */
	private void populateNewsStoryJList() {
			System.out.println("TODO: FIX THE POPULATENEWSSTORYLIST METHOD IN EDITNEWSMAKERVIEW!!!!!!!");
	}

	/**
	 * <P>
	 * This method handles when an action is performed. 
	 * </P>
	 */
	@Override
	public void actionPerformed(ActionEvent arg0) {
		// TODO Auto-generated method stub
		
	}
	
}
