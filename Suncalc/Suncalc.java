package Suncalc;
import javax.swing.*;
import javax.swing.GroupLayout.Alignment;
import javax.swing.LayoutStyle.ComponentPlacement;
import javax.swing.event.*;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;

import static java.util.Calendar.YEAR;
import static java.util.Calendar.MONTH;
import static java.util.Calendar.DAY_OF_MONTH;

import java.awt.*;
import java.awt.event.*;
import java.io.File;
import java.io.IOException;


public final class Suncalc extends JFrame{
	private static final long serialVersionUID = 1L;

	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				JFrame frame = new AppFrame();
				frame.setTitle("Sun Calculator");
				frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
				frame.setVisible(true);
				frame.setBounds(100, 100, 400, 500);
				frame.setIconImage(new ImageIcon("images/Frame_icon16.png").getImage());
			}
		});
	}
}

final class AppFrame extends JFrame{
	private static final long serialVersionUID = 1L;
	private JMenuBar menuBar;
	private JMenu menuList, optionsList, languageMItem;
	private JMenuItem saveCityMItem, exitFrameMItem;
	private JRadioButtonMenuItem langEnglish, langRussian;
	private JPanel contentPane, panelTheme, panelEnterData, panelShortData, panelMainData, landTheme, panelTime,
	sunPanel;
	private SkyBoxPanel panelSky;
	private AzimuthPanel sunAzimuthPanel;
	private JTextField coordsTF, yearTF, monthTF, dayTF, timeZoneTF;
	private JComboBox<City> cityNameCB;
	private JLabel cityLab, dateLab, coordsLab, timeZoneLab, theme;
	private JLabel nameLabel1, nameLabel2, nameLabel3, nameLabel4, nameLabel5,
	nameLabel6, nameLabel7, nameLabel8;
	private JLabel valueLabel1, valueLabel2, valueLabel3, valueLabel4, valueLabel5,
	valueLabel6, valueLabel7, valueLabel8;
	private JLabel sunriseLabelName, sunriseLabelTime, sunsetLabelName, sunsetLabelTime, dayLongLabelName,
	dayLongLabelTime, solarNoonLabelName, solarNoonLabelTime, twilightLabel, astroTwilightLabelName,
	astroTwilightLabelValue, nauticalTwilightLabelName, nauticalTwilightLabelValue, civilTwilightLabelName,
	civilTwilightLabelValue;
	private JLabel land, timeValueLab;
	private JSlider timeSlider;
	private int themeNum, sunX, sunY;
	private Font tahomaPlain, tahomaBold;
	private Place place;
	private City city;
	private Data data;
	private Clock clock;
	private GregorianCalendar defaultCalendar;
	private CityList cityList;
	private DocumentListener changeInputText;
	private ActionListener cityChange;
	private KeyAdapter keyChange;

