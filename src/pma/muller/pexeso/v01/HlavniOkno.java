package pma.muller.pexeso.v01;

import java.util.ArrayList;

import pma.muller.pexeso.v01.utils.Hrac;

import android.support.v7.app.ActionBarActivity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.os.Handler;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

public class HlavniOkno extends ActionBarActivity {
	private ArrayList<Hrac> hraci = new ArrayList<Hrac>();
	private Button kartaJedna;
	private Button kartaDve;
	private int tah = 1;
	private TextView hracNaRade;
	private TextView pocetBodu;
	private int cisloAktualnihoHrace = 0;
	private int pocetNalezenych = 0;
	private int[] polePozadi;
	int pocetHracu;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		DisplayMetrics rozmery = new DisplayMetrics();
		getWindowManager().getDefaultDisplay().getMetrics(rozmery);
		int sirka = rozmery.widthPixels;

		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_hlavni_okno);
		getSupportActionBar().hide();
		Intent intent = getIntent();
		Bundle b = intent.getExtras();
		pocetHracu = b.getInt("pocetHracu");
		/* Potom smazat - pro testovací úèely!!!!!! */
		if (pocetHracu == 1) {
			hraci.add(new Hrac(b.getString("prvniHrac")));
		} else {
			hraci.add(new Hrac(b.getString("prvniHrac")));
			hraci.add(new Hrac(b.getString("druhyHrac")));
		}

		generujPole();

		// Nastaví všem tlaèítkùm stejný listener
		OnClickListener listen = new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				zaznamenejTah((Button) v);
			}
		};
		for (int i = 0; i < 12; i++) {
			String id = "button" + (i + 1);
			int resID = getResources()
					.getIdentifier(id, "id", getPackageName());
			Button button = (Button) findViewById(resID);
			button.setHeight(sirka / 5);
			button.setWidth(sirka / 4);
			button.setOnClickListener(listen);
			button.setText("" + polePozadi[i]);
			button.setBackgroundResource(android.R.drawable.btn_default);
		}

		// Nastaví listener pro tlaèítko Nová hra
		listen = new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				AlertDialog.Builder builder = new AlertDialog.Builder(HlavniOkno.this);
				builder.setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
				           public void onClick(DialogInterface dialog, int id) {
				        	    generujNovouHru();
				           }
				       });
				builder.setNegativeButton("Zpìt", new DialogInterface.OnClickListener() {
				           public void onClick(DialogInterface dialog, int id) {
				           }
				       });
				builder.setMessage(String.format("Opravdu chcete založit novou hru?"))
			       .setTitle("Pozor!!!");
				AlertDialog dialog = builder.create();
				dialog.show();
			}
		};

		Button btn = (Button) findViewById(R.id.button13);
		btn.setOnClickListener(listen);
		btn.setBackgroundResource(android.R.drawable.btn_default);
		btn.setHeight(sirka / 5);

		hracNaRade = (TextView) findViewById(R.id.textView1);
		hracNaRade.setText(hraci.get(0).getJmenoHrace());
		pocetBodu = (TextView) findViewById(R.id.textView2);
		pocetBodu.setText("Body: "	+ hraci.get(cisloAktualnihoHrace).getPocetBodu());
		
	}
