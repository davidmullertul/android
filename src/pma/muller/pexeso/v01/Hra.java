package pma.muller.pexeso.v01;

import java.util.ArrayList;

import pma.muller.pexeso.v01.utils.Hrac;

import android.support.v7.app.ActionBarActivity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

public class Hra extends ActionBarActivity {
	private ArrayList<Hrac> hraci = new ArrayList<Hrac>();		//pole hráèù
	private Button kartaJedna;									//karta 1
	private Button kartaDve;									//karta 2
	private int tah = 1;										//aktuální tah
	private boolean kartaOtecena = false;						//kontrola otoèení pouze dvou karet	
	private TextView hracNaRade;								//zobrazení aktuálního hráèe
	private TextView pocetBodu;
	private int cisloAktualnihoHrace = 0;						//èíslo aktuálního hráèe
	private int pocetNalezenych = 0;							//poèet nalezných páru
	private int[] polePozadi;									//pole pro náhodné generování karet
	private int pocetHracu;										//poèet hráèù

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		DisplayMetrics rozmery = new DisplayMetrics();
		getWindowManager().getDefaultDisplay().getMetrics(rozmery);
		int sirka = rozmery.widthPixels;

		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_hlavni_okno);
		getSupportActionBar().hide();
		
		//získaní hodnot z druhého okna
		Intent intent = getIntent();
		Bundle b = intent.getExtras();
		pocetHracu = b.getInt("pocetHracu");
		if (pocetHracu == 1) {
			hraci.add(new Hrac(b.getString("prvniHrac")));
		} else {
			hraci.add(new Hrac(b.getString("prvniHrac")));
			hraci.add(new Hrac(b.getString("druhyHrac")));
		}

		generujPole();

		nastavTlacitka(sirka);

	}

	/*
	 * 	Funkce nastavuje všechna tlaèítka (karty) hry. Nastavuje jak jejich rozmìry, tak 
	 * 	jim pøiøazuje pøíslušné listenery.
	 */
	private void nastavTlacitka(int _sirka) {
		// Nastaví všem tlaèítkùm stejný listener
		OnClickListener listen = new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				try {
					zaznamenejTah((Button) v);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		};
		for (int i = 0; i < 12; i++) {
			String id = "button" + (i + 1);
			int resID = getResources()
					.getIdentifier(id, "id", getPackageName());
			Button button = (Button) findViewById(resID);
			button.setHeight(_sirka / 5);
			button.setWidth(_sirka / 4);
			button.setOnClickListener(listen);
			button.setText("" + polePozadi[i]);
			button.setBackgroundResource(android.R.drawable.btn_default);
		}

		// Nastaví listener pro tlaèítko Nová hra
		listen = listenerNovaHra();
		Button btn = (Button) findViewById(R.id.button13);
		btn.setOnClickListener(listen);
		btn.setBackgroundResource(android.R.drawable.btn_default);
		btn.setHeight(_sirka / 5);
		//zobrazení aktuálního stavu hry
		hracNaRade = (TextView) findViewById(R.id.textView1);
		hracNaRade.setText(hraci.get(0).getJmenoHrace());
		pocetBodu = (TextView) findViewById(R.id.textView2);
		pocetBodu.setText("Body: "
				+ hraci.get(cisloAktualnihoHrace).getPocetBodu());
	}

	/*
	 * Listener pro tlaèítko "Nová hra". Zobrazí okno s nabídkou nové hry.
	 * Pokud je nová hra potvrzena, dojde k jejímu vygenerování. Pokud ne,
	 * nic se nedìje.
	 */
	private OnClickListener listenerNovaHra() {
		return new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				AlertDialog.Builder builder = new AlertDialog.Builder(Hra.this);
				builder.setPositiveButton(R.string.ok,
						new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog, int id) {
								generujNovouHru();
							}
						});
				builder.setNegativeButton("Zpìt",
						new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog, int id) {
							}
						});
				builder.setMessage(
						String.format("Opravdu chcete založit novou hru?"))
						.setTitle("Pozor!!!");
				AlertDialog dialog = builder.create();
				dialog.show();
			}
		};
	}

	/*Funkce volaná pøi otoèení displeje. Nastavuje velikost obrazovky s hrou*/
	@Override
	public void onConfigurationChanged(Configuration newConfig) {

		super.onConfigurationChanged(newConfig);

		int orientation = newConfig.orientation;

		switch (orientation) {

		case Configuration.ORIENTATION_LANDSCAPE:
			DisplayMetrics rozmery = new DisplayMetrics();
			getWindowManager().getDefaultDisplay().getMetrics(rozmery);	//vrací rozmìry obrazovky
			int sirka = rozmery.widthPixels;							//aktuální šíøka displeje
			for (int i = 0; i < 12; i++) {								//nastavení šíøky všech tlaèítek
				String id = "button" + (i + 1);
				int resID = getResources().getIdentifier(id, "id",
						getPackageName());
				Button button = (Button) findViewById(resID);
				button.setHeight(sirka / 14);
				button.setWidth(sirka / 7);
			}
			Button btn = (Button) findViewById(R.id.button13);			//nastavení tlaèítka "Nová hra"
			btn.setBackgroundResource(android.R.drawable.btn_default);
			btn.setHeight(sirka / 14);
			break;

		case Configuration.ORIENTATION_PORTRAIT:
			rozmery = new DisplayMetrics();
			getWindowManager().getDefaultDisplay().getMetrics(rozmery);
			sirka = rozmery.widthPixels;
			for (int i = 0; i < 12; i++) {
				String id = "button" + (i + 1);
				int resID = getResources().getIdentifier(id, "id",
						getPackageName());
				Button button = (Button) findViewById(resID);
				button.setHeight(sirka / 5);
				button.setWidth(sirka / 4);
			}
			btn = (Button) findViewById(R.id.button13);
			btn.setBackgroundResource(android.R.drawable.btn_default);
			btn.setHeight(sirka / 5);
			break;
		}
	}

	// ************************************Moje metody**************************************//

	/*
	 * Funkce obsluhuje událost kliknutí na kartu. Sleduje poèet otoèených
	 * karet. Pokud není otoèená žádná karta, zmìní obrázek pozadí karty a její
	 * barvu. Dále je karta uzamèena, aby nebylo možné její opìtovné stiknutí.
	 * Pokud je otoèena karta druhá, nastaví opìt pozadí a barvu karty a uzamkne
	 * ji. Dále je jsou porovnána pozadí obou karet. Pokud se shodují, je
	 * aktuálnímu hráèi pøidán bod a pokraèuje se dále ve høe (dokud není
	 * konec). Pokud se neshodují, hraje další hráè. Funkce ještì nastaví
	 * zpoždìní pro otoèení karet na 1s.
	 */
	public void zaznamenejTah(Button button) throws InterruptedException {
		if (!kartaOtecena) {
			int cisloTlacitka;
			int resID = 0;
			//nalezení stisknutého tlaèítka
			for (int i = 0; i < 12; i++) {
				String id = "button" + (i + 1);
				resID = getResources()
						.getIdentifier(id, "id", getPackageName());
				Button btn = (Button) findViewById(resID);
				if (btn.equals(button))
					break;
			}

			cisloTlacitka = Integer.parseInt(button.getResources()					//zjistí èíslo tlaèítka
					.getResourceName(resID).substring(31)) - 1;
			//otoèení první karty
			if (tah == 1) {
				kartaJedna = button;
				tah++;
				resID = getResources().getIdentifier(								//nalezení obrázku pro pozadí tlaèítka
						"a" + polePozadi[cisloTlacitka], "drawable",
						getPackageName());
				kartaJedna.setBackgroundResource(resID);							//nastavení pozadí karty
				kartaJedna.setEnabled(false);										//uzamknutí karty
			//otoèení druhé karty
			} else {
				kartaDve = button;
				resID = getResources().getIdentifier(
						"a" + polePozadi[cisloTlacitka], "drawable",
						getPackageName());
				kartaDve.setBackgroundResource(resID);
				// Nalezen stejný pár
				if ((kartaJedna.getBackground().getConstantState()
						.equals(kartaDve.getBackground().getConstantState()))) {
					hraci.get(cisloAktualnihoHrace).zaznamenejTah();
					hraci.get(cisloAktualnihoHrace).zaznamenejBod();
					kartaDve.setEnabled(false);
					pocetNalezenych++;
					//zobrazit aktuální stav
					hracNaRade.setText(hraci.get(cisloAktualnihoHrace)
							.getJmenoHrace());
					pocetBodu.setText("Body: "
							+ hraci.get(cisloAktualnihoHrace).getPocetBodu());
					//Konec hry
					if (testKonec()) {
						generujVysledky();
					}
				} else {
					// Pár nenalezen
					kartaOtecena = !kartaOtecena;
					kartaJedna.setEnabled(true);
					hraci.get(cisloAktualnihoHrace).zaznamenejTah();
					cisloAktualnihoHrace++;
					if (cisloAktualnihoHrace == hraci.size())
						cisloAktualnihoHrace = 0;
					hracNaRade.setText(hraci.get(cisloAktualnihoHrace)
							.getJmenoHrace());
					pocetBodu.setText("Body: "
							+ hraci.get(cisloAktualnihoHrace).getPocetBodu());
					// zde pomalejší zmìna pozadí
					kartaDve.postDelayed(new Runnable() {
						@Override
						public void run() {
							kartaJedna
									.setBackgroundResource(android.R.drawable.btn_default);
							kartaDve.setBackgroundResource(android.R.drawable.btn_default);
							kartaOtecena = !kartaOtecena;
						}
					}, 1000);
				}
				tah = 1;
			}
		}
	}

	/*
	 * Funkce generuje výsledky aktuální hry. Výsledky jsou zobrazeny ve
	 * vyskakovacím oknì. Následnì je generována nová hra.
	 */
	private void generujVysledky() {
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		String vypis = "";
		builder.setPositiveButton(R.string.ok,
				new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int id) {
						generujNovouHru();
					}
				});
		// generování výsledkù obou hráèù
		for (int i = 0; i < pocetHracu; i++) {
			vypis += hraci.get(i).getJmenoHrace() + "\t\t"
					+ hraci.get(i).getPocetBodu() + "/"
					+ hraci.get(i).getPocetTahu() + "\n";
		}
		builder.setMessage(vypis).setTitle("Výsledky");
		AlertDialog dialog = builder.create();
		dialog.show();
	}

	/*
	 * Funkce vygeneruje na náhodná neobsazená místo v poli dvojce èísel od 1-6,
	 * která jsou následnì využita pro výbìr pozadí karet na daných pozicích.
	 */
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

	/*
	 * Funkce testuje konec hry na základì poètu nalezených párù.
	 */
	public boolean testKonec() {
		if (pocetNalezenych == 6)
			return true;
		return false;
	}

	/*
	 * Funkce generující novou hru. Resetují se všechna tlaèítka, vygeneruje se
	 * nové pole pro výbìr pozadí, resetují se body hráèù a potøebné promìnné.
	 * Následnì je zobrazen stav hry.
	 */
	protected void generujNovouHru() {
		for (int i = 0; i < 12; i++) {
			String id = "button" + (i + 1);
			int resID = getResources()
					.getIdentifier(id, "id", getPackageName());
			Button button = (Button) findViewById(resID);
			button.setBackgroundResource(android.R.drawable.btn_default);
			button.setEnabled(true);
		}

		generujPole();

		for (int i = 0; i < pocetHracu; i++) {
			hraci.get(i).resetHrac();
		}
		cisloAktualnihoHrace = 0;
		pocetNalezenych = 0;
		hracNaRade.setText(hraci.get(cisloAktualnihoHrace).getJmenoHrace());
		pocetBodu.setText("Body: "
				+ hraci.get(cisloAktualnihoHrace).getPocetBodu());
	}

}