	public AppFrame() {
		createCityList();
		getSavings();
		themeNum = 0;
		tahomaPlain = new Font("Tahoma", Font.PLAIN, 11);
		tahomaBold = new Font("Tahoma", Font.BOLD, 10);
		
		menuBar = new JMenuBar();
		menuBar.setBorderPainted(false);
		menuBar.setBackground(new Color(33, 159, 222));

		menuList = new JMenu("Menu");	
		Action saveAction = new AbstractAction("Save Options", new ImageIcon("images/Save_icon32.png")) {
			private static final long serialVersionUID = 1L;

			public void actionPerformed(ActionEvent event) {
				saveCity();
			}
		};
		saveCityMItem = menuList.add(saveAction);
		
		Action exitAction = new AbstractAction("Exit", new ImageIcon("images/Exit_icon32.png")) {
			private static final long serialVersionUID = 1L;

			public void actionPerformed(ActionEvent event) {
				System.exit(0);
			}
		};
		exitFrameMItem = menuList.add(exitAction);
		setJMenuBar(menuBar);
		menuBar.add(menuList);
		
		optionsList = new JMenu("Options");
		menuBar.add(optionsList);
		
		languageMItem = new JMenu("Language");
		languageMItem.setIcon(new ImageIcon("images/Language_icon32.png"));
		optionsList.add(languageMItem);
		ButtonGroup langGroup = new ButtonGroup();
		
		langEnglish = new JRadioButtonMenuItem("English");
		langEnglish.setSelected(true);
		languageMItem.add(langEnglish);
		langGroup.add(langEnglish);
		
		langRussian = new JRadioButtonMenuItem("Russian");
		languageMItem.add(langRussian);
		langGroup.add(langRussian);
		
		changeInputText = new DocumentListener() {
			
			@Override
			public void removeUpdate(DocumentEvent e) {
				System.out.println("Field remove >");
				calculate();
				paintData();
			}
			
			@Override
			public void insertUpdate(DocumentEvent e) {
				System.out.println("Field insert >");
				calculate();
				paintData();
			}
			
			@Override
			public void changedUpdate(DocumentEvent e) {
				System.out.println("Field changed >");
				calculate();
				paintData();
			}
		};
		
		ActionListener resetAction = new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				System.out.println("Reset button >");
				reset();
				paintData();
			}
		};
		
		contentPane = new JPanel();
		contentPane.setBorder(null);
		setContentPane(contentPane);
		contentPane.setLayout(null);
		
		panelEnterData = new JPanel();
		panelEnterData.setBorder(null);
		panelEnterData.setBounds(0, 0, 384, 110);
		panelEnterData.setOpaque(false);
		contentPane.add(panelEnterData);
		
		JButton sunThemeButton = new JButton("Sun");
		sunThemeButton.setForeground(new Color(0, 0, 0));
		sunThemeButton.setFont(new Font("Monospaced", Font.BOLD, 14));
		sunThemeButton.setBackground(new Color(33, 159, 222));
		sunThemeButton.setOpaque(false);
		sunThemeButton.setBorderPainted(false);
		sunThemeButton.setContentAreaFilled(false);
		sunThemeButton.setSelected(true);
		sunThemeButton.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				theme.setIcon(new ImageIcon("images/Sun_theme.png"));
				themeNum = 0;
				changeTextColor();
				menuBar.setBackground(new Color(33, 159, 222));
				paintData();
				repaint();
			}
		});
		
		JButton moonThemeButton = new JButton("Moon");
		moonThemeButton.setForeground(new Color(0, 0, 0));
		moonThemeButton.setFont(new Font("Monospaced", Font.BOLD, 14));
		moonThemeButton.setBackground(new Color(33, 159, 222));
		moonThemeButton.setOpaque(false);
		moonThemeButton.setBorderPainted(false);
		moonThemeButton.setContentAreaFilled(false);
		moonThemeButton.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				theme.setIcon(new ImageIcon("images/Moon_theme.png"));
				themeNum = 1;
				changeTextColor();
				menuBar.setBackground(new Color(202, 224, 235));
				paintData();
				repaint();
			}
		});
		
		ButtonGroup themeGroup = new ButtonGroup();
		themeGroup.add(sunThemeButton);
		themeGroup.add(moonThemeButton);
		
		cityLab = new JLabel("City name");
		cityLab.setHorizontalAlignment(SwingConstants.RIGHT);
		cityLab.setFont(tahomaPlain);
		
		coordsTF = new JTextField();
		coordsTF.setBackground(Color.WHITE);
		coordsTF.setColumns(10);
		
		JButton findCityButton = new JButton("Search city");
		findCityButton.setFont(tahomaPlain);
		
		dateLab = new JLabel("Date");
		dateLab.setHorizontalAlignment(SwingConstants.RIGHT);
		dateLab.setFont(tahomaPlain);
		coordsLab = new JLabel("or place coordinates");
		coordsLab.setFont(tahomaPlain);
		coordsLab.setHorizontalAlignment(SwingConstants.RIGHT);
		
		yearTF = new JTextField();
		yearTF.setColumns(4);
		
		monthTF = new JTextField();
		monthTF.setColumns(2);
		
		dayTF = new JTextField();
		dayTF.setColumns(2);
		
		timeZoneTF = new JTextField();
		timeZoneTF.setColumns(2);
		
		cityNameCB = this.cityList.getComboBox();
		
		JButton calendarButton = new JButton();
		calendarButton.setIcon(new ImageIcon("images/Calendar_icon32.png"));
		calendarButton.setOpaque(false);
		calendarButton.setBorderPainted(false);
		calendarButton.setContentAreaFilled(false);
		JButton resetTimeButton = new JButton("Reset");
		resetTimeButton.setFont(tahomaPlain);
		
		timeZoneLab = new JLabel("time zone");
		timeZoneLab.setHorizontalAlignment(SwingConstants.RIGHT);
		timeZoneLab.setFont(tahomaPlain);
		changeTextColor();
		
		GroupLayout gl_panelEnterData = new GroupLayout(panelEnterData);
		gl_panelEnterData.setHorizontalGroup(
			gl_panelEnterData.createParallelGroup(Alignment.LEADING)
				.addGroup(Alignment.TRAILING, gl_panelEnterData.createSequentialGroup()
					.addContainerGap(61, Short.MAX_VALUE)
					.addGroup(gl_panelEnterData.createParallelGroup(Alignment.TRAILING)
						.addGroup(gl_panelEnterData.createSequentialGroup()
							.addComponent(dateLab)
							.addPreferredGap(ComponentPlacement.RELATED)
							.addComponent(yearTF, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
							.addPreferredGap(ComponentPlacement.RELATED)
							.addComponent(monthTF, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
							.addPreferredGap(ComponentPlacement.RELATED)
							.addComponent(dayTF, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
							.addPreferredGap(ComponentPlacement.UNRELATED)
							.addComponent(calendarButton, GroupLayout.PREFERRED_SIZE, 29, GroupLayout.PREFERRED_SIZE)
							.addPreferredGap(ComponentPlacement.RELATED)
							.addComponent(resetTimeButton))
						.addGroup(gl_panelEnterData.createSequentialGroup()
							.addComponent(sunThemeButton, GroupLayout.PREFERRED_SIZE, 121, GroupLayout.PREFERRED_SIZE)
							.addPreferredGap(ComponentPlacement.RELATED)
							.addComponent(moonThemeButton))
						.addGroup(gl_panelEnterData.createSequentialGroup()
							.addGroup(gl_panelEnterData.createParallelGroup(Alignment.TRAILING, false)
								.addGroup(gl_panelEnterData.createSequentialGroup()
									.addComponent(coordsLab)
									.addPreferredGap(ComponentPlacement.RELATED)
									.addComponent(coordsTF, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
								.addGroup(gl_panelEnterData.createSequentialGroup()
									.addComponent(cityLab, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
									.addPreferredGap(ComponentPlacement.RELATED)
									.addComponent(cityNameCB, GroupLayout.PREFERRED_SIZE, 150, GroupLayout.PREFERRED_SIZE)))
							.addPreferredGap(ComponentPlacement.RELATED)
							.addGroup(gl_panelEnterData.createParallelGroup(Alignment.TRAILING)
								.addComponent(findCityButton)
								.addGroup(gl_panelEnterData.createSequentialGroup()
									.addComponent(timeZoneLab)
									.addPreferredGap(ComponentPlacement.RELATED)
									.addComponent(timeZoneTF, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)))))
					.addGap(27))
		);
		gl_panelEnterData.setVerticalGroup(
			gl_panelEnterData.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_panelEnterData.createSequentialGroup()
					.addGroup(gl_panelEnterData.createParallelGroup(Alignment.BASELINE)
						.addComponent(moonThemeButton, GroupLayout.PREFERRED_SIZE, 20, GroupLayout.PREFERRED_SIZE)
						.addComponent(sunThemeButton, GroupLayout.PREFERRED_SIZE, 20, GroupLayout.PREFERRED_SIZE))
					.addPreferredGap(ComponentPlacement.RELATED)
					.addGroup(gl_panelEnterData.createParallelGroup(Alignment.BASELINE)
						.addComponent(findCityButton, GroupLayout.PREFERRED_SIZE, 20, GroupLayout.PREFERRED_SIZE)
						.addComponent(cityNameCB, GroupLayout.PREFERRED_SIZE, 20, GroupLayout.PREFERRED_SIZE)
						.addComponent(cityLab, GroupLayout.PREFERRED_SIZE, 20, GroupLayout.PREFERRED_SIZE))
					.addPreferredGap(ComponentPlacement.RELATED)
					.addGroup(gl_panelEnterData.createParallelGroup(Alignment.BASELINE)
						.addComponent(coordsTF, GroupLayout.PREFERRED_SIZE, 20, GroupLayout.PREFERRED_SIZE)
						.addComponent(coordsLab, GroupLayout.PREFERRED_SIZE, 20, GroupLayout.PREFERRED_SIZE)
						.addComponent(timeZoneTF, GroupLayout.PREFERRED_SIZE, 20, GroupLayout.PREFERRED_SIZE)
						.addComponent(timeZoneLab, GroupLayout.PREFERRED_SIZE, 20, GroupLayout.PREFERRED_SIZE))
					.addPreferredGap(ComponentPlacement.RELATED)
					.addGroup(gl_panelEnterData.createParallelGroup(Alignment.BASELINE)
						.addComponent(resetTimeButton, GroupLayout.PREFERRED_SIZE, 20, GroupLayout.PREFERRED_SIZE)
						.addComponent(calendarButton, GroupLayout.PREFERRED_SIZE, 20, GroupLayout.PREFERRED_SIZE)
						.addComponent(dayTF, GroupLayout.PREFERRED_SIZE, 20, GroupLayout.PREFERRED_SIZE)
						.addComponent(monthTF, GroupLayout.PREFERRED_SIZE, 20, GroupLayout.PREFERRED_SIZE)
						.addComponent(yearTF, GroupLayout.PREFERRED_SIZE, 20, GroupLayout.PREFERRED_SIZE)
						.addComponent(dateLab, GroupLayout.PREFERRED_SIZE, 20, GroupLayout.PREFERRED_SIZE))
					.addGap(3))
		);
		panelEnterData.setLayout(gl_panelEnterData);
		
		panelTheme = new JPanel();
		panelTheme.setBounds(0, 0, 384, 110);
		contentPane.add(panelTheme);
		
		theme = new JLabel(new ImageIcon("images/Sun_theme.png"));
		theme.setVerticalAlignment(SwingConstants.TOP);
		theme.setHorizontalAlignment(SwingConstants.LEFT);
		GroupLayout gl_panel_2 = new GroupLayout(panelTheme);
		gl_panel_2.setHorizontalGroup(
			gl_panel_2.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_panel_2.createSequentialGroup()
					.addComponent(theme)
					.addContainerGap(GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
		);
		gl_panel_2.setVerticalGroup(
			gl_panel_2.createParallelGroup(Alignment.TRAILING)
				.addGroup(gl_panel_2.createSequentialGroup()
					.addComponent(theme, GroupLayout.DEFAULT_SIZE, 165, Short.MAX_VALUE)
					.addContainerGap())
		);
		panelTheme.setLayout(gl_panel_2);
		
		panelShortData = new JPanel();
		panelShortData.setBounds(0, 110, 192, 180);
		panelShortData.setBorder(BorderFactory.createLineBorder(new Color(0, 0, 0)));
		panelShortData.setBackground(new Color(190, 230, 255));
		contentPane.add(panelShortData);
		
		nameLabel1 = new JLabel("Julian day:");
		nameLabel1.setFont(tahomaPlain);
		nameLabel2 = new JLabel("Modified Julian day:");
		nameLabel2.setFont(tahomaPlain);
		nameLabel3 = new JLabel("Siderial time:");
		nameLabel3.setFont(tahomaPlain);
		nameLabel4 = new JLabel("Star time:");
		nameLabel4.setFont(tahomaPlain);
		nameLabel5 = new JLabel("Right ascension:");
		nameLabel5.setFont(tahomaPlain);
		nameLabel6 = new JLabel("Declination:");
		nameLabel6.setFont(tahomaPlain);
		nameLabel7 = new JLabel("Sun altitude:");
		nameLabel7.setFont(tahomaPlain);
		nameLabel8 = new JLabel("Max sun height:");
		nameLabel8.setFont(tahomaPlain);
		valueLabel1 = new JLabel("New label");
		valueLabel1.setFont(tahomaPlain);
		valueLabel2 = new JLabel("New label");
		valueLabel2.setFont(tahomaPlain);
		valueLabel3 = new JLabel("New label");
		valueLabel3.setFont(tahomaPlain);
		valueLabel4 = new JLabel("New label");
		valueLabel4.setFont(tahomaPlain);
		valueLabel5 = new JLabel("New label");
		valueLabel5.setFont(tahomaPlain);
		valueLabel6 = new JLabel("New label");
		valueLabel6.setFont(tahomaPlain);
		valueLabel7 = new JLabel("New label");
		valueLabel7.setFont(tahomaPlain);
		valueLabel8 = new JLabel("New label");
		valueLabel8.setFont(tahomaPlain);
		GroupLayout gl_panelShortData = new GroupLayout(panelShortData);
		gl_panelShortData.setHorizontalGroup(
			gl_panelShortData.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_panelShortData.createSequentialGroup()
					.addContainerGap()
					.addGroup(gl_panelShortData.createParallelGroup(Alignment.LEADING)
						.addComponent(nameLabel1)
						.addComponent(nameLabel2)
						.addComponent(nameLabel3)
						.addComponent(nameLabel5)
						.addComponent(nameLabel4)
						.addComponent(nameLabel6)
						.addComponent(nameLabel7)
						.addComponent(nameLabel8))
					.addPreferredGap(ComponentPlacement.RELATED, 21, Short.MAX_VALUE)
					.addGroup(gl_panelShortData.createParallelGroup(Alignment.LEADING)
						.addComponent(valueLabel1)
						.addComponent(valueLabel2)
						.addComponent(valueLabel3)
						.addComponent(valueLabel4)
						.addComponent(valueLabel5)
						.addComponent(valueLabel6)
						.addComponent(valueLabel7)
						.addComponent(valueLabel8))
					.addContainerGap(32, Short.MAX_VALUE))
		);
		gl_panelShortData.setVerticalGroup(
			gl_panelShortData.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_panelShortData.createSequentialGroup()
					.addContainerGap()
					.addGroup(gl_panelShortData.createParallelGroup(Alignment.BASELINE)
						.addComponent(nameLabel1)
						.addComponent(valueLabel1))
					.addPreferredGap(ComponentPlacement.RELATED)
					.addGroup(gl_panelShortData.createParallelGroup(Alignment.BASELINE)
						.addComponent(nameLabel2)
						.addComponent(valueLabel2))
					.addPreferredGap(ComponentPlacement.RELATED)
					.addGroup(gl_panelShortData.createParallelGroup(Alignment.BASELINE)
						.addComponent(nameLabel3)
						.addComponent(valueLabel3))
					.addPreferredGap(ComponentPlacement.RELATED)
					.addGroup(gl_panelShortData.createParallelGroup(Alignment.BASELINE)
						.addComponent(nameLabel4)
						.addComponent(valueLabel4))
					.addPreferredGap(ComponentPlacement.RELATED)
					.addGroup(gl_panelShortData.createParallelGroup(Alignment.BASELINE)
						.addComponent(nameLabel5)
						.addComponent(valueLabel5))
					.addPreferredGap(ComponentPlacement.RELATED)
					.addGroup(gl_panelShortData.createParallelGroup(Alignment.BASELINE)
						.addComponent(nameLabel6)
						.addComponent(valueLabel6))
					.addPreferredGap(ComponentPlacement.RELATED)
					.addGroup(gl_panelShortData.createParallelGroup(Alignment.BASELINE)
						.addComponent(nameLabel7)
						.addComponent(valueLabel7))
					.addPreferredGap(ComponentPlacement.RELATED)
					.addGroup(gl_panelShortData.createParallelGroup(Alignment.BASELINE)
						.addComponent(nameLabel8)
						.addComponent(valueLabel8))
					.addContainerGap(35, Short.MAX_VALUE))
		);
		panelShortData.setLayout(gl_panelShortData);
		
		panelMainData = new JPanel();
		panelMainData.setBounds(192, 110, 192, 180);
		panelMainData.setBorder(BorderFactory.createLineBorder(new Color(0, 0, 0)));
		panelMainData.setBackground(new Color(190, 230, 255));
		contentPane.add(panelMainData);
		
		sunriseLabelName = new JLabel("Sunrise:");
		sunriseLabelName.setHorizontalAlignment(SwingConstants.RIGHT);
		sunriseLabelName.setFont(tahomaBold);
		sunriseLabelName.setForeground(new Color(255, 99, 71));
		
		solarNoonLabelName = new JLabel("Solar noon:");
		solarNoonLabelName.setHorizontalAlignment(SwingConstants.RIGHT);
		solarNoonLabelName.setFont(tahomaBold);
		solarNoonLabelName.setForeground(new Color(100, 149, 237));
		
		sunsetLabelName = new JLabel("Sunset:");
		sunsetLabelName.setHorizontalAlignment(SwingConstants.RIGHT);
		sunsetLabelName.setFont(tahomaBold);
		sunsetLabelName.setForeground(new Color(255, 0, 0));
		
		sunriseLabelTime = new JLabel("New label");
		sunriseLabelTime.setFont(tahomaBold);
		sunriseLabelTime.setForeground(new Color(255, 99, 71));
		
		solarNoonLabelTime = new JLabel("New label");
		solarNoonLabelTime.setFont(tahomaBold);
		solarNoonLabelTime.setForeground(new Color(100, 149, 237));
		
		sunsetLabelTime = new JLabel("New label");
		sunsetLabelTime.setFont(tahomaBold);
		sunsetLabelTime.setForeground(new Color(255, 0, 0));
		
		dayLongLabelName = new JLabel("Day long:");
		dayLongLabelName.setHorizontalAlignment(SwingConstants.RIGHT);
		dayLongLabelName.setFont(tahomaBold);
		
		dayLongLabelTime = new JLabel("New label");
		dayLongLabelTime.setFont(tahomaBold);
		
		twilightLabel = new JLabel("Twilight:");
		twilightLabel.setHorizontalAlignment(SwingConstants.CENTER);
		twilightLabel.setFont(tahomaPlain);
		
		astroTwilightLabelName = new JLabel("Astronomical:");
		astroTwilightLabelName.setFont(tahomaBold);
		astroTwilightLabelName.setForeground(new Color(0, 0, 127));
		nauticalTwilightLabelName = new JLabel("Nautical:");
		nauticalTwilightLabelName.setFont(tahomaBold);
		nauticalTwilightLabelName.setForeground(new Color(0, 0, 205));
		civilTwilightLabelName = new JLabel("Civil:");
		civilTwilightLabelName.setFont(tahomaBold);
		civilTwilightLabelName.setForeground(new Color(100, 149, 237));
		astroTwilightLabelValue = new JLabel("New label");
		astroTwilightLabelValue.setFont(tahomaBold);
		astroTwilightLabelValue.setForeground(new Color(0, 0, 127));
		nauticalTwilightLabelValue = new JLabel("New label");
		nauticalTwilightLabelValue.setFont(tahomaBold);
		nauticalTwilightLabelValue.setForeground(new Color(0, 0, 205));
		civilTwilightLabelValue = new JLabel("New label");
		civilTwilightLabelValue.setFont(tahomaBold);
		civilTwilightLabelValue.setForeground(new Color(100, 149, 237));
		
		GroupLayout gl_panelMainData = new GroupLayout(panelMainData);
		gl_panelMainData.setHorizontalGroup(
			gl_panelMainData.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_panelMainData.createSequentialGroup()
					.addGap(38)
					.addGroup(gl_panelMainData.createParallelGroup(Alignment.TRAILING)
						.addComponent(solarNoonLabelName)
						.addComponent(sunriseLabelName)
						.addComponent(sunsetLabelName)
						.addComponent(dayLongLabelName))
					.addPreferredGap(ComponentPlacement.RELATED)
					.addGroup(gl_panelMainData.createParallelGroup(Alignment.LEADING)
						.addComponent(dayLongLabelTime)
						.addComponent(sunsetLabelTime)
						.addComponent(solarNoonLabelTime)
						.addComponent(sunriseLabelTime))
					.addGap(44))
				.addGroup(gl_panelMainData.createSequentialGroup()
					.addContainerGap()
					.addGroup(gl_panelMainData.createParallelGroup(Alignment.LEADING)
						.addGroup(gl_panelMainData.createSequentialGroup()
							.addGap(28)
							.addGroup(gl_panelMainData.createSequentialGroup()
								.addGap(37)
								.addComponent(twilightLabel, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
						.addGroup(gl_panelMainData.createSequentialGroup()
							.addGroup(gl_panelMainData.createParallelGroup(Alignment.LEADING)
								.addComponent(astroTwilightLabelName)
								.addComponent(nauticalTwilightLabelName))
							.addPreferredGap(ComponentPlacement.RELATED)
							.addGroup(gl_panelMainData.createParallelGroup(Alignment.LEADING)
								.addComponent(nauticalTwilightLabelValue)
								.addComponent(astroTwilightLabelValue)
								.addComponent(civilTwilightLabelValue))))
					.addGap(60))
				.addGroup(gl_panelMainData.createSequentialGroup()
					.addContainerGap()
					.addComponent(civilTwilightLabelName)
					.addContainerGap(134, Short.MAX_VALUE))
		);
		gl_panelMainData.setVerticalGroup(
			gl_panelMainData.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_panelMainData.createSequentialGroup()
					.addContainerGap()
					.addGroup(gl_panelMainData.createParallelGroup(Alignment.BASELINE)
						.addComponent(sunriseLabelTime)
						.addComponent(sunriseLabelName))
					.addPreferredGap(ComponentPlacement.RELATED)
					.addGroup(gl_panelMainData.createParallelGroup(Alignment.BASELINE)
						.addComponent(solarNoonLabelTime)
						.addComponent(solarNoonLabelName))
					.addPreferredGap(ComponentPlacement.RELATED)
					.addGroup(gl_panelMainData.createParallelGroup(Alignment.BASELINE)
						.addComponent(sunsetLabelTime)
						.addComponent(sunsetLabelName))
					.addPreferredGap(ComponentPlacement.RELATED)
					.addGroup(gl_panelMainData.createParallelGroup(Alignment.BASELINE)
						.addComponent(dayLongLabelTime)
						.addComponent(dayLongLabelName))
					.addPreferredGap(ComponentPlacement.RELATED)
					.addComponent(twilightLabel)
					.addPreferredGap(ComponentPlacement.RELATED)
					.addGroup(gl_panelMainData.createParallelGroup(Alignment.BASELINE)
						.addComponent(astroTwilightLabelName)
						.addComponent(astroTwilightLabelValue))
					.addPreferredGap(ComponentPlacement.RELATED)
					.addGroup(gl_panelMainData.createParallelGroup(Alignment.BASELINE)
						.addComponent(nauticalTwilightLabelName)
						.addComponent(nauticalTwilightLabelValue))
					.addPreferredGap(ComponentPlacement.RELATED)
					.addGroup(gl_panelMainData.createParallelGroup(Alignment.BASELINE)
						.addComponent(civilTwilightLabelName)
						.addComponent(civilTwilightLabelValue))
					.addContainerGap(15, Short.MAX_VALUE))
		);
		panelMainData.setLayout(gl_panelMainData);
		
		panelTime = new JPanel();
		panelTime.setBounds(0, 400, 384, 40);
		panelTime.setOpaque(false);
		contentPane.add(panelTime);
		
		ChangeListener sliderAction = new ChangeListener() {
			
			@Override
			public void stateChanged(ChangeEvent e) {
				//System.out.println("Slider action >");
				int value = timeSlider.getValue();
				clock.timeFromSeconds(value);
				double x = (384.0/86400.0)*value;
				sunX = (int)x - 12;
				data.timeToCalendar();
				timeValueLab.setText("" + clock);
				calculate();
				paintData();
			}
		};
		
		timeSlider = new JSlider();
		timeSlider.setMajorTickSpacing(10800);
		timeSlider.setMaximum(86400);
		timeSlider.setPaintTicks(true);
		timeSlider.setOpaque(false);
		
		JLabel timeIconLab = new JLabel(new ImageIcon("images/Time_icon16.png"));
		
		timeValueLab = new JLabel();
		timeValueLab.setFont(tahomaBold);
		GroupLayout gl_panel_1 = new GroupLayout(panelTime);
		gl_panel_1.setHorizontalGroup(
			gl_panel_1.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_panel_1.createSequentialGroup()
						.addContainerGap()
						.addComponent(timeIconLab, GroupLayout.PREFERRED_SIZE, 16, GroupLayout.PREFERRED_SIZE)
						.addPreferredGap(ComponentPlacement.RELATED)
						.addComponent(timeSlider, GroupLayout.PREFERRED_SIZE, 270, GroupLayout.PREFERRED_SIZE)
						.addPreferredGap(ComponentPlacement.RELATED)
						.addComponent(timeValueLab)
						.addContainerGap(GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
		);
		gl_panel_1.setVerticalGroup(
			gl_panel_1.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_panel_1.createSequentialGroup()
						.addContainerGap(GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
						.addGroup(gl_panel_1.createParallelGroup(Alignment.LEADING)
							.addComponent(timeValueLab)
							.addComponent(timeSlider, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
							.addComponent(timeIconLab, GroupLayout.PREFERRED_SIZE, 16, GroupLayout.PREFERRED_SIZE)))
		);
		panelTime.setLayout(gl_panel_1);
		
		sunAzimuthPanel = new AzimuthPanel();
		sunAzimuthPanel.setBounds(0, 290, 384, 160);
		contentPane.add(sunAzimuthPanel);
		
		landTheme = new JPanel();
		landTheme.setBounds(0, 290, 384, 160);
		landTheme.setOpaque(false);
		contentPane.add(landTheme);
		
		ImageIcon landIcon = new ImageIcon("images/Land_day_theme.png");
		
		land = new JLabel(landIcon);
		land.setVerticalAlignment(SwingConstants.TOP);
		land.setHorizontalAlignment(SwingConstants.LEFT);
		GroupLayout gl_panel_3 = new GroupLayout(landTheme);
		gl_panel_3.setHorizontalGroup(
			gl_panel_3.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_panel_3.createSequentialGroup()
					.addComponent(land)
					.addContainerGap(GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
		);
		gl_panel_3.setVerticalGroup(
			gl_panel_3.createParallelGroup(Alignment.TRAILING)
				.addGroup(gl_panel_3.createSequentialGroup()
					.addComponent(land, GroupLayout.DEFAULT_SIZE, 160, Short.MAX_VALUE)
					.addContainerGap())
		);
		landTheme.setLayout(gl_panel_3);
		
		cityChange = new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				//System.out.println("CB action >");
				if(e.getActionCommand() == "comboBoxChanged") {
					//if(cityNameCB.getSelectedItem() instanceof String)
						//typeCity((String)cityNameCB.getSelectedItem());
					if(cityNameCB.getSelectedItem() instanceof City)
						enterCity((City)cityNameCB.getSelectedItem());
				}
				if(cityNameCB.getSelectedItem() != null)
					cityNameCB.setPopupVisible(true);
			}
		};
		
		keyChange = new KeyAdapter() {
			
			public void keyPressed(KeyEvent arg0) {
				//System.out.println("key > " + arg0.getKeyCode() + " " + arg0.getKeyChar());
				String text = "";
				if(arg0.getKeyCode() != 8 && arg0.getKeyCode() != 10)
					text = cityNameCB.getEditor().getItem().toString() + arg0.getKeyChar();
				else if(arg0.getKeyCode() == 8){
					String str = cityNameCB.getEditor().getItem().toString();
					if(str.length() > 1)
						text = str.substring(0, str.length() - 1);
				}
				typeCity(text, arg0.getKeyCode());
				cityNameCB.setPopupVisible(true);
				cityNameCB.addActionListener(cityChange);
				cityNameCB.getEditor().getEditorComponent().addKeyListener(keyChange);
			}
		};
		
		sunPanel = new JPanel();
		double x = (384.0/86400.0)*this.clock.getTimeInSeconds();
		this.sunX = (int)x - 12;
		sunPanel.setBounds(sunX, sunY, 24, 24);
		sunPanel.setLayout(null);
		sunPanel.setOpaque(false);
		contentPane.add(sunPanel);
		JLabel sunIconLabel = new JLabel(new ImageIcon("images/Sun_24.png"));
		sunPanel.add(sunIconLabel);
		sunIconLabel.setBounds(0, 0, 24, 24);
		
		panelSky = new SkyBoxPanel();
		panelSky.setBounds(0, 290, 384, 160);
		contentPane.add(panelSky);
		
		coordsTF.setEditable(true);
		coordsTF.setText("" + this.data.getPlaceCoordinates().getLatitude() + " " +
				this.data.getPlaceCoordinates().getLongitude());
		yearTF.setText("" + this.data.getCalendar().get(YEAR));
		monthTF.setText("" + (this.data.getCalendar().get(MONTH) + 1));
		dayTF.setText("" + this.data.getCalendar().get(DAY_OF_MONTH));
		timeZoneTF.setText("" + this.data.getTimeZone());
		timeSlider.setValue(this.data.getTime().getTimeInSeconds());
		timeValueLab.setText("" + this.clock);
		cityNameCB.setSelectedItem(this.city.getName() + ", " + this.city.getCountry().getName());
		paintData();
		
		dayTF.getDocument().addDocumentListener(changeInputText);
		monthTF.getDocument().addDocumentListener(changeInputText);
		yearTF.getDocument().addDocumentListener(changeInputText);
		timeZoneTF.getDocument().addDocumentListener(changeInputText);
		coordsTF.getDocument().addDocumentListener(changeInputText);
		resetTimeButton.addActionListener(resetAction);
		timeSlider.addChangeListener(sliderAction);
		cityNameCB.addActionListener(cityChange);
		cityNameCB.getEditor().getEditorComponent().addKeyListener(keyChange);
	}
	
	private void createCityList() {
		try {
			final File xmlFile = new File("data/cities.xml");
			DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
			DocumentBuilder db = dbf.newDocumentBuilder();
			Document document = db.parse(xmlFile);
			
			document.getDocumentElement().normalize();
			
			this.cityList = new CityList();
			NodeList countryList = document.getElementsByTagName("Country");
			int l = countryList.getLength();
			for(int i = 0; i < l; i++) {
				Node node = countryList.item(i);
				if(Node.ELEMENT_NODE == node.getNodeType()) {
					Element country = (Element)node;
					int id = Integer.parseInt(country.getAttribute("id"));
					String name = country.getElementsByTagName("name").item(0).getTextContent();
					Element capital = (Element)country.getElementsByTagName("Capital").item(0);
					String capName = capital.getElementsByTagName("name").item(0).getTextContent();
					int cityId = Integer.parseInt(capital.getAttribute("id"));
					double lat = Double.parseDouble(capital.getElementsByTagName("latitude").
							item(0).getTextContent());
					double lon = Double.parseDouble(capital.getElementsByTagName("longitude").
							item(0).getTextContent());
					int timeZone = Integer.parseInt(capital.getElementsByTagName("timeZone").
							item(0).getTextContent());
					City city = new City(new Coordinates(lat, lon), cityId, capName, timeZone);
					Country c = new Country(id, name, city);
					city.setCountry(c);
					this.cityList.setCountry(c);
				}
			}			
			this.cityList.createCityList();
		} catch(ParserConfigurationException | SAXException | IOException ex) {
			Logger.getLogger(AppFrame.class.getName()).log(Level.SEVERE, null, ex);
		}
	}
	
	private void getSavings() {
		try {
			final File xmlFile = new File("data/save.xml");
			DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
			DocumentBuilder db = dbf.newDocumentBuilder();
			Document document = db.parse(xmlFile);
			
			document.getDocumentElement().normalize();
			
			NodeList nList = document.getElementsByTagName("Place");
			Node node = nList.item(0);
			if(Node.ELEMENT_NODE == node.getNodeType()) {
				Element element = (Element)node;
				String cityName = "" + element.getElementsByTagName("cityName").item(0).getTextContent();
				int id = Integer.parseInt(element.getElementsByTagName("cityId").item(0).getTextContent());
				int countryId = Integer.parseInt(element.getElementsByTagName("countryId").
						item(0).getTextContent());
				double lat = Double.parseDouble(element.getElementsByTagName("latitude").item(0).getTextContent());
				double lon = Double.parseDouble(element.getElementsByTagName("longitude").item(0).getTextContent());
				int timeZone = Integer.parseInt(element.getElementsByTagName("timeZone").
						item(0).getTextContent());
				this.city = new City(new Coordinates(lat, lon), id, cityName, timeZone, this.cityList.getCountry(countryId));
				this.place = new Place(new Coordinates(lat, lon), timeZone);
				this.data = new Data(place);
				this.clock = data.getTime();
				this.defaultCalendar = new GregorianCalendar();
			}
		} catch(ParserConfigurationException | SAXException | IOException ex) {
			Logger.getLogger(AppFrame.class.getName()).log(Level.SEVERE, null, ex);
		}
	}
	
	private void typeCity(String key, int keyCode) {
		//System.out.println("Type city > " + key);
		this.cityNameCB.removeActionListener(cityChange);
		this.cityNameCB.getEditor().getEditorComponent().removeKeyListener(keyChange);
		if(key.equals("")) {
			this.cityNameCB.setSelectedIndex(-1);
		} else {
			NodeTree node = this.cityList.getCitiesTree().getNode(key);
			if(node != null) {
				//System.out.println("node: > " + node.getKey());
				ArrayList<City> cities = new ArrayList<City>();
				this.cityList.getCitiesFromNode(node, cities);
				this.cityList.sortingCities(cities);
				
				this.cityNameCB.setSelectedItem(cities.get(0));
				int selected = this.cityNameCB.getSelectedIndex() + 6;
				if(selected >= this.cityNameCB.getItemCount())
					selected = this.cityNameCB.getItemCount() - 1;
				this.cityNameCB.setSelectedIndex(selected);
				//System.out.println("key: > " + key);
			}
			if(keyCode != 8)
				this.cityNameCB.setSelectedItem(key.substring(0, key.length()-1));
			else {
				if(key.length() > 0)
					this.cityNameCB.setSelectedItem(key + key.charAt(key.length()-1));
			}
		}
	}
	
	private void enterCity(City city) {
		System.out.println("Enter city >");
		this.place.copyFrom(city);
		this.city = city;
		this.data.recalculate();
		//System.out.println("enter: " + this.place.getTimeZone());
		this.coordsTF.getDocument().removeDocumentListener(changeInputText);
		this.timeZoneTF.getDocument().removeDocumentListener(changeInputText);
		this.coordsTF.setText("" + this.place.getCoordinates().getLatitude() + " " +
		this.place.getCoordinates().getLongitude());
		this.timeZoneTF.setText("" + this.place.getTimeZone());
		paintData();
		this.coordsTF.getDocument().addDocumentListener(changeInputText);
		this.timeZoneTF.getDocument().addDocumentListener(changeInputText);
	}
	
	private void saveCity() {
		
	}
	
	private void changeTextColor() {
		switch(this.themeNum) {
		case 0:
			this.cityLab.setForeground(new Color(0, 0, 0));
			this.dateLab.setForeground(new Color(0, 0, 0));
			this.coordsLab.setForeground(new Color(0, 0, 0));
			this.timeZoneLab.setForeground(new Color(0, 0, 0));
			break;
		case 1:
			this.cityLab.setForeground(new Color(255, 255, 255));
			this.dateLab.setForeground(new Color(255, 255, 255));
			this.coordsLab.setForeground(new Color(255, 255, 255));
			this.timeZoneLab.setForeground(new Color(255, 255, 255));
			break;
		}
	}
	
	private void calculate() {
		if(this.coordsTF.getText().length() > 2 && this.timeZoneTF.getText().length() > 0 &&
				this.yearTF.getText().length() > 0 && this.monthTF.getText().length() > 0 &&
				this.dayTF.getText().length() > 0) {
			//System.out.println("Calculate >");
			int year = Integer.parseInt(this.yearTF.getText());
			int month = Integer.parseInt(this.monthTF.getText()) - 1;
			int day = Integer.parseInt(this.dayTF.getText());
			int timeZone = Integer.parseInt(this.timeZoneTF.getText());
			String[] coords = this.coordsTF.getText().split(" ");
			
			double latitude = Double.parseDouble(coords[0]);
			double longitude = Double.parseDouble(coords[1]);
			
			GregorianCalendar gCalendar = data.getCalendar();
			gCalendar.set(year, month, day);
			place.getCoordinates().setCoordinates(latitude, longitude);
			place.setTimeZone(timeZone);
			if(latitude != city.getCoordinates().getLatitude() ||
					longitude != city.getCoordinates().getLongitude()) {
				this.cityNameCB.setSelectedIndex(-1);
			}
			data.recalculate();	
		}
	}
	
	private void reset() {
		this.yearTF.setText("" + defaultCalendar.get(YEAR));
		this.monthTF.setText("" + (defaultCalendar.get(MONTH) + 1));
		this.dayTF.setText("" + defaultCalendar.get(DAY_OF_MONTH));
		clock.setTime(defaultCalendar);
		timeValueLab.setText("" + clock);
		timeSlider.setValue(clock.getTimeInSeconds());
	}
	
	private void paintData() {
		JLabel[] names = {nameLabel2, nameLabel3, nameLabel4, nameLabel5, nameLabel6, nameLabel7, nameLabel8};
		JLabel[] values = {valueLabel1, valueLabel2, valueLabel3, valueLabel4, valueLabel5, valueLabel6, valueLabel7, valueLabel8};
		JLabel[] sunData = {sunriseLabelTime, solarNoonLabelName, solarNoonLabelTime, sunsetLabelName, sunsetLabelTime, dayLongLabelName, dayLongLabelTime, twilightLabel,
				astroTwilightLabelName, astroTwilightLabelValue, nauticalTwilightLabelName, nauticalTwilightLabelValue, civilTwilightLabelName, civilTwilightLabelValue};
		
		if(this.themeNum == 0) {
			this.nameLabel1.setText("Julian day:");
			this.sunriseLabelName.setText("Sunrise:");
			for(JLabel lab : names)
				lab.setVisible(true);
			for(JLabel lab : values)
				lab.setVisible(true);
			for(JLabel lab : sunData)
				lab.setVisible(true);
			
			this.valueLabel1.setText("" + data.getJulianDay(data.getCalendar(), 3));
			this.valueLabel2.setText("" + data.getModifiedJulianDay(data.getCalendar(), 3));
			this.valueLabel3.setText("" + data.getSiderialTime(data.getCalendar(), 3));
			this.valueLabel4.setText("" + data.getStarTimeClock());
			this.valueLabel5.setText("" + data.getSunParameter(Sun.ASCENSION, 3) + "°");
			this.valueLabel6.setText("" + data.getSunParameter(Sun.DECLINATION, 3) + "°");
			this.valueLabel7.setText("" + data.getSunParameter(Sun.ALTITUDE, 3) + "°");
			double maxSun = data.getMaxSunAltitude(3);
			this.valueLabel8.setText("" + maxSun + "°");
		
			this.solarNoonLabelTime.setText("" + data.getSunTime(SunTimer.SOLAR_NOON));
		
			if(this.data.getSun().isPolarDay()) {
				this.dayLongLabelTime.setText("Polar day");
				this.sunriseLabelTime.setText("----");
				this.sunsetLabelTime.setText("----");
			}
			else if(this.data.getSun().isPolarNight()) {
				this.dayLongLabelTime.setText("Polar night");
				this.sunriseLabelTime.setText("----");
				this.sunsetLabelTime.setText("----");
			}
			else {
				this.dayLongLabelTime.setText("" + data.getSunTime(SunTimer.DAY_LONG));
				this.sunriseLabelTime.setText("" + data.getSunTime(SunTimer.SUNRISE));
				this.sunsetLabelTime.setText("" + data.getSunTime(SunTimer.SUNSET));
			}
			
			if(!this.data.getSun().isAstronomicalTwilight())
				this.astroTwilightLabelValue.setText("" + data.getSunTime(SunTimer.DAWN) + " - " +
					data.getSunTime(SunTimer.DUSK));
			else if(this.data.getSun().isPolarDay())
				this.astroTwilightLabelValue.setText("No night");
			else
				this.astroTwilightLabelValue.setText("No twilight");
		
			if(!this.data.getSun().isNauticalTwilight())
				this.nauticalTwilightLabelValue.setText("" + data.getSunTime(SunTimer.NAUTICAL_DAWN) + " - " +
					data.getSunTime(SunTimer.NAUTICAL_DUSK));
			else if(this.data.getSun().isPolarDay())
				this.nauticalTwilightLabelValue.setText("No astro twilight");
			else
				this.nauticalTwilightLabelValue.setText("No nautical twilight");
		
			if(!this.data.getSun().isCivilTwilight())
				this.civilTwilightLabelValue.setText("" + data.getSunTime(SunTimer.CIVIL_DAWN) + " - " +
					data.getSunTime(SunTimer.CIVIL_DUSK));
			else if(this.data.getSun().isPolarDay())
				this.civilTwilightLabelValue.setText("No nautical twilight");
			else
				this.civilTwilightLabelValue.setText("No civil twilight");
		}
		else {
			this.nameLabel1.setText("ComingSoon!");
			this.sunriseLabelName.setText("Coming Soon!");
			for(JLabel lab : names)
				lab.setVisible(false);
			for(JLabel lab : values)
				lab.setVisible(false);
			for(JLabel lab : sunData)
				lab.setVisible(false);
		}
		
		double alt = data.getSunParameter(Sun.ALTITUDE, 3);
		double y = 390 - alt*(100.0/80.0);
		if(alt > 80)
			y = 290;
		else if(alt < -80)
			y = 490;
		sunY = (int)y;
		this.sunPanel.setBounds(this.sunX, this.sunY, 24, 24);
		
		this.panelSky.setColor(alt);
		if(alt > -6 && alt <= 1) {
			this.land.setIcon(new ImageIcon("images/Land_sunset_theme.png"));
			this.timeValueLab.setForeground(new Color(180, 180, 180));
		}
		else if(alt > 1) {
			this.land.setIcon(new ImageIcon("images/Land_day_theme.png"));
			this.timeValueLab.setForeground(new Color(0, 0, 0));
		}
		else {
			this.land.setIcon(new ImageIcon("images/Land_night_theme.png"));
			this.timeValueLab.setForeground(new Color(255, 255, 255));
		}
		
		int angle = (int)(((this.clock.getTimeInHours()/24) - this.data.getSun().getAzimuthAnomaly())*360);
		while(angle >= 360)
			angle -= 360;
		while(angle < 0)
			angle += 360;
		if(this.data.getSunParameter(Sun.ALTITUDE, 3) > -0.8)
			this.sunAzimuthPanel.setCoords(true, this.sunX + 12, 390 - this.sunY - 12, angle,
					this.data.getSunParameter(Sun.ALTITUDE, 2), this.data.getSun().getAzimuthAnomaly());
		else
			this.sunAzimuthPanel.setCoords(false, this.sunX + 12, 390 - this.sunY - 12, angle,
					this.data.getSunParameter(Sun.ALTITUDE, 2), this.data.getSun().getAzimuthAnomaly());
	}
	
	public int roundToInt(double num) {
		return (int)Math.round(num);
	}
}