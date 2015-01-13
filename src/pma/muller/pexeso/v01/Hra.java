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
	private ArrayList<Hrac> hraci = new ArrayList<Hrac>();		//pole hr���
	private Button kartaJedna;									//karta 1
	private Button kartaDve;									//karta 2
	private int tah = 1;										//aktu�ln� tah
	private boolean kartaOtecena = false;						//kontrola oto�en� pouze dvou karet	
	private TextView hracNaRade;								//zobrazen� aktu�ln�ho hr��e
	private TextView pocetBodu;
	private int cisloAktualnihoHrace = 0;						//��slo aktu�ln�ho hr��e
	private int pocetNalezenych = 0;							//po�et nalezn�ch p�ru
	private int[] polePozadi;									//pole pro n�hodn� generov�n� karet
	private int pocetHracu;										//po�et hr���

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		DisplayMetrics rozmery = new DisplayMetrics();
		getWindowManager().getDefaultDisplay().getMetrics(rozmery);
		int sirka = rozmery.widthPixels;

		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_hlavni_okno);
		getSupportActionBar().hide();
		
		//z�skan� hodnot z druh�ho okna
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
	 * 	Funkce nastavuje v�echna tla��tka (karty) hry. Nastavuje jak jejich rozm�ry, tak 
	 * 	jim p�i�azuje p��slu�n� listenery.
	 */
	private void nastavTlacitka(int _sirka) {
		// Nastav� v�em tla��tk�m stejn� listener
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

		// Nastav� listener pro tla��tko Nov� hra
		listen = listenerNovaHra();
		Button btn = (Button) findViewById(R.id.button13);
		btn.setOnClickListener(listen);
		btn.setBackgroundResource(android.R.drawable.btn_default);
		btn.setHeight(_sirka / 5);
		//zobrazen� aktu�ln�ho stavu hry
		hracNaRade = (TextView) findViewById(R.id.textView1);
		hracNaRade.setText(hraci.get(0).getJmenoHrace());
		pocetBodu = (TextView) findViewById(R.id.textView2);
		pocetBodu.setText("Body: "
				+ hraci.get(cisloAktualnihoHrace).getPocetBodu());
	}

	/*
	 * Listener pro tla��tko "Nov� hra". Zobraz� okno s nab�dkou nov� hry.
	 * Pokud je nov� hra potvrzena, dojde k jej�mu vygenerov�n�. Pokud ne,
	 * nic se ned�je.
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
				builder.setNegativeButton("Zp�t",
						new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog, int id) {
							}
						});
				builder.setMessage(
						String.format("Opravdu chcete zalo�it novou hru?"))
						.setTitle("Pozor!!!");
				AlertDialog dialog = builder.create();
				dialog.show();
			}
		};
	}

	/*Funkce volan� p�i oto�en� displeje. Nastavuje velikost obrazovky s hrou*/
	@Override
	public void onConfigurationChanged(Configuration newConfig) {

		super.onConfigurationChanged(newConfig);

		int orientation = newConfig.orientation;

		switch (orientation) {

		case Configuration.ORIENTATION_LANDSCAPE:
			DisplayMetrics rozmery = new DisplayMetrics();
			getWindowManager().getDefaultDisplay().getMetrics(rozmery);	//vrac� rozm�ry obrazovky
			int sirka = rozmery.widthPixels;							//aktu�ln� ���ka displeje
			for (int i = 0; i < 12; i++) {								//nastaven� ���ky v�ech tla��tek
				String id = "button" + (i + 1);
				int resID = getResources().getIdentifier(id, "id",
						getPackageName());
				Button button = (Button) findViewById(resID);
				button.setHeight(sirka / 14);
				button.setWidth(sirka / 7);
			}
			Button btn = (Button) findViewById(R.id.button13);			//nastaven� tla��tka "Nov� hra"
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
	 * Funkce obsluhuje ud�lost kliknut� na kartu. Sleduje po�et oto�en�ch
	 * karet. Pokud nen� oto�en� ��dn� karta, zm�n� obr�zek pozad� karty a jej�
	 * barvu. D�le je karta uzam�ena, aby nebylo mo�n� jej� op�tovn� stiknut�.
	 * Pokud je oto�ena karta druh�, nastav� op�t pozad� a barvu karty a uzamkne
	 * ji. D�le je jsou porovn�na pozad� obou karet. Pokud se shoduj�, je
	 * aktu�ln�mu hr��i p�id�n bod a pokra�uje se d�le ve h�e (dokud nen�
	 * konec). Pokud se neshoduj�, hraje dal�� hr��. Funkce je�t� nastav�
	 * zpo�d�n� pro oto�en� karet na 1s.
	 */
	public void zaznamenejTah(Button button) throws InterruptedException {
		if (!kartaOtecena) {
			int cisloTlacitka;
			int resID = 0;
			//nalezen� stisknut�ho tla��tka
			for (int i = 0; i < 12; i++) {
				String id = "button" + (i + 1);
				resID = getResources()
						.getIdentifier(id, "id", getPackageName());
				Button btn = (Button) findViewById(resID);
				if (btn.equals(button))
					break;
			}

			cisloTlacitka = Integer.parseInt(button.getResources()					//zjist� ��slo tla��tka
					.getResourceName(resID).substring(31)) - 1;
			//oto�en� prvn� karty
			if (tah == 1) {
				kartaJedna = button;
				tah++;
				resID = getResources().getIdentifier(								//nalezen� obr�zku pro pozad� tla��tka
						"a" + polePozadi[cisloTlacitka], "drawable",
						getPackageName());
				kartaJedna.setBackgroundResource(resID);							//nastaven� pozad� karty
				kartaJedna.setEnabled(false);										//uzamknut� karty
			//oto�en� druh� karty
			} else {
				kartaDve = button;
				resID = getResources().getIdentifier(
						"a" + polePozadi[cisloTlacitka], "drawable",
						getPackageName());
				kartaDve.setBackgroundResource(resID);
				// Nalezen stejn� p�r
				if ((kartaJedna.getBackground().getConstantState()
						.equals(kartaDve.getBackground().getConstantState()))) {
					hraci.get(cisloAktualnihoHrace).zaznamenejTah();
					hraci.get(cisloAktualnihoHrace).zaznamenejBod();
					kartaDve.setEnabled(false);
					pocetNalezenych++;
					//zobrazit aktu�ln� stav
					hracNaRade.setText(hraci.get(cisloAktualnihoHrace)
							.getJmenoHrace());
					pocetBodu.setText("Body: "
							+ hraci.get(cisloAktualnihoHrace).getPocetBodu());
					//Konec hry
					if (testKonec()) {
						generujVysledky();
					}
				} else {
					// P�r nenalezen
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
					// zde pomalej�� zm�na pozad�
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
	 * Funkce generuje v�sledky aktu�ln� hry. V�sledky jsou zobrazeny ve
	 * vyskakovac�m okn�. N�sledn� je generov�na nov� hra.
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
		// generov�n� v�sledk� obou hr���
		for (int i = 0; i < pocetHracu; i++) {
			vypis += hraci.get(i).getJmenoHrace() + "\t\t"
					+ hraci.get(i).getPocetBodu() + "/"
					+ hraci.get(i).getPocetTahu() + "\n";
		}
		builder.setMessage(vypis).setTitle("V�sledky");
		AlertDialog dialog = builder.create();
		dialog.show();
	}

	/*
	 * Funkce vygeneruje na n�hodn� neobsazen� m�sto v poli dvojce ��sel od 1-6,
	 * kter� jsou n�sledn� vyu�ita pro v�b�r pozad� karet na dan�ch pozic�ch.
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
	 * Funkce testuje konec hry na z�klad� po�tu nalezen�ch p�r�.
	 */
	public boolean testKonec() {
		if (pocetNalezenych == 6)
			return true;
		return false;
	}

	/*
	 * Funkce generuj�c� novou hru. Resetuj� se v�echna tla��tka, vygeneruje se
	 * nov� pole pro v�b�r pozad�, resetuj� se body hr��� a pot�ebn� prom�nn�.
	 * N�sledn� je zobrazen stav hry.
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
