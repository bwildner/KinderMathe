package de.bewild.kindermathe;



import java.io.File;
import java.util.ArrayList;
import java.util.Locale;

import android.os.Bundle;
import android.os.Environment;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.speech.tts.TextToSpeech;
import android.speech.tts.TextToSpeech.OnInitListener;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.ToggleButton;

public class KinderMatheActivity extends Activity implements OnClickListener, OnInitListener {

	/*
	KinderMathe
	Version 5  Datum 26.11.2013
	
	Zahlen addieren und subtrahieren
	Zahlenbereich einstellbar bis 10, 100, 1000
		
	Drehen des Bildschirm nicht erlaubt. Neue Zahlen durch Button Nochmal.
	
	Bei richtigem Ergebnis; gelber Smiley, Sprachausgabe "Richtig" und anzeigen eines Bildes aus der Gallerie
	bei falsch: blauer Heuli bei falsch und Sprachausgabe "Falsch"
	
	Button "-" und "+"
	
	grosse Zahlentastatur
	
	für Android 4.4 ertüchtigt
	*/
	
	
	private Button lsgButton;
	private ToggleButton minusButton;
	private ToggleButton plusButton;
	
	private ToggleButton bis10;
	private ToggleButton bis100;
	private ToggleButton bis1000;
	
	private Button nochmalButton;
	