@Override
public void onConfigurationChanged (Configuration newConfig){
		
		super.onConfigurationChanged(newConfig);
		
		int orientation=newConfig.orientation;

		switch(orientation) {

		case Configuration.ORIENTATION_LANDSCAPE:
			DisplayMetrics rozmery = new DisplayMetrics();
			getWindowManager().getDefaultDisplay().getMetrics(rozmery);
			int sirka = rozmery.widthPixels;
			for (int i = 0; i < 12; i++) {
				String id = "button" + (i + 1);
				int resID = getResources()
						.getIdentifier(id, "id", getPackageName());
				Button button = (Button) findViewById(resID);
				button.setHeight(sirka / 14);
				button.setWidth(sirka / 7);
			}
			Button btn = (Button) findViewById(R.id.button13);
			btn.setBackgroundResource(android.R.drawable.btn_default);
			btn.setHeight(sirka / 14);

		//to do something
		 break;

		case Configuration.ORIENTATION_PORTRAIT:
			rozmery = new DisplayMetrics();
			getWindowManager().getDefaultDisplay().getMetrics(rozmery);
			sirka = rozmery.widthPixels;
			for (int i = 0; i < 12; i++) {
				String id = "button" + (i + 1);
				int resID = getResources()
						.getIdentifier(id, "id", getPackageName());
				Button button = (Button) findViewById(resID);
				button.setHeight(sirka / 5);
				button.setWidth(sirka / 4);
			}
			btn = (Button) findViewById(R.id.button13);
			btn.setBackgroundResource(android.R.drawable.btn_default);
			btn.setHeight(sirka / 5);
		//to do something
		 break;
		 }

		}
	// ************************************Moje metody**************************************//
	public void zaznamenejTah(Button button) {
		int cisloTlacitka;
		int resID = 0;
		for (int i = 0; i < 12; i++) {
			String id = "button" + (i + 1);
			resID = getResources().getIdentifier(id, "id", getPackageName());
			Button btn = (Button) findViewById(resID);
			if (btn.equals(button))
				break;
		}
		cisloTlacitka = Integer.parseInt(button.getResources()
				.getResourceName(resID).substring(31)) - 1;
		if (tah == 1) {
			kartaJedna = button;
			tah++;
			resID = getResources().getIdentifier(
					"a" + polePozadi[cisloTlacitka], "drawable",
					getPackageName());
			kartaJedna.setBackgroundResource(resID);
			kartaJedna.setEnabled(false);
		} else {
			kartaDve = button;
			resID = getResources().getIdentifier(
					"a" + polePozadi[cisloTlacitka], "drawable",
					getPackageName());
			kartaDve.setBackgroundResource(resID);
			// Nalezen stejný pár
			if ((kartaJedna.getBackground().getConstantState().equals(kartaDve
					.getBackground().getConstantState()))) {
				hraci.get(cisloAktualnihoHrace).zaznamenejTah();
				hraci.get(cisloAktualnihoHrace).zaznamenejBod();
				kartaDve.setEnabled(false);
				pocetNalezenych++;
				hracNaRade.setText(hraci.get(cisloAktualnihoHrace).getJmenoHrace());
				pocetBodu.setText("Body: "	+ hraci.get(cisloAktualnihoHrace).getPocetBodu());
				if(testKonec()){
					generujVysledky();
				}
			} else {
				// Pár nenalezen
				kartaJedna.setEnabled(true);
				hraci.get(cisloAktualnihoHrace).zaznamenejTah();
				cisloAktualnihoHrace++;
				if (cisloAktualnihoHrace == hraci.size())
					cisloAktualnihoHrace = 0;
				hracNaRade.setText(hraci.get(cisloAktualnihoHrace).getJmenoHrace());
				pocetBodu.setText("Body: "	+ hraci.get(cisloAktualnihoHrace).getPocetBodu());
				kartaJedna.setBackgroundResource(android.R.drawable.btn_default);
				
				//kartaDve.setBackgroundResource(android.R.drawable.btn_default);
			}
			tah = 1;
		}
	}

	private void generujVysledky() {
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		String vypis = "";
		builder.setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
		           public void onClick(DialogInterface dialog, int id) {
		        	    generujNovouHru();
		           }
		       });
		for(int i = 0; i < pocetHracu; i++){
			vypis += hraci.get(i).getJmenoHrace()+"\t\t"+hraci.get(i).getPocetBodu()+"/"+hraci.get(i).getPocetTahu()+"\n";
		}
		builder.setMessage(vypis)
	       .setTitle("Výsledky");
		AlertDialog dialog = builder.create();
		dialog.show();
	}

	public void generujPole() {
		boolean zarazeno;
		int pozice;
		int cislo = 1;
		polePozadi = new int[12];
		for (int i = 0; i < 12; i++) {
			zarazeno = false;
			while (!zarazeno) {
				pozice = (int) (Math.random() * 12);
				if (polePozadi[pozice] == 0) {
					polePozadi[pozice] = cislo;
					cislo++;
					zarazeno = true;
					cislo = (cislo == 7 ? 1 : cislo);
				}
			}
		}
	}

	public boolean testKonec() {
		if (pocetNalezenych == 6)
			return true;
		return false;
	}

	protected void generujNovouHru() {
		for (int i = 0; i < 12; i++) {
			String id = "button" + (i + 1);
			int resID = getResources()
					.getIdentifier(id, "id", getPackageName());
			Button button = (Button) findViewById(resID);
			button.setBackgroundResource(android.R.drawable.btn_default);
			button.setEnabled(true);
			generujPole();
		}

		for (int i = 0; i < pocetHracu; i++) {
			hraci.get(i).resetHrac();
		}
		cisloAktualnihoHrace = 0;
		pocetNalezenych = 0;
		hracNaRade.setText(hraci.get(cisloAktualnihoHrace).getJmenoHrace());
		pocetBodu.setText("Body: "	+ hraci.get(cisloAktualnihoHrace).getPocetBodu());
	}

}
