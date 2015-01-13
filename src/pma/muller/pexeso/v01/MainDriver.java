package pma.muller.pexeso.v01;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;


public class MainDriver extends Activity {

	private int pocetHracu;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main_driver);
		
		//Listener na tla��tko OK (po�et hr���)
		Button button = (Button) findViewById(R.id.button2);
		button.setBackgroundResource(android.R.drawable.btn_default);
		button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                zadejPocetHracu();
        }});
		
		//Listener na tla��tko Nov� Hra
		button = (Button) findViewById(R.id.button1);
		button.setBackgroundResource(android.R.drawable.btn_default);
		button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                vytvorHru(pocetHracu);
        }});
		
		//Listener na zm�nu po�tu hr���
		EditText editable = (EditText)findViewById(R.id.editText1);
		editable.addTextChangedListener(zmenaPoctuHracuListener());
	}
	
	/*
	 *	Funkce hl�d� kolonku po�tu hr���. Pokud dojde ke zm�n�, jsou
	 *	p��slu�n� pole neviditeln�.
	 */
	private TextWatcher zmenaPoctuHracuListener(){
		return new TextWatcher(){
			 public void afterTextChanged(Editable s) {
					findViewById(R.id.editText3).setVisibility(View.INVISIBLE);
					findViewById(R.id.editText4).setVisibility(View.INVISIBLE);
					findViewById(R.id.button1).setVisibility(View.INVISIBLE);
		          }

			@Override
			public void beforeTextChanged(CharSequence s, int start, int count, int after) {	
			}

			@Override
			public void onTextChanged(CharSequence s, int start, int before, int count) {
			}
		};
	}
	
	/*
	 *	Funkce hl�d� rozsah hr��� od 1-2. Zobrazuje pot�ebn� okna pro dal�� nastaven�.
	 *	Pokud je po�et hr��� mimo rozsah, je zobrazen Toast. 
	 */
	private void zadejPocetHracu(){
		EditText pocet = (EditText)findViewById(R.id.editText1);
		CharSequence text = pocet.getText();

		pocetHracu = Integer.parseInt(text.toString());
		if(pocetHracu == 1){
			findViewById(R.id.editText3).setVisibility(View.VISIBLE);
			findViewById(R.id.button1).setVisibility(View.VISIBLE);
		}else if(pocetHracu == 2){
			findViewById(R.id.editText3).setVisibility(View.VISIBLE);
			findViewById(R.id.editText4).setVisibility(View.VISIBLE);
			findViewById(R.id.button1).setVisibility(View.VISIBLE);
		}else{
			Context context = getApplicationContext();
			CharSequence textt = "Hra pro 1-2 hr��e!";
			int duration = Toast.LENGTH_SHORT;
			Toast toast = Toast.makeText(context, textt, duration);
			toast.show();
		}
	}
	
	/*
	 * Funkce ulo�� nastaven� hr��e a ode�le je dal�� aktivit�.
	 */
	private void vytvorHru(int pocetHracu){
		Intent intent = new Intent(getApplicationContext(), Hra.class);
		Bundle bund = new Bundle();
		EditText hrac;
		bund.putInt("pocetHracu", pocetHracu);
		if(pocetHracu==1){
			hrac = (EditText)findViewById(R.id.editText3);
			bund.putString("prvniHrac", hrac.getText().toString());
		}else if(pocetHracu==2){
			hrac = (EditText)findViewById(R.id.editText3);
			bund.putString("prvniHrac", hrac.getText().toString());
			hrac = (EditText)findViewById(R.id.editText4);
			bund.putString("druhyHrac", hrac.getText().toString());
		}
		intent.putExtras(bund);
		startActivity(intent);
	}    
}