	private TextToSpeech tts;
	
//	 private String pfad = "/sdcard/DCIM/Camera"; Pfad auf Handy
//	 private String pfad = "/mnt/sdcard"; //Pfad Handyemulator
//	 private String pfad = "/storage/sdcard"; //Pfad Tabletemulator
	 private File filepfad= Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM);
	 private String pfad = filepfad.getAbsolutePath();
		
	int zahl1;
    int zahl2;
    int bildnummer;
    String aufgabe;
    boolean richtig = false;
    File bild;
    
  static  ArrayList<File> treffer ;
  	private Pic bildliste;
	
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_kindermathe);
        // Buttons initialisieren
        lsgButton = (Button)findViewById(R.id.lsg_button);
        lsgButton.setOnClickListener(this);
  
        minusButton = (ToggleButton)findViewById(R.id.minus_button);
        minusButton.setOnClickListener(this);
  
        plusButton = (ToggleButton)findViewById(R.id.plus_button);
        plusButton.setOnClickListener(this);

        bis10 = (ToggleButton)findViewById(R.id.bis10_button);
        bis10.setOnClickListener(this);

        bis100 = (ToggleButton)findViewById(R.id.bis100_button);
        bis100.setOnClickListener(this);
        
        bis1000 = (ToggleButton)findViewById(R.id.bis1000_button);
        bis1000.setOnClickListener(this);
        
        nochmalButton = (Button)findViewById(R.id.nochmal_button);
        nochmalButton.setOnClickListener(this);
        
        minusButton.setChecked(false);
        plusButton.setChecked(true); //plus ist Standardeinstellung
    
        bis10.setChecked(true); //bis10 ist Standardeinstellung
        bis100.setChecked(false);
        bis1000.setChecked(false);
        
        
        //Sprachausgabe intialisieren
        tts = new TextToSpeech(this, this);       
        
        Suchen suchen = new Suchen(); //Instanz der suchen klasse anlegen    
        File Datei = new File(pfad);//neues Fileobjekt  für die suche anlegen
        ArrayList<File> treffer = new ArrayList<File> (); //Arraylistobjekt anlegen
    	treffer = suchen.searchFile(Datei, ".jpg"); //Alle jpg files sind nun in treffer gespeichert
    	
    	bildliste = new Pic(); //neues Pic Objekt
    	bildliste.setpic(treffer); //Alle Bilddateien an Pic Objekt übergeben
    	
    	//File datei3 = bildliste.getpic();
    	//System.out.println("onCreate ausgewähltes Bild : "+datei3.getAbsolutePath()); 
         
    	System.out.println("Suchpfad: "+pfad); 
        
    	
    	//Ausgabe aller Bilddateien zu Testzwecken in Console
    //	for (File Datei2 : treffer){
    	//	 System.out.println("Name der Bilder "+ Datei2.getAbsolutePath()); // Name der Datei ausgeben
    //	}
    	 System.out.println("Anzahl gefundener Dateien: "+treffer.size()); 
         
    	 System.out.println("Ende OnCreate"); 
    	 
    	 //Ausgabe der Aufgabe
        anzeige();
     

    }

    public void anzeige(){
    
    	//boolean minus = minusButton.isChecked();
        boolean plus = plusButton.isChecked();
        int bereich=10;
        String aufgabe;
      
        if (bis10.isChecked()) bereich=10;
        if (bis100.isChecked()) bereich=100;
        if (bis1000.isChecked()) bereich=1000;
        
        
        
    	
    	if (plus)
    {
    	do{ 
    		java.util.Random random = new java.util.Random();
    		zahl1 = random.nextInt(bereich); // 0 <= bar < 100
    		zahl2 = random.nextInt(bereich); // 0 <= bar < 100
    	
    	} while ((zahl1+zahl2)>bereich);
    	aufgabe =(" "+ zahl1 +" + "+zahl2);
	    
    }
    else
    {
    	do{ 
    		java.util.Random random = new java.util.Random();
    		zahl1 = random.nextInt(bereich); // 0 <= bar < 100
    		zahl2 = random.nextInt(bereich); // 0 <= bar < 100
    	
    	} while ((zahl2-zahl1)>=0);
    	aufgabe =(" "+ zahl1 +" - "+zahl2);
	    
    }
        TextView Aufgabe = (TextView) findViewById(R.id.aufgabenfeld);
	    Aufgabe.setText(aufgabe);

    }
    
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.activity_hello_workshop, menu);
        return true;
    }

	public void onClick(View v) {
		
	    
		ImageView jpgView = (ImageView)findViewById(R.id.jpgview);
		
		System.out.println("***********onClick gestartet"); 
		//bildliste.setpic(treffer); //Alle Bilddateien an Pic Objekt übergeben
		 //System.out.println("onClick nach setpic"); 
					
	  //  bildliste.getpic().getAbsolutePath();
	    //System.out.println("***********onClick gestartet"); 
	//	File datei4 = bildliste.getpic();
	    //*//System.out.println("onClick geht weiter"+ datei4.getAbsolutePath()); 
		//System.out.println("nach Click Anzahl gefundener Dateien: "+bild.getAbsolutePath()); 
	    //bild = treffer.get(2);
	    
		
		
        //String myJpgPath = "/sdcard/external_sd/dcim/camera/20130323_133059.JPG"; //UPDATE WITH YOUR OWN JPG FILE
       // String myJpgPath = "/sdcard/test.JPG"; //UPDATE WITH YOUR OWN JPG FILE
	    
	    
       String myJpgPath = bildliste.getpic().getAbsolutePath();
        System.out.println("Bildnummer: "+bildnummer);
        System.out.println("Bildatei: "+myJpgPath);
        
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inSampleSize = 2;
        File bild4 = bildliste.getpic();
        
    		   
        Bitmap bm = BitmapFactory.decodeFile(myJpgPath, options);
		
		
		ImageView smiley = (ImageView) findViewById(R.id.smiley);
        ImageView sauri = (ImageView) findViewById(R.id.sauri);
        EditText nameField = (EditText) findViewById(R.id.name_field);
    
        // Tastatur ausblenden
        InputMethodManager imm = (InputMethodManager)getSystemService(
        	      Context.INPUT_METHOD_SERVICE);
        	imm.hideSoftInputFromWindow(nameField.getWindowToken(), 0);
		
		if (v == nochmalButton)
		{
		anzeige();
		smiley.setVisibility(View.INVISIBLE);
		sauri.setVisibility(View.INVISIBLE);
	    jpgView.setVisibility(View.INVISIBLE);
		nameField.setText("");
		return;
		}
		
		if (v == plusButton || v == minusButton)
			{
			if (v==plusButton)
				{
				if (plusButton.isChecked())
					{
					minusButton.setChecked(false);
					anzeige();
					}
				}
			if (v==minusButton)
			{
			if (minusButton.isChecked())
				{
				plusButton.setChecked(false);
				anzeige();
				}
			}
			return;
			}
			
		if (v == bis10 || v == bis100 || v == bis1000)
			{
			if (v==bis10)
				{
				if (bis10.isChecked())
					{
					bis100.setChecked(false);
					bis1000.setChecked(false);
					anzeige();
					}
			}
			if (v==bis100)
				{
				if (bis100.isChecked())
					{
					bis10.setChecked(false);
					bis1000.setChecked(false);
					anzeige();
					}
				}
			if (v==bis1000)
				{
				if (bis1000.isChecked())
					{
					bis10.setChecked(false);
					bis100.setChecked(false);
					anzeige();
					}
				}
			return;
			}
		
		
		String name = nameField.getText().toString();
		if (name.length() == 0) {
		    new AlertDialog.Builder(this) 
		            .setMessage(R.string.error_name_missing)
		            .setNeutralButton(R.string.error_ok, null)
		            .show();
		    return;
		}
		
		try {
			int eing1 = new Integer(name).intValue();				
	//		int eing1 = new Integer.valueOf(name);				
			
			
			int ergebnis;
			boolean plus = plusButton.isChecked();
			
			if (plus)
			{
				ergebnis = zahl1+zahl2;
			}
			else {
				ergebnis = zahl1-zahl2;
			}
				

		if (eing1 == ergebnis) {
			
			String greeting = "Richtig!!!!";
		 //   Toast.makeText(this, greeting, Toast.LENGTH_LONG).show();
		    richtig = true;
		    tts.speak(greeting, TextToSpeech.QUEUE_FLUSH, null);
		    smiley.setVisibility(View.VISIBLE);
		    sauri.setVisibility(View.INVISIBLE);		   
		    jpgView.setImageBitmap(bm);
		    jpgView.setVisibility(View.VISIBLE);
		    nameField.setText("");
		    anzeige();
			       
		    

	
		}
		else {
			String greeting = "Falsch!!!!";
		  //  Toast.makeText(this, greeting, Toast.LENGTH_LONG).show();
		    tts.speak(greeting, TextToSpeech.QUEUE_FLUSH, null);
		    smiley.setVisibility(View.INVISIBLE);
		    sauri.setVisibility(View.VISIBLE);
		    jpgView.setVisibility(View.INVISIBLE);
		    nameField.setText("");
		}
		}    
		catch( NumberFormatException e1) {
			  new AlertDialog.Builder(this) 
	            .setMessage(R.string.error_nurzahlen)
	            .setNeutralButton(R.string.error_ok, null)
	            .show();
	    return;
		}
			}

	public void onInit(int status) {
		//Diese Methode wird aufgerufen, wenn die Textausgabe bereit ist
		tts.setLanguage(Locale.GERMAN);
	}
	
	
}

class Suchen{
	String fullpath = null;
	String rueckgabe = null;
	String verz(String pfad, String MNummer){
		File temp= new File(pfad); // Pfad für Suche zusammenbauen
		
		File[] tempInhalt = temp.listFiles(); //Files im Pfad auslesen
 	System.out.println("Inhalt: "+ temp.getAbsolutePath());
		if (tempInhalt==null)
			System.exit(0);

		
		
		for(int i=0;i<tempInhalt.length;i++){
			System.out.println( tempInhalt[i].toString());
			if (tempInhalt[i].isDirectory()){
				System.out.println("ist Verzeichniss");
				String verzname=tempInhalt[i].toString();
				if (verzname.contains(MNummer)){
					System.out.print("hier ist das gewünschte Vezeichniss: ");
					String fullpath = tempInhalt[i].getPath();
					System.out.println(fullpath);
					rueckgabe = fullpath;
					
				}
			}
		}

		return rueckgabe;
		}
	public ArrayList<File> searchFile(File dir, String find) {
		//sucht alle Dateien mit einem bestimmten String			
		File[] files = dir.listFiles();
		System.out.println("Suchort: " + dir.toString());
		//System.out.println(" Anzahl Files:"+ files.length);
		
		ArrayList<File> matches = new ArrayList<File> ();
		if (files != null) {
			for (int i = 0; i < files.length; i++) {
				System.out.println("Prüfung von Datei: " + files[i].getName());
				
				if (files[i].getName().endsWith(find)) { // überprüft ob der Dateiname mit dem Suchstring
										 // übereinstimmt. Groß-/Kleinschreibung wird
										 // ignoriert.
					matches.add(files[i]);
					System.out.println("Treffer: " + files[i].getName());
				}
				if (files[i].isDirectory()) {
				System.out.println("Verzeichnis: " + files[i].getName());
					
					matches.addAll(searchFile(files[i], find)); // fügt der ArrayList die ArrayList mit den
										    // Treffern aus dem Unterordner hinzu
				}
			}
		}
		return matches;
}

}


 class Pic{
	int bildnummer;
//	static ArrayList<File> bilder = new ArrayList<File> (); //Arraylistobjekt anlegen
	
	private ArrayList<File> bilder;  
	
	public File getpic(){
		  File bild;
		  //gibt aus einer Arraylist ein bestimmtes Zufallsbild zurück
		  java.util.Random bildrandom = new java.util.Random(); //neues Randomobjekt erzeugen
		  bildnummer = bildrandom.nextInt(bilder.size());
		  bild = (File) bilder.get(bildnummer);
		  
		  System.out.println("Klasse Pic getPic: Anzahl: "+bilder.size()); 
		  System.out.println("Klasse Pic getPic: Anzahl: "+bild.getAbsolutePath()); 
		  
		  return bild;
	 }
	 
	public void setpic(ArrayList<File> bildliste){
		 bilder = bildliste;
		System.out.println("in Klasse Pic Anzahl: "+bilder.size()); 
		    
	 }
	 
 }
	
